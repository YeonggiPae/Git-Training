package com.freeks.training.stockSystem.validate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;

import com.freeks.training.stockSystem.validate.impl.JanCodeValidateImpl;

// フィールドが適用対象
@Target(ElementType.FIELD)

// 実行時までアノテーション情報を保持する
@Retention(RetentionPolicy.RUNTIME)

// バリエータクラスを登録
@Constraint(validatedBy = {JanCodeValidateImpl.class})


/** JANコード用バリデータインターフェース
 * null、空文字、空白のみ、先頭空白、末尾空白をチェック
 * 半角英数字、13桁制限
 */
@ReportAsSingleViolation
public @interface JanCodeValidate {
	// 桁数指定のための引数として使用
	int value();
	
	String message() default "";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default {};
	

}
