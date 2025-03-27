package com.freeks.training.stockSystem.converter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.freeks.training.stockSystem.entity.ItemInfoEntity;
import com.freeks.training.stockSystem.entity.ItemStockEntity;
import com.freeks.training.stockSystem.form.ItemInfoForm;
import com.freeks.training.stockSystem.form.ItemStockForm;
import com.freeks.training.stockSystem.util.CommonUtil;

@Component
public class ItemConverter {
	
	
	@Autowired
	private CommonUtil commonUtil;
	
	/**
	 *  商品情報リスト表示用コンバーター
	 * @param entityList
	 * @return entityListの中身をItemInfoForm型に変換した後、リストとして返す
	 */
	public List<ItemInfoForm> entityListToFormList(List<ItemInfoEntity> entityList){
		
		// entityListがnullなら新しいリストを作成して返す
		if(entityList == null) {
			return new ArrayList<>();
		}
		
		List<ItemInfoForm> itemInfoFormList = new ArrayList<>();
		
		// 引数entityListの中身を拡張for文で取り出す
		for(ItemInfoEntity entity : entityList) {
			
			// 新たにItemInfoFormのインスタンスを生成
			ItemInfoForm form = new ItemInfoForm();
			
			// 拡張for文で取り出す値をItemInfoForm内のそれぞれのフィールドに設定
			form.setItemId(entity.getItemId());			// 商品ID
			form.setItemName(entity.getItemName());		// 商品名
			form.setItemKubun(entity.getItemKubun());	// 商品区分
			form.setMaker(entity.getMaker());			// メーカー名
			form.setJancd(entity.getJancd());			// JANコード

			// 購入単価と販売単価はintからStringへ変換
			// 可読性のため、一度別のString型変数を作成して代入
			String purchaseUnitPrice = String.valueOf(entity.getPurchaseUnitPrice());
			form.setPurchaseUnitPrice(purchaseUnitPrice);		   // 購入単価
			
			String salesUnitPrice = String.valueOf(entity.getSalesUnitPrice());
			form.setSalesUnitPrice(salesUnitPrice);				   // 販売単価
			
			form.setStorageLocation(entity.getStorageLocation());  // 保管場所
			
			// commonUtilのconvertDateToStrDateメソッドを使ってDate型からString型へ変換
			String receiptDate = commonUtil.convertDateToStrDate(entity.getReceiptDate());
			form.setReceiptDate(receiptDate);					   // 入庫日
			
			form.setCreateUser(entity.getCreateUser());			   // 作成者
			
			// commonUtilのconvertDateToStrDateメソッドを使ってDate型からString型へ変換
			String createDate = commonUtil.convertDateToStrDate(entity.getCreateDate());
			form.setCreateDate(createDate);						   // 作成日時
			
			form.setUpdateUser(entity.getUpdateUser());			   // 更新者
			
			// commonUtilのconvertDateToStrDateメソッドを使ってDate型からString型へ変換
			String updateDate = commonUtil.convertDateToStrDate(entity.getUpdateDate());
			form.setUpdateDate(updateDate);						   // 更新日時
			
			
			// itemInfoFormのリストに追加
			itemInfoFormList.add(form);
		}
		
		return itemInfoFormList;
	}
	
	/**
	 *  商品情報登録用コンバーター
	 * @param form
	 * @param loginUser
	 * @return ItemInfoForm型をItemInfoEntity型にして返す
	 */
	
