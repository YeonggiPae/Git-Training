package com.freeks.training.stockSystem.validate;

import jakarta.validation.ConstraintValidatorContext;

import org.springframework.stereotype.Component;

@Component
public class ValidationItem {
	
	/**null判定チェック
	 * 
	 * @param obj  チェックする対象
	 * @return  nullならtrueを返す
	 */
	 
	public boolean isNull(Object obj){
		return obj == null;
	}
	
	/** 文字列が空かチェック
	 * 
	 * @param value  チェックする対象
	 * @return null、空の文字列ならtrueを返す
	 */
	public boolean isEmpty(String value) {
		return value.isEmpty();
	}
	
	/**
	 * 空白文字チェック
	 * @param str  チェックする対象
	 * @return  null、空文字、空白文字ならtrueを返す
	 */
	public boolean isBlank(String str) {
		return str.isBlank();
	}
	
	/** 先頭の文字が空白かをチェック
	 * 
	 * @param str  チェックする対象
	 * @return  先頭が空白ならtrueを返す
	 */
	public boolean isFirstSpace(String str) {
		return str.startsWith(" ") || str.startsWith("　");
	}
	
	/** 末尾の文字が空白かをチェック
	 * 
	 * @param str  チェックする対象
	 * @return  末尾が空白ならtrueを返す
	 */
	public boolean isLastSpace(String str) {
		return str.endsWith(" ") || str.endsWith("　");
	}
	
	/** 規定文字数以内かをチェック
	 * 
	 * @param str  チェックする対象
	 * @param min  最小文字数
	 * @param max  最大文字数
	 * @return  指定した文字数以内の場合trueを返す
	 */
	public boolean isBetween(String str, int min, int max) {
		int length = str.length();
		return length >= min && length <= max;
	}
	
	/**
	 *  0以上、int型の最大値以内か
	 * @param str  チェックする対象
	 * @return  0以上 or int型の最大値以内ならtrueを返す
	 */
	public boolean isIntRange(String str) {
		try {
			int value = Integer.parseInt(str);
			return value >= 0 && value <= Integer.MAX_VALUE;
		}catch(Exception e) {
			return false;
		}
	}
	
	/**
	 *  半角数字のみかチェック
	 * @param str  チェックする対象
	 * @return  数字のみならtrueを返す
	 */
	public boolean isNumeric(String str) {
		return str.matches("^[0-9]+$");
	}
	
	/** 半角英数字のみかチェック
	 * 
	 * @param str  チェックする対象
	 * @return  半角英数字のみならtrueを返す
	 */
	public boolean isAlphaNumeric(String str) {
		return str.matches("^[a-zA-Z0-9]+$");
	}
	
	/** 桁数が合っているかチェック
	 * 
	 * @param str  チェックする対象
	 * @param length  指定の桁数
	 * @return  指定した桁数とイコールならtrueを返す
	 */
	public boolean isDigit(String str, int length) {
		return str.length() == length;
	}
	
	
	/**バリデーションエラー生成処理
	 * 
	 * @param msg エラーメッセージ
	 * @param context バリデータ情報
	 * @return 常にfalseを返す
	 */
	public boolean createErrorWithMsg(String msg, ConstraintValidatorContext context) {
		
		// デフォルトのエラーメッセージを無効化
		context.disableDefaultConstraintViolation();
		
		// バリデーションの独自エラーメッセージ設定
		context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
		return false;
	}
}
