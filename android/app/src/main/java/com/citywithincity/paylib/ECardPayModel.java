package com.citywithincity.paylib;

import android.app.Activity;

import com.citywithincity.auto.tools.Notifier;
import com.citywithincity.interfaces.IDestroyable;
import com.citywithincity.models.LocalData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ECardPayModel implements IDestroyable, IPay {


	public static String WEIXIN_APPID = "";

	private int id;
	private PayType[] payTypes;
	private CashierInfo info;
	private IPayAction action;
	private AbsPay pay;
	private String orderId;
	
	@Override
	public void setId(int id) {
		this.id = id;
	}

	@Override
	public void destroy() {
		if (pay != null) {
			pay.destroy();
		}
		payTypes = null;
		info = null;
		action = null;
		orderInfo = null;
		supportPayTypeInfos = null;
	}

	@Override
	public void setPayTypes(PayType[] infos) {
		this.payTypes = infos;
	}

	@Override
	public void setCashierInfo(CashierInfo info) {
		this.info = info;
	}

	@Override
	public CashierInfo getCashierInfo() {
		return info;
	}

	@Override
	public PayType[] getPayTypes() {
		return payTypes;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setPayAction(IPayAction payAction) {
		this.action = payAction;
	}
	
	@Override
	public IPayAction getPayAction() {
		return action;
	}

	@Override
	public void prePay(Activity context) {
		PayType payType = supportPayTypeInfos.get(currentIndex).type;
		if (pay != null) {
			pay.destroy();
		}
		orderInfo = null;
		switch (payType) {
		case PAY_ALIPAY:
			pay = new AliPay(context, this, action);
			break;
		case PAY_ETONGKA:
			pay = new ECardPay(context, this);
			break;
		case PAY_WEIXIN:
			pay = new WXPay(context, this, WEIXIN_APPID, action);
			break;
		default:
			break;
		}
		action.prePay(pay.getType(), info.getOrderID(), pay);
	}

	// 最终返回结果
	private Object payResult;

	void onPaySuccess(PayType type, Object result) {
		payResult = result;
		Notifier.notifyObservers(IPay.PAY_SUCCESS, result);
	}

	/**
	 * 支付错误
	 * 
	 * @param errorCode
	 * @param errorMessage
	 */
	void onPayError(int errorCode, String errorMessage) {
		Notifier.notifyObservers(IPay.PAY_ERROR, this.id, errorCode,
				errorMessage);
	}

	public void onPayCancel() {
		Notifier.notifyObservers(IPay.PAY_CANCEL);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends AbsPay> T getPay() {
		return (T) pay;
	}

	/**
	 * 有系统调用
	 */
	void setOrderInfo(OrderInfo orderInfo) {
		this.orderInfo = orderInfo;
	}

	private OrderInfo orderInfo;

	/**
	 * 获取订单实际信息，这个方法只能在订单预支付成功以后调用
	 * 
	 * @return
	 */
	public OrderInfo getOrderInfo() {
		return orderInfo;
	}

	private List<PayTypeInfo> supportPayTypeInfos;
	private int currentIndex;

	@Override
	public List<PayTypeInfo> getSupportPayTypes(PayTypeInfo[] allPayTypes) {
		// 获取
		int defaultType = LocalData.read().getInt("pay_for_default_" + id,
				payTypes[0].value());
		int index = 0;
		currentIndex = 0;

		HashSet<Integer> set = new HashSet<Integer>();
		for (PayType i : payTypes) {
			set.add(i.value());
		}
		List<PayTypeInfo> datas = new ArrayList<PayTypeInfo>();

		for (PayTypeInfo info : allPayTypes) {
			if (set.contains(info.type.value())) {
				datas.add(info);

				if (info.type.value() == defaultType) {
					currentIndex = index;
					info.selected = true;
				} else {
					info.selected = false;
				}
				index++;
			}
		}
		supportPayTypeInfos = datas;
		return datas;
	}

	@Override
	public boolean setCurrentPayIndex(int index) {
		if (index != currentIndex) {
			PayTypeInfo data = supportPayTypeInfos.get(index);
			data.selected = true;
			supportPayTypeInfos.get(currentIndex).selected = false;
			currentIndex = index;
			LocalData.write()
					.putInt("pay_for_default_" + id, data.type.value())
					.destroy();
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getPayResult() {
		return (T) payResult;
	}
	

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
}
