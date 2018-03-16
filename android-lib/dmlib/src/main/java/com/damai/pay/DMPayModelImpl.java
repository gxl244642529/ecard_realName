package com.damai.pay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.citywithincity.interfaces.IDestroyable;
import com.citywithincity.models.http.ObjectJsonTask;
import com.damai.auto.LifeManager;
import com.damai.core.ApiJob;
import com.damai.core.ApiListener;
import com.damai.core.DMError;
import com.damai.core.DMLib;
import com.damai.core.DMMessage;
import com.damai.helper.DMAdapter;
import com.damai.interfaces.IAdapterListener;

/**
 * 对于支付，需要知道支付的金额和支付的订单id号，以及其他额外信息 一、配置所有支付类型，在程序一开始 二、配置本次支付的类型
 * 
 * 
 * @author renxueliang
 * 
 */
public class DMPayModelImpl implements DMPayModel, OnItemClickListener,
		IDestroyable, ApiListener {
	private String orderId;
	private int fee;
	private List<DMPay> pays;
	private String businessId;
	private DMPayAction action;
	private DMPayListener listener;

	/**
	 * 雷场
	 */
	private DMPayFactory factory;
	private int currentIndex;
	
	public DMPayModelImpl() {
		DMLib.setPayModel(this);
		currentIndex = 0;
	}

	public void setListView(final ListView listView, int itemResId,
			IAdapterListener<DMPay> listener) {
		DMAdapter<DMPay> adapter = createAdapter(listView.getContext(),itemResId, listener);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		adapter.setData(pays);
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		listView.setItemChecked(currentIndex, true);

	}



	/**
	 * 创建array adaper
	 */
	public DMAdapter<DMPay> createAdapter(Context context, int itemResId,
			IAdapterListener<DMPay> listener) {
		return new DMAdapter<DMPay>(context, itemResId, listener);
	}

	@SuppressLint("DefaultLocale")
	public String getFormatFee() {
		return String.format("%.02f", (float) fee / 100);
	}

	/**
	 * 设置支持的类型
	 */
	public void setSupportPayTypes(int[] types) {
		pays = new ArrayList<DMPay>(types.length);
		DMPayFactory factory = this.factory;
		for (int i : types) {
			AbsDMPay pay = factory.createPay(i);
			pay.setModel(this);
			pays.add(pay);
		}
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@Override
	public DMPayListener getPayListener() {
		return listener;
	}

	public int getFee() {
		return fee;
	}

	public void setFee(int fee) {
		this.fee = fee;
	}

	public void prePay(View button) {
		AbsDMPay pay = (AbsDMPay) pays.get(currentIndex);
		if(pay.checkInstalled()){
			action.prePay(pay.getPayType(), orderId, button, pay);
		}
		
	}

	@Override
	public void prePay(View view, String api, Map<String, Object> data) {
		AbsDMPay pay = (AbsDMPay) pays.get(currentIndex);
		if(pay.checkInstalled()){
			action.prePay(pay.getPayType(),api,data,view,pay);
		}
	}

    /**
     * 保护这个方法一席
     * @param info
     * @param showWaiting
     */
	private void getOrderInfo(Object info, boolean showWaiting) {
		action.getOrderInfo(getOrderId(), info, showWaiting, this);
	}

	public void setPayAction(DMPayAction action) {
		this.action = action;
	}

	@Override
	public void setIndex(int index) {
		this.currentIndex = index;
	}

	@SuppressWarnings("unchecked")
	public <T extends DMPay> T getPay() {
		return (T) pays.get(currentIndex);
	}

    @Override
    public void notifyUserCancel(int type) {
        if(listener!=null){
            listener.onPayCancel(this);
        }
    }


    public void notifyPayError(int type,String error,boolean isNetworkError){
        if(listener!=null){
            listener.onPayError(type,error,isNetworkError);
        }
    }
    /**
	 * 选中
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		currentIndex = position;
	}




	@Override
	public void destroy() {
		for (DMPay p : pays) {
			p.destroy();
		}
		pays.clear();
		pays = null;
		factory = null;
		DMLib.setPayModel(null);
		
	}

	public DMPayFactory getFactory() {
		return factory;
	}

	public void setFactory(DMPayFactory factory) {
		this.factory = factory;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	/**
	 * 整个交易成功
	 */
	@Override
	public void onJobSuccess(ApiJob job) {

		// 获取订单信息成功
		closeWXActivity();
        if(listener==null){
            return;
        }
		listener.onPaySuccess(this, job.getData());
	}

	/**
	 * 服务器通知，交易失败
	 */
	@Override
	public boolean onApiMessage(ApiJob job) {

		closeWXActivity();
        if(listener==null){
            return false;
        }
		DMMessage message = job.getMessage();
		return listener.onGetPayInfoError(this, message.getMessage(), false);
	}
	
	/**
	 * 交易失败,这里可以重新发一个请求
	 */
	@Override
	public boolean onJobError(ApiJob job) {
        if(listener==null){
            return false;
        }
		closeWXActivity();
		DMError error = job.getError();
		return listener.onGetPayInfoError(this, error.getReason(),error.isNetworkError());
	}

	public boolean notifyError(DMError error) {
        if(listener==null){
            return false;
        }
        return listener.onPrePayError(error.getReason(),error.getError() instanceof IOException);
	}



	public boolean notifyMessage(DMMessage error) {
        if(listener==null){
            return false;
        }
        return listener.onPrePayError(error.getMessage(),false);
	}

	private void closeWXActivity() {
		if (LifeManager.getActivity().getClass().getName()
				.endsWith("WXPayEntryActivity")) {
			LifeManager.getActivity().finish();
		}
	}

	

	public DMPayListener getListener() {
		return listener;
	}

	public void setListener(DMPayListener listener) {
		this.listener = listener;
	}

	@Override
	public void onUserCancel() {
        if(listener==null){
            return;
        }
		listener.onPayCancel(this);
	}

    /**
     * 成功了
     * @param payType
     * @param info
     */
	public void notifyPaySuccess(int payType, Object info,boolean showNotify) {
		if(listener==null){
			return;
		}
		if(listener.onClientPaySuccess(this,payType,info)){
            return;
        }
        getOrderInfo(info,showNotify);
	}
}
