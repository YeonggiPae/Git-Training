package com.freeks.training.stockSystem.entity;

import java.util.Date;

import lombok.Data;

/**商品情報エンティティ
 * 
 */

@Data
public class ItemInfoEntity {
	// 商品ID
	private int itemId;
	
	// 商品名称
	private String itemName;
	
	// 区分
	private String itemKubun;
	
	// メーカー名
	private String maker;
	
	// JANコード
	private String jancd;
	
	// 購入単価
	private int purchaseUnitPrice;
	
	// 販売単価
	private int salesUnitPrice;
	
	// 保管場所
	private String storageLocation;
	
	// 入庫日
	private Date receiptDate;
	
	// 作成者
	private String createUser;
	

	// 作成日時
	private Date createDate;
	
	// 更新者
	private String updateUser;
	
	// 更新日時
	private Date updateDate;
	
	// 論理削除フラグ
	private boolean logicalDeleteFlg;
	
	// バージョン
	private int version;
	

}
