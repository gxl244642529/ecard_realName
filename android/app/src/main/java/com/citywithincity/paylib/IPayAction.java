package com.citywithincity.paylib;

public interface IPayAction {
	
	
	/**
	 * 预支付
	 * @param payType
	 * @param orderID
	 */
	void prePay(PayType payType,String orderID,IPayActionListener listener);
	
	/**
	 * 通知服务器成功了
	 * @param payType
	 * @param info
	 */
	void notifyServer(PayType payType,Object info,IPayActionListener listener);
}
