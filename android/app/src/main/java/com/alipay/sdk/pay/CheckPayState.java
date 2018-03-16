package com.alipay.sdk.pay;

public class CheckPayState {

	public static int checkPayState(String result) {
		String RESULT_CODE;
		int start = result.indexOf("resultStatus={") + "resultStatus={".length();
		RESULT_CODE = result.substring(start, start + 4);
		int resultState = -1;
		try {
			resultState = Integer.parseInt(RESULT_CODE);

		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return resultState;
	}

}
