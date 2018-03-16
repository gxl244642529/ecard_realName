package com.citywithincity.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat")
public class DateTimeUtil {
	/**
	 * 获取当前日期的字符串格式：Y-m-d
	 * 
	 * @return
	 */
	public static String getDate() {
		SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyy-MM-dd");
		return dateformat1.format(new Date());
	}

	public static String getYestoday() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		String yesterday = new SimpleDateFormat("yyyy-MM-dd ").format(cal
				.getTime());
		return yesterday;
	}

	public static String getDateTime() {
		SimpleDateFormat dateformat1 = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return dateformat1.format(new Date());
	}
	
	/**
	 * 
	 * @param time
	 * @return
	 */
	public static String time2String(long time) {
		SimpleDateFormat dateformat1 = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return dateformat1.format(new Date(time));
	}

	public static String getTime() {
		SimpleDateFormat dateformat1 = new SimpleDateFormat("HH:mm:ss");
		return dateformat1.format(new Date());
	}
}
