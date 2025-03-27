package com.freeks.training.stockSystem.service;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.freeks.training.stockSystem.converter.ItemConverter;
import com.freeks.training.stockSystem.entity.ItemInfoEntity;
import com.freeks.training.stockSystem.entity.ItemStockEntity;
import com.freeks.training.stockSystem.form.ItemInfoForm;
import com.freeks.training.stockSystem.form.ItemListForm;
import com.freeks.training.stockSystem.mapper.ItemInfoMapper;
import com.freeks.training.stockSystem.util.CommonUtil;

class ItemServiceTest {

	//日付フォーマット
	private SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");

	// テストデータ
	private int itemId = 9990;
	private int itemId2 = 9991;
	private String itemName = "testName1";
	private String itemName2 = "testName2";
	private String itemKubun = "test1";
	private String itemKubun2 = "test2";
	private String maker = "テストメーカー1";
	private String maker2 = "テストメーカー2";
	private String jancd = "test1234";
	private String jancd2 = "test5678";
	private int purchaseUnitPrice = 1234;
	private int purchaseUnitPrice2 = 5678;
	private int salesUnitPrice = 5678;
	private int salesUnitPrice2 = 1234;
	private String storageLocation = "テスト保管場所";
	private String storageLocation2 = "テスト保管場所2";
	private Date receiptDate = nowDate();
	private String createUser = "createTest";
	private Date createDate = nowDate();
	private String updateUser = "updateTest";
	private Date updateDate = nowDate();
	private boolean logicalDeleteFlg = false;
	private int version = 999;
	
	// insertの結果
	private int resultOfInsertItem = 0;
	private int resultOfInsertStock = 0;


	@Mock
	ItemInfoMapper itemInfoMapper;

	@Mock
	ItemConverter itemConverter;

	@Mock
	CommonUtil commonUtil;
	
	@Spy
	@InjectMocks
	ItemService itemService;

	/**
	 * @Mockを付与したメンバ変数にモックオブジェクトを注入
	 * @Testを付与したメソッドの実行前に都度実行される
	 */
	@BeforeEach
	public void initMocks(){
		MockitoAnnotations.openMocks(this);
	}

	/**
	 * getItemListAll
	 * 項番1
	 * 正常系
	 * 
	 */
	@Test
	void getItemListAll_1() {

		// Mock戻り値の設定
		List<ItemInfoEntity> entList = createItemInfoEntityList();
		List<ItemInfoForm> formList = entityListToFormList(entList);
		
		// Mockの設定
		when(itemInfoMapper.getFindAll()).thenReturn(entList);
		when(itemConverter.entityListToFormList(entList)).thenReturn(formList);
		
		// 対象メソッド実行
		ItemListForm result = itemService.getItemListAll();
		
		// 結果確認
		assertThat(result.getItemInfoFormList().size(), is(2));
		assertThat(result.getErrMsg(), is(nullValue()));
		assertThat(result.getItemInfoFormList().get(0).getItemId(), is(itemId));
		assertThat(result.getItemInfoFormList().get(0).getItemName(), is(itemName));
		assertThat(result.getItemInfoFormList().get(0).getItemKubun(), is(itemKubun));
		assertThat(result.getItemInfoFormList().get(0).getMaker(), is(maker));
		assertThat(result.getItemInfoFormList().get(0).getJancd(), is(jancd));
		assertThat(result.getItemInfoFormList().get(0).getPurchaseUnitPrice(), is(String.valueOf(purchaseUnitPrice)));
		assertThat(result.getItemInfoFormList().get(0).getSalesUnitPrice(), is(String.valueOf(salesUnitPrice)));
		assertThat(result.getItemInfoFormList().get(0).getStorageLocation(), is(storageLocation));
		assertThat(result.getItemInfoFormList().get(1).getItemId(), is(itemId2));
		assertThat(result.getItemInfoFormList().get(1).getItemName(), is(itemName2));
		assertThat(result.getItemInfoFormList().get(1).getItemKubun(), is(itemKubun2));
		assertThat(result.getItemInfoFormList().get(1).getMaker(), is(maker2));
		assertThat(result.getItemInfoFormList().get(1).getJancd(), is(jancd2));
		assertThat(result.getItemInfoFormList().get(1).getPurchaseUnitPrice(), is(String.valueOf(purchaseUnitPrice2)));
		assertThat(result.getItemInfoFormList().get(1).getSalesUnitPrice(), is(String.valueOf(salesUnitPrice2)));
		assertThat(result.getItemInfoFormList().get(1).getStorageLocation(), is(storageLocation2));

	
	}
	
	/**
	 * getItemListAll
	 * 項番2
	 * List<ItemInfoEntity>のSizeが0
	 * 正常系
	 * 
	 */
	@Test
	void getItemListAll_2() {

		// Mock戻り値の設定
		List<ItemInfoEntity> entList = new ArrayList<>();
		
		// Mockの設定
		when(itemInfoMapper.getFindAll()).thenReturn(entList);
		
		// 対象メソッド実行
		ItemListForm result = itemService.getItemListAll();
		
		// 結果確認
		assertThat(result.getItemInfoFormList(), is(nullValue()));
		assertThat(result.getErrMsg(), is("システムエラー：商品情報が見つかりません"));
	
	}
	
	/**
	 * getItemListAll
	 * 項番3
	 * itemInfoMapper.getFindAll()で予期せぬエラー
	 * 異常系
	 * 
	 */
	@Test
	void getItemListAll_3() {
		
		// Mockの設定
		when(itemInfoMapper.getFindAll()).thenThrow(new RuntimeException());
		
		// 対象メソッド実行
		ItemListForm result = itemService.getItemListAll();
		System.out.println(result.getErrMsg());
		// 結果確認
		assertThat(result.getItemInfoFormList(), is(nullValue()));
		assertThat(result.getErrMsg(), is("システムエラー：DB接続に失敗しました"));
	
	}
	
	
	/**
	 *  checkDuplicateItemName
	 *  項目4
	 *  正常系
	 */
	@Test
	void checkDuplicateItemName_1() {
		
		// チェックする商品名を設定
		String requestItemName = itemName;
		
		// チェック結果格納用変数
		String result = null;
		
		// Mockの設定
		when(itemInfoMapper.findByItemName(requestItemName)).thenReturn(null);
		
		// 対象メソッド実行
		result = itemService.checkDuplicateItemName(requestItemName);
		System.out.println(result);
		
		// 結果確認
		assertThat(result, is(nullValue()));
		
	}
	

