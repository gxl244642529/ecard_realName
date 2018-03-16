package com.damai.core;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.models.cache.CachePolicy;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.MessageUtil;
import com.citywithincity.utils.MessageUtil.IMessageListener;
import com.damai.auto.LifeManager;
import com.damai.core.ApiNetwork.Response;
import com.damai.dmlib.DMException;
import com.damai.dmlib.LibBuildConfig;
import com.damai.lib.R;
public abstract class ECardApiServerHandler extends ApiServerHandler  implements  IMessageListener, DialogListener, LoginListener{
	public static final class RequestInfo{
		public RequestInfo(byte[] data) {
			this.data = data;
		}
		public byte[] data;
		public Map<String, String> headers;
	}

	protected final Context context;
	// 服务器错误
	public static final int ErrCode_Service = 1;
	// 服务器端数据库操作失败
	public static final int ErrCode_Database = 2;
	// 用户没有操作这个接口的权限
	public static final int ErrCode_NoRight = 3;
	// 客户端提交的参数错误
	public static final int ErrCode_Param = 4;
	// 客户端提交的参数错误
	public static final int ErrCode_Json_Format = 5;
	// 客户端提交的参数错误
	public static final int ErrCode_Null_Value = 6;
	// 用户没有登录，但是本接口要求用户登录
	public static final int ErrCode_NotLogin = 7;
	// 客户端提交的数据版本与服务器端的数据版本一致
	public static final int ErrCode_SameVersion = 8;
	// 签名失败，需要下载客户端密钥
	public static final int ErrCode_RequireAccessToken = 9;
	// 签名失败
	public static final int ErrCode_SignError = 10;
	// 错误地址
	public static final int ErrCode_UrlError = 11;
	// 商户id错误
	public static final int ErrCode_BusinessID = 12;
	// 商户状态错误
	public static final int ErrCode_BusinessStatus = 13;
	// 请求时间和服务器时间相差太多(5分钟)
	public static final int ErrCode_RequestTime = 14;


	public static final int DEFAULT_TIME_OUT = 3000;

	protected ApiParser[] parsers;




	public ECardApiServerHandler(Context context) {
		this.context = context;
		parsers = new ApiParser[DataTypes.DataType_Max];
		parsers[DataTypes.DataType_ApiArray] = getListParser();
		parsers[DataTypes.DataType_ApiPage] = getPageParser();
		parsers[DataTypes.DataType_ApiObject] = getObjectParser();
		//	DataTypes.DataType_ApiArray;
	}


	private String url;
	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public void setUrl(String url) {
		this.url = url;
	}

	private int server;

	public void setServer(int server){
		this.server = server;
	}

	public int getServer(){
		return this.server;
	}

	/**
	 * 获取任务地址
	 *
	 * @param api
	 * @return
	 */
	protected abstract String getUrl(String api);


	@Override
	protected void handleJobImpl(ApiJobImpl job) {
		handleAndParse(job, 0);
	}

	protected Response execute(Map<String, Object> params,int timeoutMs,String url,int crypt) throws IOException,NoAccessTokenError{
		if(LibBuildConfig.DEBUG){
			System.out.println("Start post "+url);
		}
		RequestInfo info = encodeRequest(params,crypt);
		Response data = network.execute(timeoutMs, url,info.headers,info.data);
		return data;
	}

	protected void handleAndParse(ApiJobImpl job, int lastFlag) {
		try {
			Response data = null;
			try{
				data = execute(job.params,job.timeoutMs, getUrl(job.getApi()),job.crypt);
			}catch(NoAccessTokenError e){
				doGetAccessToken(job);
				try{
					data = execute(job.params,job.timeoutMs, getUrl(job.getApi()),job.crypt);
				}catch(NoAccessTokenError e1){
					throw new IOException("获取accessToken失败");
				}

			}
			if(job.isCanceled()){
				return;
			}
			parse(data.data, job, lastFlag);
		} catch (Exception e) {
			if (job.isCanceled()) {
				return;
			}
			job.setError(new DMError(e));
			listener.onJobError(job);
		}
	}

