package com.damai.pay;

import android.view.View;

import com.damai.core.ApiListener;
import com.damai.models.DMModel;

import java.util.Map;

public class DMPayAction extends DMModel {
	

	public void prePay(int payType, String api, Map<String,Object> data,View view,ApiListener listener){

	}
	
	/**
	 * 预支付
	 * @param payType
	 * @param orderId
	 * @param listener
	 */
	public void prePay(int payType,String orderId,View view,ApiListener listener){
		
	}
	
	/**
	 * 获取订单信息
	 * @param orderId
	 * @param info
	 * @param listener
	 */
	public void getOrderInfo(String orderId,Object info,boolean showWaiting,ApiListener listener){
		
	}
}
