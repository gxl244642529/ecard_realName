package com.citywithincity.ecard.interfaces;

public interface IDoudouBus {
	void refresh();//刷新
	void reserve();//反向刷新
	void setRefreshInterval(int seconds);
	void stop();
	void setOnlyShowMyBus(boolean isChecked);
}
