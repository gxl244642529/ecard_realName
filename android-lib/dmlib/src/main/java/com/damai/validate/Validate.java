package com.damai.validate;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;

import com.citywithincity.utils.ValidateUtil;

@SuppressLint({ "DefaultLocale", "SimpleDateFormat" }) public class Validate {
	
	public static class ValidateError extends Exception{

		/**
		 * 
		 */
		private static final long serialVersionUID = -1789370047921445061L;
		
		
		public ValidateError(String error){
			super(error);
		}
		
	}
	
	
	private static Map<String, Method> map = new HashMap<String, Method>();
	
	static{
		Method[] methods = Validate.class.getMethods();
		for (Method method : methods) {
			map.put(method.getName(), method);
		}
	}
	
	public static boolean helpInvoke(String name,String rule){
		Method method = map.get(name);
		try {
			return (Boolean) method.invoke(null, rule);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
	}
	
	public static boolean helpInvoke(String name,String rule,int v){
		Method method = map.get(name);
		try {
			return (Boolean) method.invoke(null, rule,v);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} 
	}
	public final static boolean isNull(String str) {
		return str == null || "".equals(str.trim())
				|| "null".equals(str.toLowerCase());
	}

	/**
	 * 正则表达式匹配
	 * 
	 * @param text
	 *            待匹配的文本
	 * @param reg
	 *            正则表达式
	 * @return
	 * @author jiqinlin
	 */
	private final static boolean match(String text, String reg) {
		if (isNull(text) || isNull(reg))
			return false;
		return Pattern.compile(reg).matcher(text).matches();
	}
	public static boolean number(String value){
		return true;
	}
	public static boolean integer(String value){
		return match(value, "^[+]?\\d+$");
	}
	public static boolean account(String value){
		return true;
	}
	public static boolean email(String value){
		return true;
	}
	public static boolean password(String value){
		return true;
	}
	
	public static boolean url(String value){
		return value.startsWith("http://") || value.startsWith("https://");
	}
	
	public static boolean minLen(String value,int len){
		return value.length()>=len;
	}
	
	public static boolean maxLen(String value,int len){
		return value.length() <= len;
	}
	
	public static boolean minValue(String value,int v){
		return Integer.parseInt(value) >= v;
	}
	
	public static boolean maxValue(String value,int v){
		return Integer.parseInt(value) <= v;
	}
	/**
	 * 匹配浮点数
	 * 
	 * @param str
	 * @return
	 * @author jiqinlin
	 */
	public final static boolean isFloat(String str) {
		return match(str, "^[-\\+]?\\d+(\\.\\d+)?$");
	}
	/**
	 * 判断数值类型，包括整数和浮点数
	 * 
	 * @param str
	 * @return
	 * @author jiqinlin
	 */
	public final static boolean isNumeric(String str) {
		if (isFloat(str) || integer(str))
			return true;
		return false;
	}
	/**
	 * 功能：判断字符串是否为日期格式
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isDate(String strDate) {
		Pattern pattern = Pattern
				.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
		Matcher m = pattern.matcher(strDate);
		if (m.matches()) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 身份证号
	 * @param value
	 * @return
	 * @throws ValidateError 
	 */
	public static boolean idcard(String IDStr) throws ValidateError{
		String[] ValCodeArr = { "1", "0", "x", "9", "8", "7", "6", "5", "4",
				"3", "2" };
		String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
				"9", "10", "5", "8", "4", "2" };
		String Ai = "";
		// ================ 号码的长度 15位或18位 ================
		if (IDStr.length() != 18) {
			throw new ValidateError("身份证号码长度应该为18位。");
		}
		// =======================(end)========================

		// ================ 数字 除最后以为都为数字 ================
		Ai = IDStr.substring(0, 17);
		if (isNumeric(Ai) == false) {
			throw new ValidateError("18位号码除最后一位外，都应为数字。");
		}
		// =======================(end)========================

		// ================ 出生年月是否有效 ================
		String strYear = Ai.substring(6, 10);// 年份
		String strMonth = Ai.substring(10, 12);// 月份
		String strDay = Ai.substring(12, 14);// 月份
		if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
			throw new ValidateError("身份证生日无效。");
		}
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
					|| (gc.getTime().getTime() - s.parse(
							strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
				throw new ValidateError("身份证生日不在有效范围。");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
			throw new ValidateError("身份证月份无效。");
		}
		if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
			throw new ValidateError("身份证日期无效");
		}
		// =====================(end)=====================

		// ================ 地区码时候有效 ================
		Map<String, String> h = ValidateUtil.hashtable;
		if (h.get(Ai.substring(0, 2)) == null) {
			throw new ValidateError("身份证地区编码错误。");
		}
		// ==============================================

		// ================ 判断最后一位的值 ================
		int TotalmulAiWi = 0;
		for (int i = 0; i < 17; i++) {
			TotalmulAiWi = TotalmulAiWi
					+ Integer.parseInt(String.valueOf(Ai.charAt(i)))
					* Integer.parseInt(Wi[i]);
		}
		int modValue = TotalmulAiWi % 11;
		String strVerifyCode = ValCodeArr[modValue];
		Ai = Ai + strVerifyCode;

		if (IDStr.length() == 18) {
			if (Ai.equals(IDStr) == false) {
				throw new ValidateError("身份证无效，不是合法的身份证号码");
			}
		}
		return true;
	}
	
	
}
