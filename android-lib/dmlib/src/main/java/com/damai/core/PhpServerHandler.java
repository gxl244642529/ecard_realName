package com.damai.core;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Context;

import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.utils.MessageUtil;
import com.citywithincity.utils.MessageUtil.IMessageListener;
import com.citywithincity.utils.PackageUtil;
import com.damai.auto.LifeManager;
import com.damai.core.ApiNetwork.Response;
import com.damai.dmlib.LibBuildConfig;
import com.damai.push.Push;
import com.damai.util.JsonUtil;

public class PhpServerHandler extends ECardApiServerHandler implements
		IMessageListener, DialogListener, LoginListener {

	public PhpServerHandler(Context context) {
		super(context);
	}

	@Override
	protected String getUrl(String api) {
		return new StringBuilder(LibBuildConfig.MAX_API_LEN)
				.append(DMServers.getUrl(0)).append("/index.php/api2/").append(api)
				.toString();
	}

	protected static final String CONTENT = "content=";


	@Override
	public int positionToPageParam(int position) {
		return position+1;
	}

	/**
	 * 如果是oracle服务器的话
	 */
	@Override
	public void setPosition(int postion, Map<String, Object> param) {
		param.put("position", postion + 1);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public DMPage<?> createPage() {
		return new DMPage();
	}


	@Override
	protected RequestInfo encodeRequest(Map<String, Object> params, int crypt)
			throws IOException {
		params.put("deviceID", Push.getUdid());
		StringBuilder sb = new StringBuilder(CONTENT);
		// 获取参数
		JsonUtil.mapToJson(sb, params);
		String send = sb.toString();
		if(LibBuildConfig.DEBUG){
			System.out.print(send);
		}

		return new RequestInfo(send.getBytes("UTF-8"));
	}

	@Override
	protected boolean doGetAccessToken(ApiJobImpl job) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean doLogin(ApiJobImpl job) throws IOException {

		DMAccount account = DMAccount.get();

		Map<String, Object> params = new HashMap<String, Object>();

		params.put("deviceID", Push.getUdid());
		params.put("pushID", DMLib.getJobManager().getPushId());
		params.put("platform", "iphone");

		params.put("version", PackageUtil.getVersionCode(LifeManager.getActivity()));

		params.put("username",account.getAccount() );
		params.put("pwd", account.getPassword());
		/**
		 * 这里自动登录
		 *
		 */
		Response data = null;
		try {
			data = execute(params,DEFAULT_TIME_OUT, getUrl("pass_api/login"),Crypt.NONE);
		} catch (NoAccessTokenError e) {
			throw new IOException("需要accesstoken");
		}
		JSONObject result = null;
		try {
			result = tryParse(data.data, ErrCode_NotLogin);
		} catch (NotLoginError notLoginError) {

		}
		if(result==null){
			// 发生其他错误，则应该是登录失败，表示登录信息过期了,这个时候应该要求用户登录了
			MessageUtil.sendMessage(0, this);
			return false;
		}
		return true;
	}





}
