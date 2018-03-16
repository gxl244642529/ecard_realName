package com.damai.pay;

public interface DMPayFactory {

	AbsDMPay createPay(int type);
}
