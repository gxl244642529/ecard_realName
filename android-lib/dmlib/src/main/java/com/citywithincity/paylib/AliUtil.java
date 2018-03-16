package com.citywithincity.paylib;

import java.util.HashMap;
import java.util.Map;


public class AliUtil {
	/**
	 * 字符串转json对象
	 * @param str
	 * @param split
	 * @return
	 */
	public static Map<String, String> string2JSON(String str, String split) {
		Map<String, String> json = new HashMap<String, String>();
		String[] arrStr = str.split(split);
		for (int i = 0; i < arrStr.length; i++) {
			String[] arrKeyValue = arrStr[i].split("=");
			json.put(arrKeyValue[0],
					arrStr[i].substring(arrKeyValue[0].length() + 1));
		}

		return json;
	}
	
	

	public static String getResult(String content) {
		Map<String, String> objContent = string2JSON(content, ";");
		String result = objContent.get("result");
		result = result.substring(1, result.length() - 1);
		
		return result;
	}
	
	public static int checkPayState(String result) {
		int start = result.indexOf("resultStatus={") + "resultStatus={".length();
		String RESULT_CODE = result.substring(start, start + 4);
		return Integer.parseInt(RESULT_CODE);
	}
	
	/**
	 * 获取网站订单id
	 * @param result
	 * @return
	 */
	public static String getOutId(String result) {
		int start = result.indexOf("out_trade_no=\"") + "out_trade_no=\"".length();
		int end = result.indexOf("\"",start+1);
		return result.substring(start, end-1);
	}
	
	
	
	/**
	 * 获取网站订单id
	 * @param result
	 * @return
	 */
	public static double getTotalFee(String result) {
		int start = result.indexOf("total_fee=\"") + "total_fee=\"".length();
		int end = result.indexOf("\"",start+1);
		String strFee= result.substring(start, end);
		return Double.parseDouble(strFee);
	}
	
}