	/**
	 *  checkDuplicateItemName
	 *  項目5
	 *  checkNameDuplicateがnullではない
	 *  正常系
	 */
	@Test
	void checkDuplicateItemName_2() {
		
		// チェックする商品名を設定
		String requestItemName = itemName;
		ItemInfoEntity entName = new ItemInfoEntity();
		entName.setItemName(itemName);
		
		// チェック結果格納用変数
		String result = null;
		
		// Mockの設定
		when(itemInfoMapper.findByItemName(requestItemName)).thenReturn(entName);
		
		// 対象メソッド実行
		result = itemService.checkDuplicateItemName(requestItemName);		
		System.out.println(result);
		
		// 結果確認
		assertThat(result, is("商品名称が重複しています"));
		
	}
	
	
	/**
	 *  checkDuplicateItemName
	 *  項目6
	 *  itemInfoMapper.findByItemName(requestItemName)で予期せぬエラー
	 *  異常系
	 */
	@Test
	void checkDuplicateItemName_3() {
		
		// チェックする商品名を設定
		String requestItemName = itemName;
		
		// チェック結果格納用変数
		String result = null;
		
		// Mockの設定
		when(itemInfoMapper.findByItemName(requestItemName)).thenThrow(new RuntimeException());
		
		// 対象メソッド実行
		result = itemService.checkDuplicateItemName(requestItemName);
		System.out.println(result);
		
		// 結果確認
		assertThat(result, is("システムエラー：DB接続に失敗しました"));
		
	}
	
	
	/**
	 *  createItem
	 *  項目7
	 *  正常系
	 */
	@Test
	void createItem_1() throws Exception {
		// 定数用の仮変数
		String loginUser = createUser;
		
		// Mock戻り値の設定
		ItemInfoForm requestForm = createItemInfoForm();
		ItemInfoEntity itemEntity = formToEntity(requestForm);
		ItemInfoEntity findByNameEntity = findByNameEntity(requestForm.getItemName());
		ItemStockEntity stockEntity = createStockEntity(findByNameEntity, loginUser);
		List<ItemInfoEntity> entList = createItemInfoEntityList();
		List<ItemInfoForm> formList = entityListToFormList(entList);
		
		// insertの結果
		resultOfInsertItem = 1;
		resultOfInsertStock = 1;
		
		// Mockの設定
		when(itemConverter.insertFormToEntity(requestForm, loginUser)).thenReturn(itemEntity);
		when(itemInfoMapper.insert(itemEntity)).thenReturn(resultOfInsertItem);
		when(itemInfoMapper.findByItemName(requestForm.getItemName())).thenReturn(findByNameEntity);
		when(itemConverter.createStockEntity(findByNameEntity, loginUser)).thenReturn(stockEntity);
		when(itemInfoMapper.stockInsert(stockEntity)).thenReturn(resultOfInsertStock);
		when(itemInfoMapper.getFindAll()).thenReturn(entList);
		when(itemConverter.entityListToFormList(entList)).thenReturn(formList);
		
		// 対象メソッド実行
		ItemListForm result = itemService.createItem(requestForm, loginUser);
		
		System.out.println("[sysMsgの結果]");
		System.out.println(result.getSysMsg());
		System.out.println("[errMsgの結果]");
		System.out.println(result.getErrMsg());
		System.out.println("[createDateの結果]");
		System.out.println(stockEntity.getCreateDate());
		System.out.println("[updateDateの結果]");
		System.out.println(stockEntity.getUpdateDate());
		
		// メソッド呼び出し確認
		verify(itemService, times(1)).getItemListAll();
		
		// 結果確認
		assertThat(result.getSysMsg(), is("商品情報を更新しました"));
		assertThat(result.getErrMsg(), is(nullValue()));
		
	}
	
	

