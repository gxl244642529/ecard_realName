package com.citywithincity.ecard.models;

import android.app.Activity;
import android.content.Context;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.interfaces.IECardJsonTaskManager;
import com.citywithincity.ecard.models.vos.CheckVersionResult;
import com.citywithincity.ecard.models.vos.ECardUserInfo;
import com.citywithincity.interfaces.IJsonTask;
import com.citywithincity.interfaces.IObjectJsonTask;
import com.citywithincity.interfaces.IRequestResult;
import com.citywithincity.interfaces.IValueJsonTask;
import com.citywithincity.models.cache.CachePolicy;
import com.citywithincity.models.http.AbsJsonTask;
import com.citywithincity.models.http.JsonTaskManager;
import com.citywithincity.utils.PackageUtil;
import com.damai.core.DMAccount;
import com.damai.push.Push;

import org.json.JSONException;
import org.json.JSONObject;

public class ECardJsonManager extends JsonTaskManager
		implements IECardJsonTaskManager {

	public static IECardJsonTaskManager getInstance() {
		if (instance == null) {
			synchronized (ECardJsonManager.class) {
				instance = new ECardJsonManager();
			}
		}
		return (IECardJsonTaskManager) instance;
	}
	
	


	
	protected ECardJsonManager() {
		setDefaultImageResId(R.drawable.default_coupon_logo);
		setUserInfoClass(ECardUserInfo.class);
	}
	
	@Override
	public void getVersionInfo(
			IRequestResult<CheckVersionResult> listener) {
		// 默认无缓存
		IObjectJsonTask<CheckVersionResult> task = createObjectJsonTask(
				VERSION, CachePolicy.CachePolity_NoCache,CheckVersionResult.class);
		Context appContext = application.getApplicationContext();
		task.setListener(listener);
		task.put("channel",PackageUtil.getMetaValue(appContext, "UMENG_CHANNEL"));
		task.put("version", PackageUtil.getVersionCode(appContext));
		task.execute();
	}



	@Override
	public void updatePushID(String registrationID,IRequestResult<Boolean> iRequestResult) {
		if(getCurrentContext()==null){
			return;
		}
		IValueJsonTask<Boolean> task = createBooleanJsonTask("update_push_id");
		task.setListener(iRequestResult);
		task.put("pushID", registrationID);
		task.execute();
	}
	


	@Override
	protected void callLoginActivity(Activity context) {
		DMAccount.callLoginActivity(context, null);
	}

	@Override
	protected String getLoginApi() {
		return LOGIN;
	}

	@Override
	public String getPushID(){
		return Push.getPushId();
	}

	@Override
	public void clearSession() {

	}


	@Override
	protected void onBeforeLogin(IJsonTask loginTask, DMAccount userInfo) {
		loginTask.put("username", userInfo.getAccount());
		loginTask.put("pwd", userInfo.getPassword());
		loginTask.put("platform", "android");
		loginTask.put("version", PackageUtil.getVersionCode(application.getApplicationContext()));
		loginTask.put("channel", PackageUtil.getMetaValue(application.getApplicationContext(), "UMENG_CHANNEL"));
	}

	@Override
	protected void onParseUserInfo(DMAccount userInfo, JSONObject json)
			throws JSONException {

	}


	@Override
	public void onRequireAccessToken(AbsJsonTask<?> task) {

	}
}
