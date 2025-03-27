package com.freeks.training.stockSystem.session;

import java.io.Serializable;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import com.freeks.training.stockSystem.form.ItemInfoForm;
import com.freeks.training.stockSystem.form.ItemStockForm;

import lombok.Data;

@Component
@SessionScope
@Data

/* セッションに保存するクラスはシリアライズできるようにしなければならない
 * シリアライズ：メモリの割り当て領域が一杯になったりした時に、
 * セッション上の保存オブジェクトを一旦Diskに書き出して退避させる処理
 */
public class ItemSession implements Serializable{
	
	// 商品情報フォーム
	private ItemInfoForm itemInfoForm;
	
	// 更新前の商品情報フォーム
	private ItemInfoForm beforeUpdItemInfoForm;
	
	// 在庫情報フォーム
	private ItemStockForm itemStockForm;
	
	// 更新前の在庫情報フォーム
	private ItemStockForm beforeUpdItemStockForm;
	
	// 各フォームのセッションをリセット
	public void clearItemInfoForm(){
		this.itemInfoForm = null;
		this.beforeUpdItemInfoForm = null;
	}
	
	public void clearItemStockForm(){
		this.itemStockForm = null;
		this.beforeUpdItemStockForm = null;
	}


}
