package com.citywithincity.ecard.interfaces;

import com.citywithincity.ecard.models.vos.CheckVersionResult;
import com.citywithincity.interfaces.IJsonTaskManager;
import com.citywithincity.interfaces.IRequestResult;

public interface IECardJsonTaskManager extends IJsonTaskManager {
	
	
	
	int NET_TYPE_NULL=0;
	int NET_TYPE_3G=1;
	int NET_TYPE_WIFI=2;
	//全局枚举
	int COUPON=1;
	int BUSINESS=2;

	
	
	/**
	 * 定义常量
	 */
	
	//登录接口
//	public static final String LOGIN="login3";
	String LOGIN="pass_api/login";
	//注册接口
	String REGISTER="register3";
	//绑定e通卡
	String BIND="ecard_bind";
	
	
	String BUSINESS_DETAIL="business_detail2";
	
	//商家列表
	String BUSINESS_LIST="bs_discount";
	
	
	
	
	String VERSION = "android_version";
	
	
	
	String MY_BUSINESS_LIST="fbusiness_list";
	String MY_COUPON_LIMIT="my_coupon_list";
	
	String MY_COUPON_DETAIL="my_coupon_detail";
	
	

	
	
	
	String UNBIND_ECARD="ecard_unbind";
	
	
	
	
	/**
	 * 游戏相关api
	 */
	String game_scence = "game_scence";
	
	

	

	void getVersionInfo(IRequestResult<CheckVersionResult> listener);
	
	void updatePushID(String registrationID,IRequestResult<Boolean> iRequestResult);
	
}
