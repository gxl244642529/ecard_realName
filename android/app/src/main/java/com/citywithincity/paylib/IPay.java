package com.citywithincity.paylib;

import android.annotation.SuppressLint;
import android.app.Activity;

import java.util.List;

public interface IPay {
	
	String PAY_SUCCESS = "IPay.pay_success";
	String PAY_ERROR = "IPay.pay_error";
	String PAY_CANCEL = "IPay.pay_cancel";
	/**
	 * 实际支付订单信息
	 * @author Randy
	 *
	 */
	@SuppressLint("DefaultLocale")
	class OrderInfo{
		//支付订单标题
		String title;
		//支付金额
		double fee;
		//外部id
		String outId;
		//各个平台分配的订单实际id(支付宝订单id)
		String platId;
		
		public String getTitle() {
			return title;
		}
		public double getFee() {
			return fee;
		}
		public String getOutId() {
			return outId;
		}
		public String getPlatId() {
			return platId;
		}
		public String getFormatedPrice() {
			return String.format("%.2f", fee);
		}
		
	}
	
	
	/**
	 * 收银台需要显示的信息，
	 * 一般来说有需要支付的金额，和订单id
	 * 如有其它属性，请继承这个类
	 * @author Randy
	 *
	 */
	@SuppressLint("DefaultLocale")
	class CashierInfo{
		
		private double fee;
		private String orderID;
		private Object extraInfo;
		
		/**
		 * 
		 * @param orderID
		 * @param fee
		 */
		public CashierInfo(String orderID,double fee){
			this.orderID = orderID;
			this.fee = fee;
		}

		public double getFee() {
			return fee;
		}


		public String getOrderID() {
			return orderID;
		}
		public String getFormatedPrice() {
			return String.format("%.2f", fee);
		}

		public <T> T getExtraInfo() {
			return (T)extraInfo;
		}

		public void setExtraInfo(Object extraInfo) {
			this.extraInfo = extraInfo;
		}
	}
	
	/**
	 * 设置支付id,用来标识支付,对于系统中的每一种支付，都应该有唯一id
	 */
	void setId(int id);
	
	
	/**
	 * 设置收银台所支持的类型
	 */
	void setPayTypes(PayType[] infos);
	
	/**
	 * 设置收银台需要显示的信息
	 */
	void setCashierInfo(CashierInfo info);
	
	/**
	 * 获取收银台信息
	 * @return
	 */
	CashierInfo getCashierInfo();
	
	/**
	 * 获取收银台支付方式
	 * @return
	 */
	PayType[] getPayTypes();
	
	List<PayTypeInfo> getSupportPayTypes(PayTypeInfo[] allPayTypes) ;
	
	boolean setCurrentPayIndex(int index);
	
	<T> T getPayResult();
	/**
	 * id
	 * @return
	 */
	int getId();
	
	/**
	 * 预支付，
	 * 第一步，首先调用服务器端的预支付，生成签名信息
	 * 当预支付成功以后，各种支付方式有各种不同的实现
	 */
	void prePay(Activity context);
	
	void setPayAction(IPayAction payAction);
	
	IPayAction getPayAction();
	
	<T extends AbsPay> T  getPay();
	
	
}
