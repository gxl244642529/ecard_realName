package com.citywithincity.ecard.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class DateTimeUtil_M {

	/**
	 * 
	 * @return
	 */
	public static String formatDate(long date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return dateFormat.format(new Date(date));
	}

	public static String getDetailTime() {
		String detailTime = null;
		detailTime = getDate() + "." + getYestoday() + "." + getTime();
		return detailTime;
	}

	public static String getDate() {
		SimpleDateFormat dateformat1 = new SimpleDateFormat("yyyy.yy.MMdd");
		return dateformat1.format(new Date());
	}

	public static String getYestoday() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		String yesterday = new SimpleDateFormat("yyyyMMdd ").format(cal
				.getTime());
		return yesterday;
	}

	public static String getDateTime() {
		SimpleDateFormat dateformat1 = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return dateformat1.format(new Date());
	}

	public static String getTime() {
		SimpleDateFormat dateformat1 = new SimpleDateFormat("HHmmss");
		return dateformat1.format(new Date());
	}

	/**
	 * 大于当前时间返回true
	 * 
	 * @param time
	 * @return
	 */
	public static boolean compareTime(String time) {
		// SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			Date date = dateFormat.parse(time);
			return date.getTime() - System.currentTimeMillis() > 0;// 这样得到的差值是微秒级别
		} catch (Exception e) {
		}
		return true;
	}

	/**
	 * 大于当前时间返回true
	 * 
	 * @param time
	 * @param format
	 *            时间格式
	 * @return
	 */
	public static boolean compareTime(String time, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		try {
			Date date = dateFormat.parse(time);
			return date.getTime() - System.currentTimeMillis() > 0;// 这样得到的差值是微秒级别
		} catch (Exception e) {
		}
		return true;
	}

	/**
	 * 时间格式转换
	 * 
	 * @param time
	 * @param format
	 * @param destFormat
	 * @return
	 * @throws ParseException
	 */
	public static String convertFormat(String time, String format,
			String destFormat) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		SimpleDateFormat destDateFormat = new SimpleDateFormat(destFormat);
		String destTime = null;
		destTime = destDateFormat.format(dateFormat.parse(time));
		return destTime;
	}

	/**
	 * 身份证提取生日 转换为 yyyy-MM-dd 格式
	 * 
	 * @param idCard
	 * @param dateFormat
	 * @return
	 */
	public static String getDirthdayFromIdCard(String IDCard, String dateFormat) {

		String birthday = IDCard.substring(6, 14);
		try {
			// birthday = convertFormat(birthday, "yyyyMMdd", "yyyy-MM-dd");
			birthday = convertFormat(birthday, "yyyyMMdd", dateFormat);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return birthday;
	}

	/**
	 * 身份证计算周岁，精确到月
	 */
	public static int getAge(String id) {
		Calendar ca = Calendar.getInstance();
		int nowYear = ca.get(Calendar.YEAR);
		int nowMonth = ca.get(Calendar.MONTH) + 1;
		int len = id.length();
		if (len == 18) {
			try{
				int IDYear = Integer.parseInt(id.substring(6, 10));
				int IDMonth = Integer.parseInt(id.substring(10, 12));
				if ((IDMonth - nowMonth) > 0) {
					return nowYear - IDYear - 1;
				} else
					return nowYear - IDYear;
			}catch (Throwable t){

			}

		}
		return 0;
	}

}
