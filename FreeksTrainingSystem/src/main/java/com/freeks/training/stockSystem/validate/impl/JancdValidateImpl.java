package com.freeks.training.stockSystem.validate.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.freeks.training.stockSystem.validate.JancdValidate;
import com.freeks.training.stockSystem.validate.ValidationItem;


public class JancdValidateImpl implements ConstraintValidator<JancdValidate, Object> {

	@Autowired
	ValidationItem item;
	@Autowired
	MessageSource msg;
	
	private int digit;  // 指定値
	
	/** Formに設定したアノテーションの引数から最小値と最大値を受け取る
	 * 
	 */
	@Override
	public void initialize(JancdValidate constraintAnnotation) {
		// アノテーションの引数を取得
		this.digit = constraintAnnotation.digit();
		
	}
	
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {

		// バリデート対象となる値
		String str = (String) value;
		
		// nullか空の文字列の場合
		if (item.isNull(value) || item.isEmpty(str)) {
			return item.createErrorWithMsg(msg.getMessage("Valied.Required", null, null), context);
		// 空白のみの場合
		} else if (item.isBlank(str)) {
			return item.createErrorWithMsg(msg.getMessage("Valied.Blank", null, null), context);
		// 先頭が空白の場合
		} else if (item.isFirstSpace(str)) {
			return item.createErrorWithMsg(msg.getMessage("Valied.BlankFirst", null, null), context);
		// 末尾が空白の場合
		} else if (item.isLastSpace(str)) {
			return item.createErrorWithMsg(msg.getMessage("Valied.BlankLast", null, null), context);

		} else if (item.isAlphaNumeric(str)) {
			return item.createErrorWithMsg(msg.getMessage("Valied.AlphaDigit", null, null), context);
		// 指定した桁数でない場合
		}else if (item.isDigit(str, digit)) {
			return item.createErrorWithMsg(msg.getMessage("Valied.Digit", new String[]{ String.valueOf(digit) }, null), context);
		}

		return true;
	}
	
}
