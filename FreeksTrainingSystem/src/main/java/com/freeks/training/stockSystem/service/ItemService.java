package com.freeks.training.stockSystem.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.freeks.training.stockSystem.converter.ItemConverter;
import com.freeks.training.stockSystem.entity.ItemInfoEntity;
import com.freeks.training.stockSystem.entity.ItemStockEntity;
import com.freeks.training.stockSystem.form.ItemInfoForm;
import com.freeks.training.stockSystem.form.ItemListForm;
import com.freeks.training.stockSystem.form.ItemStockForm;
import com.freeks.training.stockSystem.mapper.ItemInfoMapper;
import com.freeks.training.stockSystem.util.MessageEnum;

@Service
public class ItemService {
	
	// 商品情報Mapperのフィールド変数
	@Autowired
	private ItemInfoMapper itemInfoMapper;
	
	// 商品情報変換のフィールド変数
	@Autowired
	private ItemConverter itemConverter;
	
	// 環境依存しない改行コード
	public static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	
	/**
	 *  商品リスト取得メソッド
	 * @return itemListForm 取得した商品リストがnullならエラーメッセージ表示
	 */
	public ItemListForm getItemListAll(){
		ItemListForm itemListForm = new ItemListForm();

		try {
			// 商品情報のリストを取得
			List<ItemInfoEntity> entityList = itemInfoMapper.getFindAll();
			
			// リストサイズが空なら「システムエラー：商品情報が見つかりません」を返す
			if(entityList.isEmpty()) {
				itemListForm.setErrMsg(MessageEnum.ITEM_INFO_NOT_FOUND.getMessage());

			}else {
				// itemInfoMapper.getFindAll()で取得したentityリストをformリストに変換して返す
				List<ItemInfoForm> formList = itemConverter.entityListToFormList(entityList);
				itemListForm.setItemInfoFormList(formList);
			}
			
		}catch(Exception e){
			// 例外発生時は「システムエラー：DB接続に失敗しました」のメッセージを返す
			itemListForm.setErrMsg(MessageEnum.SYSYEM_ERROR_GET_DB_DATA.getMessage());
			
		}
		
		return itemListForm;
		
	}
	
	// CSV出力ロジック
	public boolean generateCsv(HttpServletResponse response) throws IOException {
		
		List<ItemInfoForm> itemList = this.getItemListAll().getItemInfoFormList();
		
		if(itemList == null || itemList.isEmpty()) {
			// データがなければfalse
			return false;
		}
		
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		
		// 書き込み処理			
			// ヘッダー書き込み
			writer.println("商品ID,商品名称,区分,メーカー,JANコード,購入単価,販売単価,保管場所,入庫日");
			
			// 各データ書き込み
			for(ItemInfoForm itemInfoForm : itemList) {
				writer.println(
					String.join(",",
					String.valueOf(itemInfoForm.getItemId()),
					escapeCsvField(itemInfoForm.getItemName()),
					escapeCsvField(itemInfoForm.getItemKubun()),
					escapeCsvField(itemInfoForm.getMaker()),
					escapeCsvField(itemInfoForm.getJancd()),
					escapeCsvField(itemInfoForm.getPurchaseUnitPrice()),
					escapeCsvField(itemInfoForm.getSalesUnitPrice()),
					escapeCsvField(itemInfoForm.getStorageLocation()),
					escapeCsvField(itemInfoForm.getReceiptDate())
					)
				);
			}
			
			//  データを確定
			writer.flush();
			String csvData = stringWriter.toString();
			
			//  CSVデータのバイト長を取得し、Content-Lengthを設定
			byte[] csvBytes = csvData.getBytes("Windows-31J");
			response.setContentLength(csvBytes.length);
			
			
			//  レスポンスに書き込む
			response.getOutputStream().write(csvBytes);
			response.getOutputStream().flush();
			
			return true;
		
	}
	

