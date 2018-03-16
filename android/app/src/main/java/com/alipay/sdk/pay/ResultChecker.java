package com.alipay.sdk.pay;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 对签名进行验签
 * 
 */
public class ResultChecker {

	public static final int RESULT_INVALID_PARAM = 0;
	public static final int RESULT_CHECK_SIGN_FAILED = 1;
	public static final int RESULT_CHECK_SIGN_SUCCEED = 2;

	String mContent;

	public ResultChecker(String content) {
		this.mContent = content;
	}
	
	public String getResult() throws JSONException {
		JSONObject objContent = BaseHelper.string2JSON(this.mContent, ";");
		String result = objContent.getString("result");
		result = result.substring(1, result.length() - 1);
		
		return result;
	}
	
	public long getOrderNum() throws JSONException {
		long orderId = -1;
		
		JSONObject objContent = BaseHelper.string2JSON(this.mContent, ";");
		String result = objContent.getString("result");
		result = result.substring(1, result.length() - 1);
		
		int index = result.indexOf("out_trade_no=\"") + "out_trade_no=\"".length();
		String out_trade_no = result.substring(index);
		List<Character> list = new ArrayList<Character>();
		for (int i = 0, len = result.length(); i < len; i++) {
			if (out_trade_no.charAt(i) == '\"') {
				break;
			} else {
				char c = out_trade_no.charAt(i);
				list.add(c);
				
			}
		}
		
		int listSize = list.size();
		char[] charList = new char[listSize];
		for (int i = 0; i < listSize; i++) {
			charList[i] = list.get(i);
		}
		
		out_trade_no = new String(charList);
		orderId = Long.parseLong(out_trade_no);
		
		return orderId;
	}

	/**
	 * 从验签内容中获取成功状态
	 * 
	 * @return
	 */
	public boolean getSuccess() {
		boolean isSuccess = false;

		try {
			JSONObject objContent = BaseHelper.string2JSON(this.mContent, ";");
			String result = objContent.getString("result");
			result = result.substring(1, result.length() - 1);

			int index = result.indexOf("success=\"") + "success=\"".length();
			String success = result.substring(index);
			char[] charList = new char[128];
			for (int i = 0 ,len = result.length(); i < len; i++) {
				if (success.charAt(i) == '\"') {
					break;
				} else {
					char c = success.charAt(i);
					charList[i] = c;
					
				}
			}
			isSuccess = Boolean.parseBoolean(success);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return isSuccess;
	}

	/**
	 * 对签名进行验签
	 * 
	 * @return
	 */
	public int checkSign() {
		int retVal = RESULT_CHECK_SIGN_SUCCEED;

		try {
			JSONObject objContent = BaseHelper.string2JSON(this.mContent, ";");
			String result = objContent.getString("result");
			result = result.substring(1, result.length() - 1);
			// 获取待签名数据
			int iSignContentEnd = result.indexOf("&sign_type=");
			@SuppressWarnings("unused")
			String signContent = result.substring(0, iSignContentEnd);
			// 获取签名
			JSONObject objResult = BaseHelper.string2JSON(result, "&");
			String signType = objResult.getString("sign_type");
			signType = signType.replace("\"", "");

			String sign = objResult.getString("sign");
			sign = sign.replace("\"", "");
			// 进行验签 返回验签结果
//			if (signType.equalsIgnoreCase("RSA")) {
//				if (!Rsa.doCheck(signContent, sign,
//						PartnerConfig.RSA_ALIPAY_PUBLIC))
//					retVal = RESULT_CHECK_SIGN_FAILED;
//			}
		} catch (Exception e) {
			retVal = RESULT_INVALID_PARAM;
			e.printStackTrace();
		}

		return retVal;
	}

//	boolean isPayOk() {
//		boolean isPayOk = false;
//
//		String success = getSuccess();
//		if (success.equalsIgnoreCase("true")
//				&& checkSign() == RESULT_CHECK_SIGN_SUCCEED)
//			isPayOk = true;
//
//		return isPayOk;
//	}
}