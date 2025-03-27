package com.freeks.training.stockSystem.validate.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.freeks.training.stockSystem.validate.PriceValidate;
import com.freeks.training.stockSystem.validate.ValidationItem;

/** 価格用バリデータクラス
 * null、空文字、空白のみ、先頭空白、末尾空白をチェック
 * 半角数字のみ、0～int型の最大値までのみ、字数制限
 */
public class PriceValidateImpl implements ConstraintValidator<PriceValidate, Object>{
	
	@Autowired
	ValidationItem item;
	
	@Autowired
	MessageSource msg;
	
	private int minLength;  // 最小値
	private int maxLength;  // 最大値
	
	
	/** Formに設定したアノテーションの引数から最小値と最大値を受け取る
	 * 
	 */
	@Override
	public void initialize(PriceValidate constraintAnnotation) {
		// アノテーションの引数を取得
		this.minLength = constraintAnnotation.min();
		this.maxLength = constraintAnnotation.max();
	}
	
	
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {

		// バリデート対象となる値
		String str = (String)value;
		
		// nullか空の文字列の場合
		if(item.isNull(value) || item.isEmpty(str)) {
			return item.createErrorWithMsg(msg.getMessage("Valid.Required", null, null), context);
		// 空白のみの場合
		}else if(item.isBlank(str)) {
			return item.createErrorWithMsg(msg.getMessage("Valid.Blank", null, null), context);
		// 先頭が空白の場合
		}else if(item.isFirstSpace(str)) {
			return item.createErrorWithMsg(msg.getMessage("Valid.BlankFirst", null, null), context);
		// 末尾が空白の場合
		}else if(item.isLastSpace(str)) {
			return item.createErrorWithMsg(msg.getMessage("Valid.BlankLast", null, null), context);
		// 指定文字数以内でない場合
		}else if(!item.isBetween(str, minLength, maxLength)) {
			return item.createErrorWithMsg(msg.getMessage("Valid.Range", new String[] {String.valueOf(minLength), String.valueOf(maxLength)}, null), context);
		// 半角数字でない場合
		}else if(!item.isNumeric(str)) {
			return item.createErrorWithMsg(msg.getMessage("Valid.Integer", null, null), context);
		// 0～int型の最大値以内でない場合
		}else if(!item.isIntRange(str)) {
			return item.createErrorWithMsg(msg.getMessage("Valid.IntRange", null, null), context);
		}
		return true;
	}
	
}