    // CSVフィールドのエスケープ処理（カンマや改行を含む場合に対応）
    private String escapeCsvField(String field) {
        if (field == null) {
            return "";
        }
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }
	
    
	/**
	 *  商品名称重複チェック
	 * @param requestItemName 受け取った商品名で重複チェック
	 * @return 重複してなければnullを返す
	 */
	
	public String checkDuplicateItemName(String requestItemName) {
		
		// エラーメッセージ格納用変数。
		String isDuplicate = null;
		
		try {
			// 受信した商品名をDBから取得。比較のためString型に変換
			ItemInfoEntity checkNameDuplicate = itemInfoMapper.findByItemName(requestItemName);
			
			// 商品名がすでに登録されている場合「商品名称が重複しています」を返す
			if(checkNameDuplicate != null) {
				// エラーメッセージをitemInfoFormにセットして、String型に変換
				isDuplicate = MessageEnum.DUPLICATE_ITEM_NAME.getMessage();
			}
			
		}catch(Exception e){
			// 例外発生時は「システムエラー：DB接続に失敗しました」のメッセージを返す
			isDuplicate = MessageEnum.SYSYEM_ERROR_GET_DB_DATA.getMessage();
		}
		
		return isDuplicate;
	}
	
	
	/** 商品情報登録メソッド
	 * 
	 * @param requestForm ItemInfoForm型をItemInfoEntity型にしてMapper.insertで登録
	 * @param loginUser 定数"freeks"
	 * @return 商品情報リストを返す。登録または商品リスト取得に失敗した場合はエラーメッセージを、成功すればシステムメッセージを返す
	 * @return いずれかの登録処理以降で例外が発生した場合、Controllerに例外をスローしてすべての登録処理をロールバックさせる
	 */
	
