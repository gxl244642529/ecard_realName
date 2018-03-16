package com.citywithincity.ecard.user;

import com.citywithincity.interfaces.IRequestResult;
import com.citywithincity.models.http.JsonTaskManager;
import com.citywithincity.utils.FileUtil;
import com.citywithincity.utils.PackageUtil;
import com.citywithincity.utils.ThreadUtil;

import java.io.File;

public class UserModel {
	// 直接注册
	public static final String REGISTER = "pass_api/register";

	public static final String NICK_NAME = "user_api/set_nick_name";
	// 设置头像
	public static final String HEAD_IMG = "user_api/set_head_img";
	// 设置壁纸
	public static final String HEAD_BG = "user_api/set_bg";

	public static final String REGISTER_SUCCESS = "register_success";

	// 绑定手机发送验证码
	public static final String BIND_PHONE_CHECK = "bind_phone_check";

	// 绑定手机
	public static final String BIND_PHONE = "bind_phone";

	// 获取绑定的手机
	public static final String GET_BIND_PHONE = "get_bind_phone";

	// 原手机检测
	public static final String BIND_PHONE_ORG_CHECK = "bind_phone_org_check";

	// 原手机验证
	public static final String BIND_CHANGE_VERIFY = "bind_change_verify";

	// 新手机检测
	public static final String BIND_CHANGE_PHONE_CHECK = "bind_change_phone_check";

	// 帮顶新手机
	public static final String bIND_CHANGE_PHONE = "bind_change_phone";

	/**
	 * 设置昵称
	 * 
	 * @param name
	 */
	public void setNickName(String name) {
		JsonTaskManager.getInstance().createValueJsonTask(NICK_NAME)
				.enableAutoNotify().put("name", name).setWaitMessage("请稍等...")
				.execute();
	}

	/**
	 * 上传头像
	 * 
	 * @param path
	 */
	public void setHeadImage(final String path) {
		// 这里用
		ThreadUtil.run(new Runnable() {
			@Override
			public void run() {
				JsonTaskManager.getInstance().createValueJsonTask(HEAD_IMG)
						.enableAutoNotify()
						.put("img", FileUtil.toBase64(new File(path)))
						.execute();
			}
		});

	}

	/**
	 * 上传壁纸
	 * 
	 * @param path
	 */
	public void setHeadBG(final String path) {
		// 这里用
		ThreadUtil.run(new Runnable() {
			@Override
			public void run() {
				JsonTaskManager.getInstance().createValueJsonTask(HEAD_BG)
						.enableAutoNotify()
						.put("img", FileUtil.toBase64(new File(path)))
						.execute();
			}
		});
	}

	/**
	 * 直接注册
	 * 
	 * @param userName
	 * @param pwd
	 *            'username','pwd','pushID','platform','version' 用户名 密码（md5）
	 *            推送id 平台 版本号
	 * 
	 *            返回： array('userid'=>$user_id,'phone'=>null);
	 */
	public void register(String userName, String pwd) {
		JsonTaskManager
				.getInstance()
				.createValueJsonTask(REGISTER)
				.enableAutoNotify()
				.put("username", userName)
				.put("pwd", pwd)
				.put("pushID", JsonTaskManager.getInstance().getPushID())
				.put("platform", "android")
				.put("version",
						PackageUtil.getVersionCode(JsonTaskManager
								.getApplicationContext()))
				.setWaitMessage("正在注册……").execute();
	}

	/**
	 * 绑定手机发送验证码
	 * 
	 * @param phone
	 */
	public void bindPhoneCheck(String phone, IRequestResult<Integer> listener) {
		JsonTaskManager.getInstance().createIntegerJsonTask(BIND_PHONE_CHECK)
				.setListener(listener).enableAutoNotify().put("phone", phone)
				.setWaitMessage("获取验证码……").execute();
	}

	/**
	 * 绑定手机
	 * 
	 * @param phone
	 */
	public void bindPhone(int verifyId, String code,
			IRequestResult<Object> listener) {
		JsonTaskManager.getInstance().createValueJsonTask(BIND_PHONE)
				.setListener(listener).enableAutoNotify()
				.put("verify_id", verifyId)// 上面api返回的结果
				.put("code", code)// 短信验证码
				.setWaitMessage("正在绑定手机……").execute();
	}

	/**
	 * 获取绑定的手机
	 */
	public void getBindPhone() {
		JsonTaskManager.getInstance().createValueJsonTask(GET_BIND_PHONE)
				.enableAutoNotify().execute();
	}

	/**
	 * 原手机检测
	 */
	public void bindPhoneOrgCheck(IRequestResult<Integer> listener) {
		JsonTaskManager.getInstance()
				.createIntegerJsonTask(BIND_PHONE_ORG_CHECK)
				.setListener(listener).enableAutoNotify().execute();
	}

	/**
	 * 原手机验证
	 * 
	 * @param verifyId
	 * @param code
	 */
	public void bindChangeVerify(int verifyId, String code,IRequestResult<Object> listener) {
		JsonTaskManager.getInstance().createValueJsonTask(BIND_CHANGE_VERIFY)
		.setListener(listener)
				.put("code", code).put("verify_id", verifyId)
				.enableAutoNotify().setWaitMessage("正在验证手机……").execute();
	}

	/**
	 * 新手机检测
	 * 
	 * @param phone
	 */
	public void bindChangePhoneCheck(String phone,IRequestResult<Integer> listener) {
		JsonTaskManager.getInstance()
				.createIntegerJsonTask(BIND_CHANGE_PHONE_CHECK)
				.setListener(listener)
				.put("phone", phone).enableAutoNotify()
				.setWaitMessage("正在获取验证码……").execute();
	}

	/**
	 * 绑定新手机
	 * 
	 * @param verifyId
	 * @param code
	 */
	public void bindChangePhone(int verifyId, String code,IRequestResult<Object> listener) {
		JsonTaskManager.getInstance().createValueJsonTask(bIND_CHANGE_PHONE)
		.setListener(listener)
				.put("code", code)
				.put("verify_id", verifyId)
				.enableAutoNotify().setWaitMessage("正在绑定新手机……").execute();
	}

}
