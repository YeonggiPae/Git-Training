package com.freeks.training.stockSystem.entity;

import java.util.Date;

import lombok.Data;

/**商品在庫情報エンティティ
 * 
 */

@Data
public class ItemStockEntity {
	
	// 在庫ID
	private int storageStockId;
	
	// 商品ID
	private int itemId;
	
	// 在庫数
	private int stockQuantity;
	
	// 入庫数
	private int receiveQuantity;
	
	// 出庫数
	private int dispatchQuantity;
	
	// 保管場所
	private String storageLocation;
	
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
