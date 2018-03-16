package com.citywithincity.ecard.enums;

import android.app.Activity;
import android.content.Context;

/**
 * 定义所有系统调用action
 * @author Randy
 *
 */
public class Actions {

	public static void init(Context context){
		PUSH_BIND =context.getPackageName() + ".action.PUSH_BIND";
		SELECT_ECARD =context.getPackageName() + ".action.SELECT_ECARD";
		LOGIN = context.getPackageName() + ".action.SELECT_ECARD";
	}

	//receiver
	public static final String UseCoupon = "com.citywithincity.ecard.UseMyCoupon";
	
	
	//注册请求
	public static final String REGISTER = "com.citywithincity.ecard.action.REGISTER";
	
	//绑定e通卡请求
	public static final String BIND_ECARD = "com.citywithincity.ecard.action.BIND_ECARD";  
	
	//登录请求
	public static String LOGIN;
	
	
	public static String PUSH_BIND;
	
	/**
	 * 选择我的e通卡
	 */
	public static String SELECT_ECARD;
	
	//定义请求码
	
	//注册
	public static final int REQUEST_CODE_REGISTER = 1;
	
	//登录
	public static final int REQUEST_CODE_LOGIN= 3;
	//送礼
	public static final int REQUEST_CODE_SEND_GIFT = 4;
	
	
	//添加
	public static final int REQUEST_CODE_GOOD_INFO_ADD = 5;
	
	
	//绑定e通卡
	public static final int REQUEST_CODE_BIND_ECARD = 2;
	
	//绑定会员卡
	public static final int REQUEST_CODE_MEMBER = 10;
	
	//解除绑定或者修改
	public static final int REQUEST_CODE_UPDATE_ECARD=6;
	
	
	//改名字
	public static final int RESULT_CODE_UPDATE_ECARD = Activity.RESULT_OK + 1;
	//解除绑定
	public static final int RESULT_CODE_UNBIND_ECARD = RESULT_CODE_UPDATE_ECARD + 1;
	
	
	
	//选择旅游版还是普通盘
	public static final int REQUEST_CODE_SELECT = 7;
	
}
