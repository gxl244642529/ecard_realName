package com.citywithincity.paylib;




public enum PayType {
	PAY_ALIPAY(1), PAY_WEIXIN(2), PAY_ETONGKA(3),PAY_UNION(4),PAY_CMB(5),PAY_UMA(6);

	private int value;

	PayType(int value) {
		this.value = value;
	}

	public int value() {
		return this.value;
	}

	public static PayType valueOf(int value) {
		switch (value) {
		case 1:
			return PAY_ALIPAY;
		case 2:
			return PAY_WEIXIN;
		case 3:
			return PAY_ETONGKA;
		case 4:
			return PAY_UNION;
		case 5:
			return PAY_UMA;
		default:
			return PAY_WEIXIN;
		}
	}

}

