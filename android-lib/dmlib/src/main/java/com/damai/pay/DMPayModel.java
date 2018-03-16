package com.damai.pay;

import android.view.View;
import android.widget.ListView;

import com.damai.interfaces.IAdapterListener;

import java.util.Map;

public interface DMPayModel {
	public String getOrderId();

	public void setOrderId(String orderId) ;

	DMPayListener getPayListener();

	public int getFee() ;

	public void setFee(int fee) ;
	
	String getFormatFee();
	void setListView(final ListView listView, int itemResId,
			IAdapterListener<DMPay> listener);


	void prePay(View view, String api, Map<String,Object> data);

	void prePay(View view);
	
	void setListener(DMPayListener listener);

	public void setPayAction(DMPayAction action);

	void setIndex(int index);

	void destroy();
	/**
	 * 用户点击取消
	 */
	void onUserCancel();
	<T extends DMPay> T getPay();
	void notifyUserCancel(int type);
	
}
