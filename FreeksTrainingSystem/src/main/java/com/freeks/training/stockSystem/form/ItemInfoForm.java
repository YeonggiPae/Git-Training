package com.freeks.training.stockSystem.form;

import com.freeks.training.stockSystem.validate.ExactLengthValidate;
import com.freeks.training.stockSystem.validate.JanCodeValidate;
import com.freeks.training.stockSystem.validate.PriceValidate;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** 商品情報フォーム
 *  カスタムバリデーションはすべてnull、空文字、空白のみ、先頭空白、末尾空白チェックを含む
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class ItemInfoForm extends BaseForm{
	
	// 商品ID
	private int itemId;
	
	// 商品名称
	@ExactLengthValidate(min = 1, max = 20) // 1～20文字制限
	private String itemName;
	
	// 区分
	private String itemKubun;
	
	// メーカー名
	@ExactLengthValidate(min = 1, max = 20) // 1～20文字制限
	private String maker;
	
	// JANコード 
	@JanCodeValidate(13)  // 半角英数字のみ、13桁制限
	private String jancd;
	
	// 購入単価
	@PriceValidate(min = 1, max = 10) // 半角数字のみ、0～2147483647(int型の最大値)のみ、1～10文字制限
	private String purchaseUnitPrice;
	
	// 販売単価
	@PriceValidate(min = 1, max = 10) // 半角数字のみ、0～2147483647(int型の最大値)のみ、1～10文字制限
	private String salesUnitPrice;
	
	// 保管場所
	@ExactLengthValidate(min = 1, max = 20) // 1～20文字制限
	private String storageLocation;
	
	// 入庫日
	private String receiptDate;
	
	// 作成者
	private String createUser;

	// 作成日時
	private String createDate;
	
	// 更新者
	private String updateUser;
	
	// 更新日時
	private String updateDate;
	

}
