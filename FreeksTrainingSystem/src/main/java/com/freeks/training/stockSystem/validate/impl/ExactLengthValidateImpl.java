package com.freeks.training.stockSystem.validate.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.freeks.training.stockSystem.validate.ExactLengthValidate;
import com.freeks.training.stockSystem.validate.ValidationItem;

/** 字数制限用バリデータクラス
 * null、空文字、空白のみ、先頭空白、末尾空白をチェック
 * 字数制限
 */
public class ExactLengthValidateImpl implements ConstraintValidator<ExactLengthValidate, Object>{
	
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
	public void initialize(ExactLengthValidate constraintAnnotation) {
		// アノテーションの引数を取得
		this.minLength = constraintAnnotation.min();
		this.maxLength = constraintAnnotation.max();
	}
	
	
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		
		// バリデート対象となる値
		String str = value.toString();
		
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
		}
		
		return true;
	}
	

}