	@Transactional(rollbackFor = Exception.class)
	public ItemListForm createItem(ItemInfoForm requestForm, String loginUser) {
		
		ItemListForm itemListForm = new ItemListForm();
		ItemInfoEntity itemInfoEntity = new ItemInfoEntity();
		String errMsg = null;  // エラーメッセージ格納用
		String sysMsg = null;  // システムメッセージ格納用
		int resultInsertItem = 0;
		int resultInsertStock = 0;
		
		try {
			
			//// 商品登録処理 ////			
			// Form ⇒ Entity変換処理(DB側のデータ型に合わせるため)
			itemInfoEntity = itemConverter.insertFormToEntity(requestForm, loginUser);

			// 商品情報登録
			resultInsertItem = itemInfoMapper.insert(itemInfoEntity);
			
			
			//// 在庫情報登録処理 ////
			// ここから商品リスト取得までの間で例外が発生したら、登録処理をロールバックさせる
			
			// 登録した商品情報から共通データを取得
			ItemInfoEntity itemInfo = itemInfoMapper.findByItemName(requestForm.getItemName());
			
			// 取得した共通データを使って在庫情報を設定
			ItemStockEntity stockEntity = itemConverter.createStockEntity(itemInfo,loginUser);
			
			// 在庫情報登録
			resultInsertStock = itemInfoMapper.stockInsert(stockEntity);
			
			//// 登録成否判定 ////
			// 商品と在庫の登録Mapperの戻り値が1以上なら登録成功
			// ※影響を受けたレコードの行数を返す(成功したら通常は1が返る)
			if(resultInsertItem > 0  && resultInsertStock > 0){
				
				// 登録成功のメッセージをsysMsgに代入
				sysMsg = MessageEnum.SUCCESS_ITEM_DATA_BASE.getMessage();
			
			}else if(resultInsertItem == 0 && resultInsertStock == 0){
				
				// 両方失敗なら商品リストだけ取得して一覧画面にメッセージ表示
				errMsg = MessageEnum.FAILED_ITEM_DATABASE.getMessage();
		
			}else if(resultInsertItem == 0 || resultInsertStock == 0) {
				
				// 商品情報登録か在庫登録のどちらかが失敗した場合、ロールバック処理。Controllerへ例外をスロー
				// この条件分岐よりも前に例外が発生した場合に備えて、具体的なメッセージはCatch内で再設定
				throw new RuntimeException();
				
			}

			
			//// 商品リスト取得 ////
			itemListForm = this.getItemListAll();
			
			// 登録失敗＆商品リスト取得メソッド内でエラーが発生した場合、二つ繋げて改行した状態でerrMsgを上書き
			if(itemListForm.getErrMsg() != null && errMsg != null) {
				errMsg = errMsg + LINE_SEPARATOR + itemListForm.getErrMsg();
				
			// 登録成功＆商品リスト取得メソッド内でエラーが発生した場合、商品リスト取得メソッドのエラーメッセージのみ代入
			}else if(itemListForm.getErrMsg() != null) {
				errMsg = itemListForm.getErrMsg();
			}
			
			// エラーメッセージとシステムメッセージをそれぞれ設定
			itemListForm.setErrMsg(errMsg);
			itemListForm.setSysMsg(sysMsg);
			
		}catch(Exception e) {
			/* 登録に失敗したら実行済みの登録処理を
			   ロールバックさせるため、Controllerに例外をスロー */
			throw new RuntimeException(MessageEnum.FAILED_CREATE_ITEM.getMessage(), e);
		}
		
		return itemListForm;
	}
	
	
	/**
	 *  商品情報取得メソッド
	 * @param itemId 商品リスト画面で選択した商品IDを受け取る
	 * @return 商品情報を返す。取得した商品IDがnullならエラーメッセージを返す
	 */
	public ItemInfoForm getItemDetail(int itemId) {
		ItemInfoForm itemInfoForm = new ItemInfoForm();
		
		try {
			
			// 選択した商品IDをキーにして商品情報を取得
			ItemInfoEntity entity = itemInfoMapper.findByItemId(itemId);
			
			if(entity == null) {
				itemInfoForm.setErrMsg(MessageEnum.ITEM_INFO_NOT_FOUND.getMessage());
			
			}else {
				itemInfoForm = itemConverter.entityToForm(entity);
			}
			
		}catch(Exception e){
			itemInfoForm.setErrMsg(MessageEnum.SYSYEM_ERROR_GET_DB_DATA.getMessage());
		}
		
		return itemInfoForm;
	}
	
	
	/**
	 * 在庫情報取得メソッド
	 * @param itemId
	 * @return 在庫情報かエラーメッセージを返す
	 */

