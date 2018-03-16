package com.citywithincity.models.http;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.citywithincity.auto.tools.Notifier;
import com.citywithincity.interfaces.IJsonTask;
import com.citywithincity.interfaces.IRequestError;
import com.citywithincity.libs.LibConfig;
import com.citywithincity.models.cache.CachePolicy;
import com.citywithincity.models.http.core.AbsRequest;
import com.citywithincity.utils.Alert;
import com.damai.core.ApiJob;
import com.damai.core.ApiListener;
import com.damai.core.DMLib;
//import com.citywithincity.utils.FileLogUtil;

public abstract class AbsJsonTask<T> extends AbsRequest<T> implements IJsonTask, ApiListener {

	protected static final String RESULT = "result";
	protected static final String FLAG = "flag";

	private boolean autoNotify;
	public IJsonTask enableAutoNotify(){
		autoNotify = true;
		return this;
	}
	public boolean isAutoNotify(){
		return autoNotify;
	}
	public void removeParam(String key){
	//	paramObject.remove(key);
        job.removeParam(key);
	}
	@Override
	public IJsonTask putAll(Map<String, Object> params){
        job.putAll(params);
		return this;
	}
	// 是否已经注册
	private boolean regsiterToLoginQueue;

	private boolean destroied;
	// 版本
	protected int version;

	protected String userId;
	
	protected String[] conditions;
	
	protected JSONObject lastParam;
	
	protected String versionKey;
	
	protected Map<String, Boolean> visiteMap;



	public AbsJsonTask(ApiJob job){
        super(job);
	//	paramObject = new JSONObject();
		setMethod(Method.POST);

        job.setApiListener(this);

	}
	
	public IJsonTask setWaitMessage(String waitMessage){
		//this.waitMessage = waitMessage;
        job.setWaitingMessage(waitMessage);
		return this;
	}
	
	public IJsonTask setButton(View button) {
		job.setButton(button);
		return this;
	}
	
	public static final Object[] NULL_RESULT = new Object[]{null};
	
	
	
	
	
	@Override
	protected void onError() {
		super.onError();
		if(LibConfig.DEBUG){
			if (error != null) {
				System.out.println(error);
			}
		}
//		IoUtil.writeToFile(error, FileLogUtil.getErrLogFile(getCurrentContext()));
		boolean notified = false;
		if(errorListener!=null){
			notified = true;
		}
		/*if(waitMessage!=null){
			Alert.cancelWait();
		}
		if (button != null) {
			button.setClickable(true);
		}*/
		if(isAutoNotify()){
			if (LibConfig.DEBUG) {
				notified = notified || 
						Notifier.notifyObservers(Notifier.makeError(job.getApi()), error, isNetworkError);
			} else {
				Alert.cancelWait();
				if (isNetworkError) {
					Alert.showShortToast("网络连接错误，请重试……");
				} else {
				notified = notified || 
						Notifier.notifyObservers(Notifier.makeError(job.getApi()), error, isNetworkError);
				}
			}
		}else{
			if(this.errorListener!=null){
				this.errorListener.onRequestError(error, isNetworkError);
				notified = true;
			}
		}
		
		if(!notified){
			Alert.showShortToast(error);
		}
	}
	
	
	
	@Override
	public void destroy() {
	/*	if(waitMessage!=null){
			Alert.cancelWait();
		}
		if (button != null) {
			button.setClickable(true);
		}*/
		destroied = true;
		lastParam = null;
		conditions = null;
	//	versionManager = null;
//		paramObject = null;
//		notLoginListener = null;
	//	requestQueue = null;
	//	files = null;
		super.destroy();
	}


	private int lastFlag= -10;
	@Override
	public int parse(byte[] content,boolean fromCache) {

        return 0;
	}




    protected abstract int parseResult(JSONObject json, boolean fromCache)
			throws JSONException;




	@Override
	public IJsonTask setCondition(String[] condition) {
		this.conditions = condition;
		return null;
	}
	


	@Override
	public IJsonTask setCachePolicy(CachePolicy cachePolicy) {
        job.setCachePolicy(CachePolicy.CachePolity_NoCache);
		return this;
	}
	@Override
	public IJsonTask setErrorListener(IRequestError errorListener) {
		this.errorListener = errorListener;
		return this;
	}
	protected Context getCurrentContext(){
		Object context = JsonTaskManager.getInstance().getCurrentContext();
		if(context instanceof Activity){
			return (Context)context;
		}
		return JsonTaskManager.getApplicationContext();
	}


	@Override
	public IJsonTask execute() {
	/*	if(waitMessage!=null){
			Alert.wait(getCurrentContext(), waitMessage);
		}
		if (button != null) {
			button.setClickable(false);
		}

		requestQueue.add(this);
		return this;*/
		if(job!=null){
			job.execute();
		}


        isCanceled=false;
        return this;
	}


	@Override
	public IJsonTask put(String paramName, Object paramValue) {
		if(job!=null){
			job.putObject(paramName,paramValue);
		}

		return this;
	}

	private IJsonTaskParam param;
	
	@Override
	public Object getParams(Object target) {
        return null;
	}

	public void setJsonTaskParam(IJsonTaskParam param){
		this.param = param;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setNotLoginListener(INotLoginListener listener){

	}



	public String getUserId() {
		return userId;
	}

	@Override
	protected void beforeRequest() {
		super.beforeRequest();
		if(getCachePolicy()!=CachePolicy.CachePolity_NoCache)
		{
            job.put("last",0);
			//put("last", versionManager.getVersion(cacheKey));
		}
	}
	@Override
	protected void beforeWriteCache() {

	}
	
	@Override
	public void reload() {
		//清除缓存
	/*	if(getCachePolicy()!=CachePolicy.CachePolity_NoCache){
			versionManager.setVersion(cacheKey, 0);
		}
*/
        job.reload();;

	//	requestQueue.add(this);
	}
	
	@Override
	public boolean isDestroyed(){
		return destroied;
	}
	
	public void clearParam(){
	//	files.clear();

	}
	@Override
	public void clearCache(){
        DMLib.getJobManager().clearCache();
	}

	private int index;
	
	public int getFactory(){
		return index;
	}
	
	public void setFactory(int index){
		job.setServer(index);
	}
	
	/**
	 * 是否等待
	 */
	private boolean padding;
	public void setPadding(boolean padding){
		this.padding = padding;
	}
	
	
	public boolean getPadding(){
		return this.padding;
	}
	
	
	/**
	 * 加密模式
	 * @return
	 */
	public int getCrypt() {
		return job.getCrypt();
	}
	public IJsonTask setCrypt(int crypt) {
		this.job.setCrypt(crypt);
		return this;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}

	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}

	
	private String token;

	private int time;


    @Override
    public boolean onApiMessage(ApiJob job) {
        setError(job.getMessage().getMessage(),false);
        onError();
        return true;
    }

    @Override
    public boolean onJobError(ApiJob job) {
        setError(job.getError().getReason(),job.getError().isNetworkError());
        onError();
        return true;
    }

    @Override
    public void onJobSuccess(ApiJob job) {
        onSuccess();
    }
}
