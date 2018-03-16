package com.damai.pay;

import com.citywithincity.interfaces.IDestroyable;
import com.damai.core.ApiListener;

public abstract class AbsDMPay implements DMPay, ApiListener,IDestroyable {

	private DMPayModelImpl model;
	
	public abstract boolean checkInstalled();
	
	public abstract int getPayType() ;
	
	
	public DMPayModelImpl getModel() {
		return model;
	}
	public void setModel(DMPayModelImpl model) {
		this.model = model;
	}
	
	
	@Override
	public void destroy(){
		model = null;
	}
	public abstract String getTitle() ;
	
	
	public abstract int getIcon() ;
	
	
}
