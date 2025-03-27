package com.freeks.training.stockSystem.validate.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.freeks.training.stockSystem.validate.CommonValidate;
import com.freeks.training.stockSystem.validate.ValidationItem;


/** 基本的なバリデータクラス
 * null、空文字、空白のみ、先頭空白、末尾空白をチェック
 */
public class CommonValidateImpl implements ConstraintValidator<CommonValidate, Object>{
	
	@Autowired
	ValidationItem item;
	
	@Autowired
	MessageSource msg;
	

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
		}
		
		return true;
	}
	

}