	/**
	 *  createItem
	 *  項目8
	 *  itemConverter.insertFormToEntity(requestForm, loginUser)で予期せぬエラー
	 *  ※商品と在庫登録は失敗、実行された登録処理はロールバックして一覧画面でエラーメッセージ表示
	 *  異常系
	 */
	@Test
	void createItem_2() throws Exception {
		// 定数用の仮変数
		String loginUser = createUser;
		
		// Mock戻り値の設定
		ItemInfoForm requestForm = createItemInfoForm();
		ItemInfoEntity itemEntity = formToEntity(requestForm);
		ItemInfoEntity findByNameEntity = findByNameEntity(requestForm.getItemName());
		ItemStockEntity stockEntity = createStockEntity(findByNameEntity, loginUser);
		List<ItemInfoEntity> entList = createItemInfoEntityList();
		List<ItemInfoForm> formList = entityListToFormList(entList);	
		
		// insertの結果
		resultOfInsertItem = 0;
		resultOfInsertStock = 0;
		
		// Mockの設定
		
		when(itemConverter.insertFormToEntity(requestForm, loginUser)).thenThrow(new RuntimeException());
		when(itemInfoMapper.insert(itemEntity)).thenReturn(resultOfInsertItem);
		when(itemInfoMapper.findByItemName(requestForm.getItemName())).thenReturn(findByNameEntity);
		when(itemConverter.createStockEntity(findByNameEntity, loginUser)).thenReturn(stockEntity);
		when(itemInfoMapper.stockInsert(stockEntity)).thenReturn(resultOfInsertStock);
		when(itemInfoMapper.getFindAll()).thenReturn(entList);
		when(itemConverter.entityListToFormList(entList)).thenReturn(formList);
		
		// 対象メソッド実行
		RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
			itemService.createItem(requestForm, loginUser);
		});
		
		System.out.println("[throwの結果]");
		System.out.println(thrown.getMessage());
		
		// itemInfoMapper.insert(itemEntity)が1回呼び出されたことを確認
		verify(itemInfoMapper, times(0)).insert(itemEntity);
		
		// itemInfoMapper.stockInsert(stockEntity)が呼び出されていないことを確認
		verify(itemInfoMapper,times(0)).stockInsert(stockEntity);
		
		// this.getItemListAll()が呼び出されていないことを確認
		verify(itemService, times(0)).getItemListAll();
		
		// 結果確認
		assertThat(thrown.getMessage(), is("システムエラー：商品、もしくは在庫の登録中にエラーが発生しました"));
		
    }
	
	/**
	 *  createItem
	 *  項目9
	 *  itemInfoMapper.insert(itemInfoEntity)で予期せぬエラー
	 *  ※商品と在庫登録は失敗、実行された登録処理はロールバックして一覧画面でエラーメッセージ表示
	 *  異常系
	 */
	@Test
	void createItem_3() throws Exception {
		// 定数用の仮変数
		String loginUser = createUser;
		
		// Mock戻り値の設定
		ItemInfoForm requestForm = createItemInfoForm();
		ItemInfoEntity itemEntity = formToEntity(requestForm);
		ItemInfoEntity findByNameEntity = findByNameEntity(requestForm.getItemName());
		ItemStockEntity stockEntity = createStockEntity(findByNameEntity, loginUser);
		List<ItemInfoEntity> entList = createItemInfoEntityList();
		List<ItemInfoForm> formList = entityListToFormList(entList);

		// insertの結果
		resultOfInsertItem = 1;
		resultOfInsertStock = 1;
		
		// Mockの設定
		
		when(itemConverter.insertFormToEntity(requestForm, loginUser)).thenReturn(itemEntity);
		when(itemInfoMapper.insert(itemEntity)).thenThrow(new RuntimeException());
		when(itemInfoMapper.findByItemName(requestForm.getItemName())).thenReturn(findByNameEntity);
		when(itemConverter.createStockEntity(findByNameEntity, loginUser)).thenReturn(stockEntity);
		when(itemInfoMapper.stockInsert(stockEntity)).thenReturn(resultOfInsertStock);
		when(itemInfoMapper.getFindAll()).thenReturn(entList);
		when(itemConverter.entityListToFormList(entList)).thenReturn(formList);
		
		// 対象メソッド実行
		RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
			itemService.createItem(requestForm, loginUser);
		});
		
		System.out.println("[throwの結果]");
		System.out.println(thrown.getMessage());
		
		// itemInfoMapper.insert(itemEntity)が1回呼び出されたことを確認
		verify(itemInfoMapper, times(1)).insert(itemEntity);
		
		// itemInfoMapper.stockInsert(stockEntity)が呼び出されていないことを確認
		verify(itemInfoMapper,times(0)).stockInsert(stockEntity);
		
		// this.getItemListAll()が呼び出されていないことを確認
		verify(itemService, times(0)).getItemListAll();
		
		// 結果確認
		assertThat(thrown.getMessage(), is("システムエラー：商品、もしくは在庫の登録中にエラーが発生しました"));
		
	}


	/**
	 *  createItem
	 *  項目10
	 *  itemInfoMapper.findByItemName(requestForm.getItemName())で予期せぬエラー
	 *  ※商品と在庫登録は失敗、実行された登録処理はロールバックして一覧画面でエラーメッセージ表示
	 *  異常系
	 */
	@Test
	void createItem_4() throws Exception {
		// 定数用の仮変数
		String loginUser = createUser;
		
		// Mock戻り値の設定
		ItemInfoForm requestForm = createItemInfoForm();
		ItemInfoEntity itemEntity = formToEntity(requestForm);
		ItemInfoEntity findByNameEntity = findByNameEntity(requestForm.getItemName());
		ItemStockEntity stockEntity = createStockEntity(findByNameEntity, loginUser);
		List<ItemInfoEntity> entList = createItemInfoEntityList();
		List<ItemInfoForm> formList = entityListToFormList(entList);

		// insertの結果
		resultOfInsertItem = 0;
		resultOfInsertStock = 0;
		
		// Mockの設定
		when(itemConverter.insertFormToEntity(requestForm, loginUser)).thenReturn(itemEntity);
		when(itemInfoMapper.insert(itemEntity)).thenReturn(resultOfInsertItem);
		when(itemInfoMapper.findByItemName(requestForm.getItemName())).thenThrow(new RuntimeException());
		when(itemConverter.createStockEntity(findByNameEntity, loginUser)).thenReturn(stockEntity);
		when(itemInfoMapper.stockInsert(stockEntity)).thenReturn(resultOfInsertStock);
		when(itemInfoMapper.getFindAll()).thenReturn(entList);
		when(itemConverter.entityListToFormList(entList)).thenReturn(formList);
		
		// 対象メソッド実行
		RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
			itemService.createItem(requestForm, loginUser);
		});
		
		System.out.println("[throwの結果]");
		System.out.println(thrown.getMessage());
		
		// itemInfoMapper.insert(itemEntity)が1回呼び出されたことを確認
		verify(itemInfoMapper, times(1)).insert(itemEntity);
		
		// itemInfoMapper.stockInsert(stockEntity)が呼び出されていないことを確認
		verify(itemInfoMapper,times(0)).stockInsert(stockEntity);
		
		// this.getItemListAll()が呼び出されていないことを確認
		verify(itemService, times(0)).getItemListAll();
		
		// 結果確認
		assertThat(thrown.getMessage(), is("システムエラー：商品、もしくは在庫の登録中にエラーが発生しました"));
		
	    }

	/**
	 *  createItem
	 *  項目11
	 *  itemConverter.createStockEntity(itemInfo,loginUser)で予期せぬエラー
	 *  ※商品と在庫登録は失敗、実行された登録処理はロールバックして一覧画面でエラーメッセージ表示
	 *  異常系
	 */
	@Test
	void createItem_5() throws Exception {
		// 定数用の仮変数
		String loginUser = createUser;
		
		// Mock戻り値の設定
		ItemInfoForm requestForm = createItemInfoForm();
		ItemInfoEntity itemEntity = formToEntity(requestForm);
		ItemInfoEntity findByNameEntity = findByNameEntity(requestForm.getItemName());
		ItemStockEntity stockEntity = createStockEntity(findByNameEntity, loginUser);
		List<ItemInfoEntity> entList = createItemInfoEntityList();
		List<ItemInfoForm> formList = entityListToFormList(entList);	
		
		// insertの結果
		resultOfInsertItem = 0;
		resultOfInsertStock = 0;
		
		// Mockの設定
		
		when(itemConverter.insertFormToEntity(requestForm, loginUser)).thenReturn(itemEntity);
		when(itemInfoMapper.insert(itemEntity)).thenReturn(resultOfInsertItem);
		when(itemInfoMapper.findByItemName(requestForm.getItemName())).thenReturn(findByNameEntity);
		when(itemConverter.createStockEntity(findByNameEntity, loginUser)).thenThrow(new RuntimeException() {});
		when(itemInfoMapper.stockInsert(stockEntity)).thenReturn(resultOfInsertStock);
		when(itemInfoMapper.getFindAll()).thenReturn(entList);
		when(itemConverter.entityListToFormList(entList)).thenReturn(formList);
		
		// 対象メソッド実行
		RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
			itemService.createItem(requestForm, loginUser);
		});
		
		System.out.println("[throwの結果]");
		System.out.println(thrown.getMessage());
		
		// itemInfoMapper.insert(itemEntity)が1回呼び出されたことを確認
		verify(itemInfoMapper, times(1)).insert(itemEntity);
		
		// itemInfoMapper.stockInsert(stockEntity)が呼び出されていないことを確認
		verify(itemInfoMapper,times(0)).stockInsert(stockEntity);
		
		// this.getItemListAll()が呼び出されていないことを確認
		verify(itemService, times(0)).getItemListAll();
		
		// 結果確認
		assertThat(thrown.getMessage(), is("システムエラー：商品、もしくは在庫の登録中にエラーが発生しました"));
	}


	/**
	 *  createItem
	 *  項目12
	 *  itemInfoMapper.stockInsert(stockEntity)で予期せぬエラー
	 *  ※商品と在庫登録は失敗、実行された登録処理はロールバックして一覧画面でエラーメッセージ表示
	 *  異常系
	 */
	@Test
	void createItem_6() throws Exception {
		// 定数用の仮変数
		String loginUser = createUser;
		
		// Mock戻り値の設定
		ItemInfoForm requestForm = createItemInfoForm();
		ItemInfoEntity itemEntity = formToEntity(requestForm);
		ItemInfoEntity findByNameEntity = findByNameEntity(requestForm.getItemName());
		ItemStockEntity stockEntity = createStockEntity(findByNameEntity, loginUser);
		List<ItemInfoEntity> entList = createItemInfoEntityList();
		List<ItemInfoForm> formList = entityListToFormList(entList);
		
		// insertの結果
		resultOfInsertItem = 1;
		resultOfInsertStock = 0;
		
		// Mockの設定
		when(itemConverter.insertFormToEntity(requestForm, loginUser)).thenReturn(itemEntity);
		when(itemInfoMapper.insert(itemEntity)).thenReturn(resultOfInsertItem);
		when(itemInfoMapper.findByItemName(requestForm.getItemName())).thenReturn(findByNameEntity);
		when(itemConverter.createStockEntity(findByNameEntity, loginUser)).thenReturn(stockEntity);
		when(itemInfoMapper.stockInsert(stockEntity)).thenThrow(new RuntimeException() {});
		when(itemInfoMapper.getFindAll()).thenReturn(entList);
		when(itemConverter.entityListToFormList(entList)).thenReturn(formList);
		
		// 対象メソッド実行
		RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
			itemService.createItem(requestForm, loginUser);
		});
		
		System.out.println("[throwの結果]");
		System.out.println(thrown.getMessage());
		
		// itemInfoMapper.insert(itemEntity)が1回呼び出されたことを確認
		verify(itemInfoMapper, times(1)).insert(itemEntity);
		
		// itemInfoMapper.stockInsert(stockEntity)が呼び出されていないことを確認
		verify(itemInfoMapper,times(1)).stockInsert(stockEntity);
		
		// this.getItemListAll()が呼び出されていないことを確認
		verify(itemService, times(0)).getItemListAll();
		
		// 結果確認
		assertThat(thrown.getMessage(), is("システムエラー：商品、もしくは在庫の登録中にエラーが発生しました"));
		
    }
	
	/**
	 *  createItem
	 *  項目13
	 *  itemInfoMapper.insert(itemInfoEntity)とitemInfoMapper.stockInsert(stockEntity)のresultが1
	 *  this.getItemListAll()でitemListFormのerrMsgがnullでない場合(リストが空)
	 *  ※商品と在庫登録は成功、リスト取得のみ失敗
	 *  異常系
	 */
	@Test
	void createItem_7() throws Exception {
		// 定数用の仮変数
		String loginUser = createUser;
		
		// Mock戻り値の設定
		ItemInfoForm requestForm = createItemInfoForm();
		ItemInfoEntity itemEntity = formToEntity(requestForm);
		ItemInfoEntity findByNameEntity = findByNameEntity(requestForm.getItemName());
		ItemStockEntity stockEntity = createStockEntity(findByNameEntity, loginUser);
		List<ItemInfoEntity> entList = new ArrayList<>();
		List<ItemInfoForm> formList = entityListToFormList(entList);
		
		// insertの結果
		resultOfInsertItem = 1;
		resultOfInsertStock = 1;
		
		// Mockの設定
		
		when(itemConverter.insertFormToEntity(requestForm, loginUser)).thenReturn(itemEntity);
		when(itemInfoMapper.insert(itemEntity)).thenReturn(resultOfInsertItem);
		when(itemInfoMapper.findByItemName(requestForm.getItemName())).thenReturn(findByNameEntity);
		when(itemConverter.createStockEntity(findByNameEntity, loginUser)).thenReturn(stockEntity);
		when(itemInfoMapper.stockInsert(stockEntity)).thenReturn(resultOfInsertStock);
		when(itemInfoMapper.getFindAll()).thenReturn(entList);
		when(itemConverter.entityListToFormList(entList)).thenReturn(formList);
		
		// 対象メソッド実行
		ItemListForm result = itemService.createItem(requestForm, loginUser);
		
		System.out.println("[sysMsgの結果]");
		System.out.println(result.getSysMsg());
		System.out.println("[errMsgの結果]");
		System.out.println(result.getErrMsg());
		
		
		// this.getItemListAll()が呼び出されたか確認
		verify(itemService, times(1)).getItemListAll();
		
		// 結果確認
		assertThat(result.getSysMsg(), is("商品情報を更新しました"));
		assertThat(result.getErrMsg(), is("システムエラー：商品情報が見つかりません"));
		
	}

	
	/**
	 *  createItem
	 *  項目14
	 *  itemInfoMapper.insert(itemInfoEntity)とitemInfoMapper.stockInsert(stockEntity)のresultが1
	 *  this.getItemListAll()でitemListFormのerrMsgがnullでない場合(予期せぬエラー発生)
	 *  異常系
	 */
	@Test
	void createItem_8(){
		// 定数用の仮変数
		String loginUser = createUser;
		
		// Mock戻り値の設定
		ItemInfoForm requestForm = createItemInfoForm();
		ItemInfoEntity itemEntity = formToEntity(requestForm);
		ItemInfoEntity findByNameEntity = findByNameEntity(requestForm.getItemName());
		ItemStockEntity stockEntity = createStockEntity(findByNameEntity, loginUser);
		List<ItemInfoEntity> entList = createItemInfoEntityList();
		List<ItemInfoForm> formList = entityListToFormList(entList);
		
		// insertの結果
		resultOfInsertItem = 1;
		resultOfInsertStock = 1;
		
		// Mockの設定
		when(itemConverter.insertFormToEntity(requestForm, loginUser)).thenReturn(itemEntity);
		when(itemInfoMapper.insert(itemEntity)).thenReturn(resultOfInsertItem);
		when(itemInfoMapper.findByItemName(requestForm.getItemName())).thenReturn(findByNameEntity);
		when(itemConverter.createStockEntity(findByNameEntity, loginUser)).thenReturn(stockEntity);
		when(itemInfoMapper.stockInsert(stockEntity)).thenReturn(resultOfInsertStock);
		when(itemInfoMapper.getFindAll()).thenThrow(new RuntimeException());
		when(itemConverter.entityListToFormList(entList)).thenReturn(formList);
		
		// 対象メソッド実行
		ItemListForm result = itemService.createItem(requestForm, loginUser);
		
		System.out.println("[sysMsgの結果]");
		System.out.println(result.getSysMsg());
		System.out.println("[errMsgの結果]");
		System.out.println(result.getErrMsg());
		
		// this.getItemListAll()が呼び出されたか確認
		verify(itemService, times(1)).getItemListAll();
		
		// 結果確認
		assertThat(result.getSysMsg(), is("商品情報を更新しました"));
		assertThat(result.getErrMsg(), is("システムエラー：DB接続に失敗しました"));
		
	}

	
	/**
	 *  createItem
	 *  項目15
	 *  itemInfoMapper.insert(itemInfoEntity)とitemInfoMapper.stockInsert(stockEntity)のresultが0
	 *  this.getItemListAll()でitemListFormのerrMsgがnull
	 *  異常系
	 */
	@Test
	void createItem_9(){
		// 定数用の仮変数
		String loginUser = createUser;
		
		// Mock戻り値の設定
		ItemInfoForm requestForm = createItemInfoForm();
		ItemInfoEntity itemEntity = formToEntity(requestForm);
		ItemInfoEntity findByNameEntity = findByNameEntity(requestForm.getItemName());
		ItemStockEntity stockEntity = createStockEntity(findByNameEntity, loginUser);
		List<ItemInfoEntity> entList = createItemInfoEntityList();
		List<ItemInfoForm> formList = entityListToFormList(entList);
		
		// insertの結果
		resultOfInsertItem = 0;
		resultOfInsertStock = 0;
		
		// Mockの設定
		
		when(itemConverter.insertFormToEntity(requestForm, loginUser)).thenReturn(itemEntity);
		when(itemInfoMapper.insert(itemEntity)).thenReturn(resultOfInsertItem);
		when(itemInfoMapper.findByItemName(requestForm.getItemName())).thenReturn(findByNameEntity);
		when(itemConverter.createStockEntity(findByNameEntity, loginUser)).thenReturn(stockEntity);
		when(itemInfoMapper.stockInsert(stockEntity)).thenReturn(resultOfInsertStock);
		when(itemInfoMapper.getFindAll()).thenReturn(entList);
		when(itemConverter.entityListToFormList(entList)).thenReturn(formList);
		
		// 対象メソッド実行
		ItemListForm result = itemService.createItem(requestForm, loginUser);
		
		System.out.println("[sysMsgの結果]");
		System.out.println(result.getSysMsg());
		System.out.println("[errMsgの結果]");
		System.out.println(result.getErrMsg());

		// this.getItemListAll()が呼び出されたか確認
		verify(itemService, times(1)).getItemListAll();
		
		// 結果確認
		assertThat(result.getSysMsg(), is(nullValue()));
		assertThat(result.getErrMsg(), is("商品情報の更新に失敗しました"));
		
	}

	
	/**
	 *  createItem
	 *  項目16
	 *  itemInfoMapper.insert(itemInfoEntity)とitemInfoMapper.stockInsert(stockEntity)のresultが0
	 *  this.getItemListAll()でitemListFormのerrMsgがnullでない(リストが空)
	 *  異常系
	 */
	@Test
	void createItem_10(){
		// 定数用の仮変数
		String loginUser = createUser;
		
		// Mock戻り値の設定
		ItemInfoForm requestForm = createItemInfoForm();
		ItemInfoEntity itemEntity = formToEntity(requestForm);
		ItemInfoEntity findByNameEntity = findByNameEntity(requestForm.getItemName());
		ItemStockEntity stockEntity = createStockEntity(findByNameEntity, loginUser);
		List<ItemInfoEntity> entList = new ArrayList<>();
		List<ItemInfoForm> formList = entityListToFormList(entList);
		
		// insertの結果
		resultOfInsertItem = 0;
		resultOfInsertStock = 0;
		
		// Mockの設定
		
		when(itemConverter.insertFormToEntity(requestForm, loginUser)).thenReturn(itemEntity);
		when(itemInfoMapper.insert(itemEntity)).thenReturn(resultOfInsertItem);
		when(itemInfoMapper.findByItemName(requestForm.getItemName())).thenReturn(findByNameEntity);
		when(itemConverter.createStockEntity(findByNameEntity, loginUser)).thenReturn(stockEntity);
		when(itemInfoMapper.stockInsert(stockEntity)).thenReturn(resultOfInsertStock);
		when(itemInfoMapper.getFindAll()).thenReturn(entList);
		when(itemConverter.entityListToFormList(entList)).thenReturn(formList);
		
		// 対象メソッド実行
		ItemListForm result = itemService.createItem(requestForm, loginUser);
		
		System.out.println("[sysMsgの結果]");
		System.out.println(result.getSysMsg());
		System.out.println("[errMsgの結果]");
		System.out.println(result.getErrMsg());

		// this.getItemListAll()が呼び出されたか確認
		verify(itemService, times(1)).getItemListAll();
		
		// 結果確認
		assertThat(result.getSysMsg(), is(nullValue()));
		assertThat(result.getErrMsg(), is("商品情報の更新に失敗しました" + System.lineSeparator() + "システムエラー：商品情報が見つかりません"));
		
	}
	
	/**
	 *  createItem
	 *  項目17
	 *  itemInfoMapper.insert(itemInfoEntity)とitemInfoMapper.stockInsert(stockEntity)のresultが0
	 *  this.getItemListAll()でitemListFormのerrMsgがnullでない(予期せぬエラー発生)
	 *  異常系
	 */
	@Test
	void createItem_11(){
		// 定数用の仮変数
		String loginUser = createUser;
		
		// Mock戻り値の設定
		ItemInfoForm requestForm = createItemInfoForm();
		ItemInfoEntity itemEntity = formToEntity(requestForm);
		ItemInfoEntity findByNameEntity = findByNameEntity(requestForm.getItemName());
		ItemStockEntity stockEntity = createStockEntity(findByNameEntity, loginUser);
		List<ItemInfoEntity> entList = new ArrayList<>();
		List<ItemInfoForm> formList = entityListToFormList(entList);
		
		// insertの結果
		resultOfInsertItem = 0;
		resultOfInsertStock = 0;
		
		// Mockの設定
		
		when(itemConverter.insertFormToEntity(requestForm, loginUser)).thenReturn(itemEntity);
		when(itemInfoMapper.insert(itemEntity)).thenReturn(resultOfInsertItem);
		when(itemInfoMapper.findByItemName(requestForm.getItemName())).thenReturn(findByNameEntity);
		when(itemConverter.createStockEntity(findByNameEntity, loginUser)).thenReturn(stockEntity);
		when(itemInfoMapper.stockInsert(stockEntity)).thenReturn(resultOfInsertStock);
		when(itemInfoMapper.getFindAll()).thenThrow(new RuntimeException());
		when(itemConverter.entityListToFormList(entList)).thenReturn(formList);
		
		// 対象メソッド実行
		ItemListForm result = itemService.createItem(requestForm, loginUser);
		
		System.out.println("[sysMsgの結果]");
		System.out.println(result.getSysMsg());
		System.out.println("[errMsgの結果]");
		System.out.println(result.getErrMsg());

		// this.getItemListAll()が呼び出されたか確認
		verify(itemService, times(1)).getItemListAll();
		
		// 結果確認
		assertThat(result.getSysMsg(), is(nullValue()));
		assertThat(result.getErrMsg(), is("商品情報の更新に失敗しました" + System.lineSeparator() + "システムエラー：DB接続に失敗しました"));
		
	}

	
	
	/**
	 *  createItem
	 *  項目18
	 *  itemInfoMapper.insert(itemInfoEntity)のresultが0、itemInfoMapper.stockInsert(stockEntity)のresultが1
	 *  ※商品と在庫登録は失敗、実行された登録処理はロールバックして一覧画面でエラーメッセージ表示
	 *  異常系
	 */
	@Test
	void createItem_12() throws Exception {
		// 定数用の仮変数
		String loginUser = createUser;
		
		// Mock戻り値の設定
		ItemInfoForm requestForm = createItemInfoForm();
		ItemInfoEntity itemEntity = formToEntity(requestForm);
		ItemInfoEntity findByNameEntity = findByNameEntity(requestForm.getItemName());
		ItemStockEntity stockEntity = createStockEntity(findByNameEntity, loginUser);
		List<ItemInfoEntity> entList = new ArrayList<>();
		List<ItemInfoForm> formList = entityListToFormList(entList);
		
		// insertの結果
		resultOfInsertItem = 0;
		resultOfInsertStock = 1;
		
		// Mockの設定
		
		when(itemConverter.insertFormToEntity(requestForm, loginUser)).thenReturn(itemEntity);
		when(itemInfoMapper.insert(itemEntity)).thenReturn(resultOfInsertItem);
		when(itemInfoMapper.findByItemName(requestForm.getItemName())).thenReturn(findByNameEntity);
		when(itemConverter.createStockEntity(findByNameEntity, loginUser)).thenReturn(stockEntity);
		when(itemInfoMapper.stockInsert(stockEntity)).thenReturn(resultOfInsertStock);
		when(itemInfoMapper.getFindAll()).thenReturn(entList);
		when(itemConverter.entityListToFormList(entList)).thenReturn(formList);
		
		// 対象メソッド実行
		RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
			itemService.createItem(requestForm, loginUser);
		});
		
		System.out.println("[throwの結果]");
		System.out.println(thrown.getMessage());
		
		
		// itemInfoMapper.insert(itemEntity)が1回呼び出されたことを確認
		verify(itemInfoMapper, times(1)).insert(itemEntity);
		
		// itemInfoMapper.stockInsert(stockEntity)が呼び出されていないことを確認
		verify(itemInfoMapper,times(1)).stockInsert(stockEntity);
		
		// this.getItemListAll()が呼び出されていないことを確認
		verify(itemService, times(0)).getItemListAll();
		
		// 結果確認
		assertThat(thrown.getMessage(), is("システムエラー：商品、もしくは在庫の登録中にエラーが発生しました"));
		
	}
	
	/**
	 *  createItem
	 *  項目19
	 *  itemInfoMapper.insert(itemInfoEntity)のresultが1、itemInfoMapper.stockInsert(stockEntity)のresultが0
	 *  ※商品と在庫登録は失敗、実行された登録処理はロールバックして一覧画面でエラーメッセージ表示
	 *  異常系
	 */
	@Test
	void createItem_13() throws Exception {
		// 定数用の仮変数
		String loginUser = createUser;
		
		// Mock戻り値の設定
		ItemInfoForm requestForm = createItemInfoForm();
		ItemInfoEntity itemEntity = formToEntity(requestForm);
		ItemInfoEntity findByNameEntity = findByNameEntity(requestForm.getItemName());
		ItemStockEntity stockEntity = createStockEntity(findByNameEntity, loginUser);
		List<ItemInfoEntity> entList = new ArrayList<>();
		List<ItemInfoForm> formList = entityListToFormList(entList);
		
		// insertの結果
		resultOfInsertItem = 1;
		resultOfInsertStock = 0;
		
		// Mockの設定
		when(itemConverter.insertFormToEntity(requestForm, loginUser)).thenReturn(itemEntity);
		when(itemInfoMapper.insert(itemEntity)).thenReturn(resultOfInsertItem);
		when(itemInfoMapper.findByItemName(requestForm.getItemName())).thenReturn(findByNameEntity);
		when(itemConverter.createStockEntity(findByNameEntity, loginUser)).thenReturn(stockEntity);
		when(itemInfoMapper.stockInsert(stockEntity)).thenReturn(resultOfInsertStock);
		when(itemInfoMapper.getFindAll()).thenReturn(entList);
		when(itemConverter.entityListToFormList(entList)).thenReturn(formList);
		
		// 対象メソッド実行
		RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
			itemService.createItem(requestForm, loginUser);
		});
		
		System.out.println("[throwの結果]");
		System.out.println(thrown.getMessage());
		
		
		// itemInfoMapper.insert(itemEntity)が1回呼び出されたことを確認
		verify(itemInfoMapper, times(1)).insert(itemEntity);
		
		// itemInfoMapper.stockInsert(stockEntity)が呼び出されていないことを確認
		verify(itemInfoMapper,times(1)).stockInsert(stockEntity);
		
		// this.getItemListAll()が呼び出されていないことを確認
		verify(itemService, times(0)).getItemListAll();
		
		// 結果確認
		assertThat(thrown.getMessage(), is("システムエラー：商品、もしくは在庫の登録中にエラーが発生しました"));
		
	}


	/**
	 * List<ItemInfoEntity>テストデータ作成メソッド
	 * 
	 * @return List<ItemInfoEntity>
	 */
	public List<ItemInfoEntity> createItemInfoEntityList() {

		List<ItemInfoEntity> list = new ArrayList<>();
		ItemInfoEntity ent1 = new ItemInfoEntity();
		ItemInfoEntity ent2 = new ItemInfoEntity();

		ent1.setItemId(itemId);
		ent1.setItemName(itemName);
		ent1.setItemKubun(itemKubun);
		ent1.setMaker(maker);
		ent1.setJancd(jancd);
		ent1.setPurchaseUnitPrice(purchaseUnitPrice);
		ent1.setSalesUnitPrice(salesUnitPrice);
		ent1.setStorageLocation(storageLocation);
		ent1.setReceiptDate(receiptDate);
		ent1.setCreateUser(createUser);
		ent1.setCreateDate(createDate);
		ent1.setUpdateUser(updateUser);
		ent1.setUpdateDate(updateDate);
		ent1.setLogicalDeleteFlg(logicalDeleteFlg);
		ent1.setVersion(version);

		ent2.setItemId(itemId2);
		ent2.setItemName(itemName2);
		ent2.setItemKubun(itemKubun2);
		ent2.setMaker(maker2);
		ent2.setJancd(jancd2);
		ent2.setPurchaseUnitPrice(purchaseUnitPrice2);
		ent2.setSalesUnitPrice(salesUnitPrice2);
		ent2.setStorageLocation(storageLocation2);
		ent2.setReceiptDate(receiptDate);
		ent2.setCreateUser(createUser);
		ent2.setCreateDate(createDate);
		ent2.setUpdateUser(updateUser);
		ent2.setUpdateDate(updateDate);
		ent2.setLogicalDeleteFlg(logicalDeleteFlg);
		ent2.setVersion(version);

		list.add(ent1);
		list.add(ent2);

		return list;

	}	
	
	/**
	 * entityリストをformリストに変換
	 * @param entityList
	 * @return
	 */
	public List<ItemInfoForm> entityListToFormList(List<ItemInfoEntity> entityList) {

		List<ItemInfoForm> formList = new ArrayList<>();

		for (ItemInfoEntity entity : entityList) {

			ItemInfoForm form = new ItemInfoForm();

			form.setItemId(entity.getItemId());
			form.setItemName(entity.getItemName());
			form.setItemKubun(entity.getItemKubun());
			form.setMaker(entity.getMaker());
			form.setJancd(entity.getJancd());
			form.setPurchaseUnitPrice(String.valueOf(entity.getPurchaseUnitPrice()));
			form.setSalesUnitPrice(String.valueOf(entity.getSalesUnitPrice()));
			form.setStorageLocation(entity.getStorageLocation());
			form.setReceiptDate(convertDateToStrDate((entity.getReceiptDate())));
			form.setCreateUser(entity.getCreateUser());
			form.setCreateDate(convertDateToStrDate((entity.getCreateDate())));
			form.setUpdateUser(entity.getUpdateUser());
			form.setUpdateDate(convertDateToStrDate((entity.getUpdateDate())));

			formList.add(form);
		}
		return formList;

	}
	
	/**
	 * ItemInfoFormテストデータ作成メソッド
	 * 
	 * @return ItemInfoForm
	 */
	public ItemInfoForm createItemInfoForm() {

		ItemInfoForm form = new ItemInfoForm();

		form.setItemId(itemId);
		form.setItemName(itemName);
		form.setItemKubun(itemKubun);
		form.setMaker(maker);
		form.setJancd(jancd);
		form.setPurchaseUnitPrice(String.valueOf(purchaseUnitPrice));
		form.setSalesUnitPrice(String.valueOf(salesUnitPrice));
		form.setStorageLocation(storageLocation);
		form.setReceiptDate(convertDateToStrDate(receiptDate));
		form.setCreateDate(convertDateToStrDate(createDate));
		form.setCreateUser(createUser);
		form.setUpdateDate(convertDateToStrDate(updateDate));
		form.setUpdateUser(updateUser);
		
		return form;
	}
	
	
	/**
	 * infoFormをinfoEntityに変換
	 * @param form
	 * @return
	 */
	public ItemInfoEntity formToEntity(ItemInfoForm form) {
		ItemInfoEntity ent = new ItemInfoEntity();
		
		ent.setItemId(form.getItemId());
		ent.setItemName(itemName);
		ent.setItemKubun(itemKubun);
		ent.setMaker(maker);
		ent.setJancd(jancd);
		ent.setPurchaseUnitPrice(Integer.parseInt(form.getPurchaseUnitPrice()));
		ent.setSalesUnitPrice(Integer.parseInt(form.getSalesUnitPrice()));
		ent.setStorageLocation(storageLocation);
		ent.setReceiptDate(receiptDate);
		ent.setCreateUser(createUser);
		ent.setCreateDate(createDate);
		ent.setUpdateUser(updateUser);
		ent.setUpdateDate(updateDate);
		ent.setLogicalDeleteFlg(logicalDeleteFlg);
		ent.setVersion(version);
		
		return ent;
	}
	
	/**
	 * itemNameから在庫情報とダブる項目を取得
	 * @param form
	 * @return
	 */
	public ItemInfoEntity findByNameEntity(String name) {
		ItemInfoEntity ent = new ItemInfoEntity();
		
		ent.setItemId(itemId);
		ent.setItemName(itemName);
		ent.setItemKubun(itemKubun);
		ent.setMaker(maker);
		ent.setJancd(jancd);
		ent.setPurchaseUnitPrice(purchaseUnitPrice);
		ent.setSalesUnitPrice(salesUnitPrice);
		ent.setStorageLocation(storageLocation);
		ent.setReceiptDate(null);
		ent.setCreateUser(null);
		ent.setCreateDate(null);
		ent.setUpdateUser(null);
		ent.setUpdateDate(null);
		ent.setLogicalDeleteFlg(logicalDeleteFlg);
		ent.setVersion(version);
		
		return ent;
	}
	
	 /** 
	  * infoFormをstockEntityに変換
	  * @param itemInfo
	  * @param loginUser
	  * @return
	  */

	public ItemStockEntity createStockEntity(ItemInfoEntity itemInfo, String loginUser) {
		ItemStockEntity stockEnt = new ItemStockEntity();
		
		stockEnt.setItemId(itemInfo.getItemId());
		stockEnt.setStockQuantity(0);
		stockEnt.setReceiveQuantity(0);
		stockEnt.setDispatchQuantity(0);
		stockEnt.setStorageLocation(itemInfo.getStorageLocation());
		stockEnt.setCreateDate(createDate);
		stockEnt.setCreateUser(loginUser);
		stockEnt.setUpdateDate(updateDate);
		stockEnt.setUpdateUser(loginUser);
		stockEnt.setLogicalDeleteFlg(false);
		stockEnt.setVersion(0);
		
		return stockEnt;
	}

	/**
	 * 現在の日付を生成
	 * @return
	 */
	public Date nowDate() {
		Date nowDate = new Date();
		return nowDate;
	}

	/**
	 * Date型をString型に変換
	 * @param date
	 * @return
	 */
	public String convertDateToStrDate(Date date) {
		// nullチェック
		if (date == null) {
			return null;
		}
		// フォーマットに成型
		String strDate = sdFormat.format(date);
		return strDate;
	}

	

	
}