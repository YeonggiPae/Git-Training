package com.freeks.training.stockSystem.validate.impl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import com.freeks.training.stockSystem.validate.ItemKubunValidate;
import com.freeks.training.stockSystem.validate.ValidationItem;


public class ItemKubunValidateImpl implements ConstraintValidator<ItemKubunValidate, Object> {

	@Autowired
	ValidationItem item;
	@Autowired
	MessageSource msg;

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {

		// バリデート対象となる値
		String str = (String) value;

		if (item.isNull(value) || item.isEmpty(str)) {
			return item.createErrorWithMsg(msg.getMessage("Valied.Required", null, null), context);

		} else if (item.isBlank(str)) {
			return item.createErrorWithMsg(msg.getMessage("Valied.Blank", null, null), context);

		} else if (item.isFirstSpace(str)) {
			return item.createErrorWithMsg(msg.getMessage("Valied.BlankFirst", null, null), context);

		} else if (item.isLastSpace(str)) {
			return item.createErrorWithMsg(msg.getMessage("Valied.BlankLast", null, null), context);

		} else if (item.isDigit(str, 1)) {
			return item.createErrorWithMsg(msg.getMessage("Valied.Digit", new String[] { "1" }, null), context);
		}

		return true;
	}
	
}
