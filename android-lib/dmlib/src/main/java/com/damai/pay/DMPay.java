package com.damai.pay;

import com.citywithincity.interfaces.IDestroyable;
import com.damai.core.ApiListener;

public interface DMPay extends ApiListener,IDestroyable {
	String getTitle();
	int getIcon();
	int getPayType();
}
