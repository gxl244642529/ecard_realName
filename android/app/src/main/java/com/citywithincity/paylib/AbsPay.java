package com.citywithincity.paylib;

import android.app.Activity;

import com.citywithincity.interfaces.IDestroyable;

public abstract class AbsPay implements IDestroyable, IPayActionListener {
	PayType payType;
	Activity mContext;
	ECardPayModel listener;
	
	public AbsPay(Activity context,ECardPayModel payListener){
		mContext = context;
		this.listener = payListener;
	}

	public PayType getType() {
		return payType;
	}

	@Override
	public void destroy(){
		mContext = null;
		listener = null;
	}
}
