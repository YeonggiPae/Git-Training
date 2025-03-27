package com.freeks.training.stockSystem.form;

import com.freeks.training.stockSystem.validate.CommonValidate;
import com.freeks.training.stockSystem.validate.NumberValidate;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** 商品在庫フォーム
 *  カスタムバリデーションはすべてnull、空文字、空白のみ、先頭空白、末尾空白チェックを含む
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class ItemStockForm extends BaseForm{
	
	// 在庫ID
	private int storageStockId;
	
	// 商品ID
	private int itemId;
	
	// 在庫数
	private String stockQuantity;
	
	// 入庫数
	@NumberValidate  // 数字のみ
	private String receiveQuantity;
	
	// 出庫数
	@NumberValidate  // 数字のみ
	private String dispatchQuantity;
	
	// 保管場所
	@CommonValidate  // null、空文字、空白チェック
	private String storageLocation;
	
	// 作成者
	private String createUser;

	// 作成日時
	private String createDate;
	
	// 更新者
	private String updateUser;
	
	// 更新日時
	private String updateDate;
	

}