	public ItemInfoEntity insertFormToEntity(ItemInfoForm form, String loginUser){
		
		// ItemInfoEntityのインスタンス生成
		ItemInfoEntity insertEntity = new ItemInfoEntity();
		
		// formから取得した値をItemInfoEntity内のそれぞれのフィールドに設定
		
		insertEntity.setItemName(form.getItemName());		// 商品名
		insertEntity.setItemKubun(form.getItemKubun());		// 商品区分
		insertEntity.setMaker(form.getMaker());				// メーカー名
		insertEntity.setJancd(form.getJancd());				// JANコード

		// 購入単価と販売単価はStringからintへ変換
		// 可読性のため、一度別のint型変数を作成して代入
		int purchaseUnitPrice = Integer.parseInt(form.getPurchaseUnitPrice());
		insertEntity.setPurchaseUnitPrice(purchaseUnitPrice);		 // 購入単価
		
		int salesUnitPrice = Integer.parseInt(form.getSalesUnitPrice());
		insertEntity.setSalesUnitPrice(salesUnitPrice);				 // 販売単価
		
		insertEntity.setStorageLocation(form.getStorageLocation());  // 保管場所
		
		// 日付はすべて今日の日付
		insertEntity.setReceiptDate(commonUtil.nowDate());	// 入庫日
		insertEntity.setCreateUser(loginUser);  			// 作成者
		insertEntity.setCreateDate(commonUtil.nowDate());	// 作成日時
		insertEntity.setUpdateUser(loginUser);				// 更新者
		insertEntity.setUpdateDate(commonUtil.nowDate());	// 更新日時
		insertEntity.setLogicalDeleteFlg(false);			// 削除フラグ
		insertEntity.setVersion(0);							// バージョン情報
		
		return insertEntity;
		
	}
	
	
	/**
	 *  商品情報表示用コンバーター
	 * @param entity
	 * @return ItemInfoEntity型をItemInfoForm型にして返す
	 */
	public ItemInfoForm entityToForm(ItemInfoEntity entity) {
		
		ItemInfoForm form = new ItemInfoForm();
		
		
		// entityから取得した値をItemInfoForm内のそれぞれのフィールドに設定
		form.setItemId(entity.getItemId());			// 商品ID
		form.setItemName(entity.getItemName());		// 商品名
		form.setItemKubun(entity.getItemKubun());	// 商品区分
		form.setMaker(entity.getMaker());			// メーカー名
		form.setJancd(entity.getJancd());			// JANコード

		// 購入単価と販売単価はintからStringへ変換
		// 可読性のため、一度別のString型変数を作成して代入
		String purchaseUnitPrice = String.valueOf(entity.getPurchaseUnitPrice());
		form.setPurchaseUnitPrice(purchaseUnitPrice);		   // 購入単価
		
		String salesUnitPrice = String.valueOf(entity.getSalesUnitPrice());
		form.setSalesUnitPrice(salesUnitPrice);				   // 販売単価
		
		form.setStorageLocation(entity.getStorageLocation());  // 保管場所
		
		// commonUtilのconvertDateToStrDateメソッドを使ってDate型からString型へ変換
		String receiptDate = commonUtil.convertDateToStrDate(entity.getReceiptDate());
		form.setReceiptDate(receiptDate);			 // 入庫日
		
		form.setCreateUser(entity.getCreateUser());  // 作成者
		
		// commonUtilのconvertDateToStrDateメソッドを使ってDate型からString型へ変換
		String createDate = commonUtil.convertDateToStrDate(entity.getCreateDate());
		form.setCreateDate(createDate);				 // 作成日時
		
		form.setUpdateUser(entity.getUpdateUser());  // 更新者
		
		// commonUtilのconvertDateToStrDateメソッドを使ってDate型からString型へ変換
		String updateDate = commonUtil.convertDateToStrDate(entity.getUpdateDate());
		form.setUpdateDate(updateDate);				 // 更新日時
		
		return form;
	}
	
	
	/** 在庫情報表示用コンバーター
	 * 
	 * @param entity
	 * @return ItemStockEntity型をItemStockForm型にしてから返す
	 */
	public ItemStockForm stockEentityToForm(ItemStockEntity entity) {
			
			ItemStockForm itemStockForm = new ItemStockForm();
			
			// entityから取得した値をそれぞれの変数に設定
			itemStockForm.setStorageStockId(entity.getStorageStockId());	// 在庫ID
			itemStockForm.setItemId(entity.getItemId());					// 商品ID
			
			// 在庫数、入庫数、出庫数はintからStringへ変換
			// 可読性のため、一度別のString型変数を作成して代入
			String stockQuantity = String.valueOf(entity.getStockQuantity());
			itemStockForm.setStockQuantity(stockQuantity);					// 在庫数
			
			String receiveQuantity = String.valueOf(entity.getReceiveQuantity());
			itemStockForm.setReceiveQuantity(receiveQuantity);				// 入庫数
			
			String dispatchQuantity = String.valueOf(entity.getDispatchQuantity());
			itemStockForm.setDispatchQuantity(dispatchQuantity);			// 出庫数
			
			itemStockForm.setStorageLocation(entity.getStorageLocation());  // 保管場所
			
			itemStockForm.setCreateUser(entity.getCreateUser());			// 作成者
			
			// commonUtilのconvertDateToStrDateメソッドを使ってDate型からString型へ変換
			String createDate = commonUtil.convertDateToStrDate(entity.getCreateDate());
			itemStockForm.setCreateDate(createDate);						// 作成日時
			
			itemStockForm.setUpdateUser(entity.getUpdateUser());			// 更新者
			
			// commonUtilのconvertDateToStrDateメソッドを使ってDate型からString型へ変換
			String updateDate = commonUtil.convertDateToStrDate(entity.getUpdateDate());
			itemStockForm.setUpdateDate(updateDate);						// 更新日時
			
			return itemStockForm;
		}
	
	
	/**
	 * 在庫情報更新用コンバーター
	 * @param form ID以外はString型で送られてくるため、在庫数などはint型に変換する
	 * @param beforForm 更新前の商品情報 扱いはformと同じ
	 * @param loginUser ログインユーザー = 更新者 
	 * @return itemUpdateForm 更新された商品情報を返す
	 */

