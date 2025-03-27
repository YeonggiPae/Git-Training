package com.freeks.training.stockSystem.validate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;

import com.freeks.training.stockSystem.validate.impl.ExactLengthValidateImpl;

// フィールドが適用対象
@Target(ElementType.FIELD)

// 実行時までアノテーション情報を保持する
@Retention(RetentionPolicy.RUNTIME)

// バリエータクラスを登録
@Constraint(validatedBy = {ExactLengthValidateImpl.class})


/** 字数制限用バリデータインターフェース
 * null、空文字、空白のみ、先頭空白、末尾空白をチェック
 * 字数制限
 */
@ReportAsSingleViolation
public @interface ExactLengthValidate {
	// 字数制限の最大文字数格納用
	int min();  // 最小文字数
	int max();  // 最大文字数 
	
	String message() default "";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
	

}
