package com.freeks.training.stockSystem.form;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/** 商品一覧リストフォーム
 * 
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class ItemListForm extends BaseForm {
	// 商品情報Formリスト
	private List<ItemInfoForm> itemInfoFormList;

	

}
