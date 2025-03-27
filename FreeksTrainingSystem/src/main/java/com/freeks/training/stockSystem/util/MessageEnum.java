package com.freeks.training.stockSystem.util;

public enum MessageEnum {
	
	// エラーメッセージ一覧
	SYSYEM_ERROR_GET_DB_DATA("システムエラー：DB接続に失敗しました"),
	
	SYSYEM_ERROR_OCCURED_DURING_CSV_EXPORTS("システムエラー：CSV出力中にエラーが発生しました"),
	
	ITEM_INFO_NOT_FOUND("システムエラー：商品情報が見つかりません"),
	
	STOCK_INFO_NOT_FOUND("在庫情報が見つかりません"),
	
	DUPLICATE_ITEM_NAME("商品名称が重複しています"),
	
	FAILED_ITEM_DATABASE("商品情報の更新に失敗しました"),
	
	SUCCESS_ITEM_DATA_BASE("商品情報を更新しました"),
	
	STOCK_NOUPDATE_INFO("更新情報に変更箇所がありません"),
	
	STOCK_QUANTITY_SHORTAGE("在庫数が不足しています"),
	
	FAILED_STOCK_DATABASE("在庫情報の更新に失敗しました"),
	
	SUCCESS_STOCK_DATA_BASE("在庫情報を更新しました"),
	
	FAILED_DELETE_ITEM("商品情報の削除に失敗しました"),
	
	SUCCESS_DELETE_ITEM("商品情報を削除しました"),
	
	SUCCESS_EXPORTS_TO_CSV("CSVファイルをダウンロードしました"),
	
	FAILED_EXPORTS_TO_CSV("ダウンロード可能なデータがありません"),
	
	FAILED_CONVERSION_LOGIC("変換処理に失敗しました"),
	
	FAILED_CREATE_ITEM("システムエラー：商品、もしくは在庫の登録中にエラーが発生しました");
	
	// フィールド変数
	private String message;
	
	//コンストラクタ
	private MessageEnum(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
	
};