	protected void parse(byte[] bytes, ApiJobImpl job, int lastFlag)
			throws JSONException, IOException ,DMException{
		String result = new String(bytes);
		if(LibBuildConfig.DEBUG){
			System.out.println(result);
		}
		JSONObject json = new JSONObject(result);
		int flag = json.getInt("flag");
		if (flag == 0) {
			Object data = parsers[job.dataType].parse(json, job.entity);
			job.data = data;
			if(job.getCachePolicy()!=CachePolicy.CachePolity_NoCache){
				//写入缓存
				cache.put(job.getCacheKey(), bytes);
			}
			listener.onJobSuccess(job);

		} else {
			if (flag < 0) {
				String info = json.getString("result");
				DMMessage message = null ;
				if(flag==-1){
					message = new DMMessage(info, null, DMMessage.ALERT);
				}else{
					message = new DMMessage(info, null, DMMessage.TOAST);
				}
				job.setMessage(message);
				messageListener.onApiMessage(job);
			} else if (flag == ErrCode_NotLogin) {
				if(lastFlag == ErrCode_NotLogin){
					//这个时候需要报错
					throw new DMException("自动登录失败");
				}
				setPaddingJob(job);
				// 需要登录
				try {
					if (doLogin(job)) {
						// 这个时候应该再次执行一次
						handleAndParse(job, ErrCode_NotLogin);
					}
				} catch (NoAccessTokenError e) {
					//在登录的时候accessToken失效
					try{
						doGetAccessToken(job);
						if (doLogin(job)) {
							// 这个时候应该再次执行一次
							handleAndParse(job, ErrCode_NotLogin);
						}
					}catch(NoAccessTokenError e1){
						throw new IOException("登录的时候accesstoken失效,再次获取token失败");
					}catch(NotLoginError e1){
						//这里需要调出login对话框
						DMAccount.callLoginActivity(this);
					}

				}
			} else if(flag == ErrCode_RequireAccessToken) {
				if(lastFlag == ErrCode_RequireAccessToken){
					//这个时候需要报错
					throw new DMException("获取token失败");
				}
				// 需要登录
				try {
					if (doGetAccessToken(job)) {
						// 这个时候应该再次执行一次
						handleAndParse(job, ErrCode_RequireAccessToken);
					}
				} catch (NotLoginError notLoginError) {
					try {
						if (doLogin(job)) {
							// 这个时候应该再次执行一次
							handleAndParse(job, ErrCode_NotLogin);
						}
					} catch (NoAccessTokenError noAccessTokenError) {
						throw new IOException("登录的时候accesstoken失效,再次获取token失败");
					}
				}
			}else{
				if(flag==1){
					// 其他错误
					throw new DMException("服务器异常，请稍后重试");
				}
				// 其他错误
				throw new DMException(String.format("出现异常错误 flag: [%d]",
						flag));
			}
		}

	}

	protected abstract boolean doGetAccessToken(ApiJobImpl job) throws IOException,NotLoginError;


	public static class NoAccessTokenError extends Exception{

		/**
		 *
		 */
		private static final long serialVersionUID = -4036547925793580250L;

	}

	public static class NotLoginError extends  Exception{

	}

	protected abstract RequestInfo encodeRequest(Map<String, Object> params,int crypt) throws IOException,NoAccessTokenError;

	private WeakReference<ApiJobImpl> paddingJob;

	/**
	 *
	 * @param job
	 * @throws IOException
	 */
	protected abstract boolean doLogin(ApiJobImpl job) throws IOException ,NoAccessTokenError;

	@Override
	public void onMessage(int message) {
		Alert.alert(LifeManager.getActivity(), "温馨提示", "您的登录信息已经过期，请重新登录", this);
	}

	@Override
	public void onDialogButton(int id) {
		if (R.id._id_ok == id) {
			DMAccount.callLoginActivity( this);
		}
	}


	@Override
	public ApiParser getListParser() {
		return new ApiArrayParser();
	}

	@Override
	public ApiParser getPageParser() {
		return new ApiPageParser();
	}

	@Override
	public ApiParser getObjectParser() {
		return new ApiObjectParser();
	}

	@Override
	public void onLoginSuccess() {
		//登录成功之后
		if(getPaddingJob()!=null && !getPaddingJob().isCanceled()){
			getPaddingJob().execute();
		}
		setPaddingJob(null);
		LoginListenerWrap.getInstance().setListener(null);
	}

	@Override
	public void onLoginCancel() {
		setPaddingJob(null);
		LoginListenerWrap.getInstance().setListener(null);
	}

	public void clearSession() {
		network.clearSession();
	}

	public ApiJobImpl getPaddingJob() {
		return paddingJob!=null ? paddingJob.get() : null;
	}

	public void setPaddingJob(ApiJobImpl paddingJob) {
		this.paddingJob = new WeakReference<ApiJobImpl>(paddingJob);
	}


	@SuppressWarnings("unchecked")
	protected <T> T tryParse(byte[] data,int lastFlag) throws IOException,NotLoginError{
		try {
			JSONObject json = new JSONObject(new String(data));
			int flag = json.getInt("flag");
			if (flag == 0) {
				return (T)json.get("result");
			}else if(flag==7){
				throw new NotLoginError();
			}
			if (flag == lastFlag) {
				throw new IOException("循环错误");
			}

			return null;
		} catch (JSONException e) {
			// 登录是否发生错误，应该是服务器错误
			throw new IOException("登录数据解析错误");
		}
	}


	protected void onLoginError(ApiJobImpl job) {
		setPaddingJob(job);
		MessageUtil.sendMessage(this);

	}



}