	public ItemStockForm getItemStock(int itemId) {
		
		ItemStockForm itemStockForm = new ItemStockForm();
		
		try {
			// 選択した商品の商品IDをキーにして在庫情報を取得
			ItemStockEntity stockEntity = itemInfoMapper.findStockByItemId(itemId); 
			if(stockEntity == null) {
				// DBの検索結果がNullの場合はエラーメッセージをセット
				itemStockForm.setErrMsg(MessageEnum.STOCK_INFO_NOT_FOUND.getMessage());
			
			}else {
				itemStockForm = itemConverter.stockEentityToForm(stockEntity);
			}
			
		}catch(Exception e) {
			// 例外発生時は「システムエラー：DB接続に失敗しました」のメッセージを返す
			itemStockForm.setErrMsg(MessageEnum.SYSYEM_ERROR_GET_DB_DATA.getMessage());
		}
		
		return itemStockForm;
	}
	
	
	/**
	 *  更新情報確認処理
	 * @param beforeForm 更新前の在庫情報
	 * @param afterForm 更新後の在庫情報
	 * @return エラーがなければnullを返す
	 */
	public String updateCheck(ItemStockForm beforeForm, ItemStockForm afterForm) {
		
		String updateCheckResult = null;  // エラーメッセージ格納用
		
			
		// 念のためNullチェック
		if(beforeForm == null || afterForm == null) {
			return updateCheckResult = MessageEnum.SYSYEM_ERROR_GET_DB_DATA.getMessage();
		}
		
		// 更新前の在庫数代入
		int beforeStockQuantity = Integer.parseInt(beforeForm.getStockQuantity());
		
		// 受け取った入庫数代入
		int requestReceiveQuantity = Integer.parseInt(afterForm.getReceiveQuantity());
		
		// 受け取った出庫数代入
		int requestDispatchQuantity = Integer.parseInt(afterForm.getDispatchQuantity());
		
		// 更新前の保管場所代入
		String beforeStorageLocation = beforeForm.getStorageLocation();
		
		// 受け取った保管場所代入
		String requestStorageLocation = afterForm.getStorageLocation();
		
		
		//// 各更新項目のbeforeとrequestの差異チェック ////
		// (受け取った入庫数 - 受け取った出庫数)が0＆保管場所が同じの場合
		if(requestReceiveQuantity - requestDispatchQuantity == 0 && beforeStorageLocation.equals(requestStorageLocation)){
			
			// 差異がなければ「更新情報に変更箇所がありません」のエラーメッセージ表示
			updateCheckResult = MessageEnum.STOCK_NOUPDATE_INFO.getMessage();
		
		// (更新前在庫数 + 受け取った入庫数 - 受け取った出庫数)が0未満の場合
		}else if(0 > beforeStockQuantity + requestReceiveQuantity - requestDispatchQuantity) {
			
			// 「在庫数が不足しています」のエラーメッセージ表示
			updateCheckResult = MessageEnum.STOCK_QUANTITY_SHORTAGE.getMessage();
		}
		
		return updateCheckResult;
	}
	
	
	/**
	 * 在庫情報更新メソッド
	 * @param requestForm フォームから送信される更新情報
	 * @param beforeForm 更新前の在庫情報
	 * @param loginUser 定数"freeks"
	 * @return 商品情報リストを返す。更新または商品リスト取得に失敗した場合はエラーメッセージを、成功すればシステムメッセージを返す
	 * @return いずれかの更新処理以降で例外が発生した場合、Controllerに例外をスローしてすべての更新処理をロールバックさせる
	 */
	@Transactional(rollbackFor = Exception.class)
	public ItemListForm updateStock(ItemStockForm requestForm, ItemStockForm beforeForm, String loginUser){
		
		ItemListForm itemListForm = new ItemListForm();
		
		
		String errMsg = null;  // エラーメッセージ格納用
		String sysMsg = null;  // システムメッセージ格納用
		
		try {
			ItemStockEntity itemStockEntity = itemConverter.formToEntity(requestForm, beforeForm, loginUser);
			
			// 在庫情報更新処理
			int updateStockResult = itemInfoMapper.updateStock(itemStockEntity);
			
			// 商品情報更新処理
			// ここで例外が発生したら商品情報登録処理をロールバックさせる
			int updateItemInfoResult = this.updateItemInfo(itemStockEntity);
			
			// updateStockメソッドとupdateItemInfoメソッドの戻り値が1以上なら更新成功
			if(updateStockResult > 0 && updateItemInfoResult >0){
				
				// 更新成功の場合は成功メッセージを格納
				sysMsg = MessageEnum.SUCCESS_STOCK_DATA_BASE.getMessage();
			
			}else {
				// 更新失敗の場合はエラーメッセージを格納
				errMsg = MessageEnum.FAILED_STOCK_DATABASE.getMessage();
			}
			
			// 商品リストの取得メソッドを呼び出して情報を格納
			itemListForm = this.getItemListAll();
			
			// 在庫更新失敗＆商品リスト取得メソッド内でエラーが発生した場合、二つ繋げて改行した状態でerrMsgを上書き
			if(itemListForm.getErrMsg() != null && errMsg != null) {
				errMsg = errMsg + LINE_SEPARATOR + itemListForm.getErrMsg();
			
			// 更新成功＆商品リスト取得メソッド内でエラーが発生した場合、商品リスト取得メソッドのエラーメッセージのみ代入
			}else if(itemListForm.getErrMsg() != null) {
				errMsg = itemListForm.getErrMsg();
			}
			
			// エラーメッセージとシステムメッセージをそれぞれ設定
			itemListForm.setErrMsg(errMsg);
			itemListForm.setSysMsg(sysMsg);
			
		}catch(Exception e) {
			// 例外発生時は実行済みの更新処理をロールバックさせるため、Controllerに例外をスロー
			throw new RuntimeException(MessageEnum.SYSYEM_ERROR_GET_DB_DATA.getMessage());
		}
		
		return itemListForm;
	}
	
	
	/**
	 *  商品情報更新メソッド
	 * @param itemStockEntity
	 */
	// 成功判定必要なら戻り値をintにしてtry catch作成(在庫情報更新メソッド参考)
	public int updateItemInfo(ItemStockEntity itemStockEntity) throws Exception{
		
		// DBで更新されたレコード数格納用
		int result = 0;
		
		// 商品情報更新
		result = itemInfoMapper.updateItemInfo(itemStockEntity);
		
		// レコードの更新行数を返す(0なら失敗)
		return result;
	}
	
	
	/**
	 *  商品削除メソッド
	 * @param itemInfoForm
	 * @return 商品情報リストを返す。削除または商品リスト取得に失敗した場合はエラーメッセージを、成功すればシステムメッセージを返す
	 * @return いずれかの削除処理以降で例外が発生した場合、Controllerに例外をスローしてすべての削除処理をロールバックさせる
	 */
	@Transactional(rollbackFor = Exception.class)
	public ItemListForm deleteItem(ItemInfoForm itemInfoForm) {
		
		ItemListForm itemListForm = new ItemListForm();
		
		String errMsg = null;  // エラーメッセージ格納用
		String sysMsg = null;  // システムメッセージ格納用
		
		try {
			// 在庫と商品情報の削除処理(item_idと紐づけてるから念のため在庫削除が先)
			int deleteStockResult = itemInfoMapper.deleteStock(itemInfoForm.getItemId());
			
			// 商品情報削除
			// ここで例外が発生したら在庫削除処理をロールバックさせる
			int deleteInfoResult = itemInfoMapper.delete(itemInfoForm.getItemId());
			
			// 商品削除と在庫削除処理の戻り値が1以上なら削除成功メッセージをsysMsgに格納
			if(deleteInfoResult > 0 && deleteStockResult > 0) {
				sysMsg = MessageEnum.SUCCESS_DELETE_ITEM.getMessage();
			
			}else {
				// 削除失敗の時はエラーメッセージを格納
				errMsg = MessageEnum.FAILED_DELETE_ITEM.getMessage();
			}
				
			// 商品リストの取得メソッドを呼び出して読み込み用変数に格納
			itemListForm = this.getItemListAll();
				
			// 削除失敗＆商品リスト取得メソッド内でエラーが発生した場合、二つ繋げて改行した状態でerrMsgを上書き
			if(itemListForm.getErrMsg() != null && errMsg != null) {	
				errMsg = errMsg + LINE_SEPARATOR + itemListForm.getErrMsg();
			
			// 削除成功＆商品リスト取得メソッド内でエラーが発生した場合、商品リスト取得メソッドのエラーメッセージのみ代入
			}else if(itemListForm.getErrMsg() != null) {
				errMsg = itemListForm.getErrMsg();
			}
			
			// エラーメッセージとシステムメッセージを設定
			itemListForm.setErrMsg(errMsg);
			itemListForm.setSysMsg(sysMsg);
			
		}catch(Exception e) {
			// 例外発生時は実行済みの削除処理をロールバックさせるため、Controllerに例外をスロー
			throw new RuntimeException(MessageEnum.SYSYEM_ERROR_GET_DB_DATA.getMessage());
		}

		return itemListForm;
	}
	
	
}