	public ItemStockEntity formToEntity(ItemStockForm form, ItemStockForm beforeForm, String loginUser) {
		
		ItemStockEntity updateStcokEntity = new ItemStockEntity();
		
		/* 受け取った在庫数、入庫数、出庫数、保管場所、更新者、更新日時を、それぞれの変数に設定
		* 最新在庫数 = 更新前の在庫数 + (受け取った入庫数 - 受け取った在庫数)
		* 最新入庫数 = 更新前入庫数 + 受け取った入庫数
		* 最新出庫数 = 更新前出庫数 + 受け取った出庫数
		*/
		
		// ID以外の数値系は受け取る時はString型だが、Entity側ではint型なので変換
		// 更新前在庫数
		int beforeStockQuantity = Integer.parseInt(beforeForm.getStockQuantity());
		
		// 更新前入庫数
		int beforeReceiveQuantity = Integer.parseInt(beforeForm.getReceiveQuantity());
		
		// 更新前出庫数
		int beforeDispatchQuantity = Integer.parseInt(beforeForm.getDispatchQuantity());
		
		// 受け取った入庫数
		int updReceiveQuantity = Integer.parseInt(form.getReceiveQuantity());
		
		// 受け取った出庫数
		int updDispatchQuantity = Integer.parseInt(form.getDispatchQuantity());
		
		
		//// Entityにセット ////
		// 商品ID(キーとして必要)
		updateStcokEntity.setItemId(form.getItemId());
		
		// 最新入庫数(= 更新前入庫数 + 受け取った入庫数)
		updateStcokEntity.setReceiveQuantity(beforeReceiveQuantity + updReceiveQuantity); // ホントはさらに - updDispatchQuantityしないとトータル入庫数になって増える一方
		
		// 最新出庫数(= 更新前出庫数 + 受け取った出庫数)
		updateStcokEntity.setDispatchQuantity(beforeDispatchQuantity + updDispatchQuantity);  // ここも増える一方
		
		// 最新在庫数(= 更新前在庫数 + (受け取った入庫数 - 受け取った出庫数))
		updateStcokEntity.setStockQuantity(beforeStockQuantity + (updReceiveQuantity - updDispatchQuantity));
		
		// 保管場所
		updateStcokEntity.setStorageLocation(form.getStorageLocation());
		
		// 更新者
		updateStcokEntity.setUpdateUser(loginUser);
		 
		// 更新日時(今日の日付/Data型)
		updateStcokEntity.setUpdateDate(commonUtil.nowDate());
		
		return updateStcokEntity;
		
	}
	
	/**
	 *  在庫情報登録メソッド
	 * @param requestForm 在庫数、入庫数、出庫数、日付、削除フラグ、バージョンはMapper.stockInsertのxmlにベタ書き
	 * @param loginUser 定数"freeks"
	 * @return 
	 */
	public ItemStockEntity createStockEntity(ItemInfoEntity itemInfo, String loginUser){
		
		ItemStockEntity stockEntity = new ItemStockEntity();

		Date today = commonUtil.nowDate();
		
		stockEntity.setItemId(itemInfo.getItemId());
		stockEntity.setStockQuantity(0);
		stockEntity.setReceiveQuantity(0);
		stockEntity.setDispatchQuantity(0);
		stockEntity.setStorageLocation(itemInfo.getStorageLocation());
		stockEntity.setCreateDate(today);
		stockEntity.setCreateUser(loginUser);
		stockEntity.setUpdateDate(today);
		stockEntity.setUpdateUser(loginUser);
		stockEntity.setLogicalDeleteFlg(false);
		stockEntity.setVersion(0);
		
		return stockEntity;
	}
	
	
}
