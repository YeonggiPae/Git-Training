package com.freeks.training.stockSystem.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class CommonUtil {
	//日付フォーマット

	private SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd");
	
	/**
	* Date型をString型に変換
	* @param date
	* @return
	*/
	public String convertDateToStrDate(Date date) {
		// nullチェック
		if (date == null) {
		return null;
		}
		// フォーマットに成型
		String strDate = sdFormat.format(date);
		return strDate;
	}
	
	/**
	 *  現在の日付を取得
	 * @return
	 */
	public Date nowDate() {
		Date date = new Date();
		
		return date;
	}

}
