package com.citywithincity.ecard.models;

import com.citywithincity.auto.ApiValue;
import com.citywithincity.interfaces.IRequestResult;

/**
 * 
 * @author Randy
 *
 */
public interface PassportModel {
	
//	public static final String UPDATE_PASSWORD = "update_password3";
String UPDATE_PASSWORD = "user_api/update_pass";
	
	/**
	 * 修改密码
	 * @param oldPwd
	 * @param newPwd
	 */
	@ApiValue(api=UPDATE_PASSWORD,params={"old_pass","new_pass"},waitingMessage="正在修改密码...")
	void updatePassword(String oldPwd,String newPwd);

	/**
	 * 注册第一步
	 * @param username
	 * @param pwd
	 */
	void tryRegister(String username,String pwd,IRequestResult<Object> listener);


	
}
