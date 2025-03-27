package com.freeks.training.stockSystem.validate.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.freeks.training.stockSystem.validate.JanCodeValidate;
import com.freeks.training.stockSystem.validate.ValidationItem;


/** JANコード用バリデータインタークラス
 * null、空文字、空白のみ、先頭空白、末尾空白をチェック
 * 半角英数字、13桁制限
 */
public class JanCodeValidateImpl implements ConstraintValidator<JanCodeValidate, Object>{
	
	@Autowired
	ValidationItem item;
	
	@Autowired
	MessageSource msg;
	
	
	private int requiredLength;  // 桁数
	
	
	/** Formに設定したアノテーションの引数から値を受け取る
	 * 
	 */
	@Override
	public void initialize(JanCodeValidate constraintAnnotation) {
		// アノテーションの引数を取得
		this.requiredLength = constraintAnnotation.value();
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
		// 半角英数字以外の場合
		}else if(!str.matches("^[a-zA-Z0-9]+$")) {
			return item.createErrorWithMsg(msg.getMessage("Valid.AlphaDigit", null, null), context);
		// 指定桁数以外の場合
		}else if(!item.isDigit(str, requiredLength)) {
			return item.createErrorWithMsg(msg.getMessage("Valid.Digit", new String[] {String.valueOf(requiredLength)}, null), context);
		}
		return true;
	}
	
}
