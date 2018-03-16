package com.citywithincity.models.http.core;

import com.citywithincity.interfaces.IDestroyable;
import com.citywithincity.interfaces.IRequestError;
import com.citywithincity.models.cache.CachePolicy;
import com.citywithincity.utils.MD5Util;
import com.damai.core.ApiJob;
import com.damai.jobqueue.Job;


public abstract class AbsRequest<T> implements IDestroyable,Request {
	
	
	public static final int DEFAULT_TIMEOUT = 30 * 1000;
	
	
	public static interface ParseResult {
        int OK = 1;		//成功
        int ERROR = 2;	
        //可以使用缓存中的数据，因为没有变化
        int USE_CACHE_IF_EXISTS = 3;
        int REQUIRE_LOGIN = 4;
        int ERROR_BUT_DONT_NOTIFY = 5;
    }
	
	static interface ResponseResult{
		int OK=8;
		int ERROR=16;
	}
	
	public static interface Method {
        int GET = 0;
        int POST = 1;
    }
	
	
	/**
	 * 在工作线程中处理结果
	 * @author Randy
	 *
	 */
	public static interface OnRequestResult{
		void onSuccess(AbsRequest<?> request);
		void onError(AbsRequest<?> request,String error, boolean isNetworkError);
	}

	/**
	 * 取消标志
	 */
	protected boolean isCanceled;
	protected String cacheKey;
	protected String url;
	protected boolean forceToRefresh;
	private int method;
	protected boolean isNetworkError;
	protected String error;
	protected IRequestError errorListener;
	protected Object tag;
	
	protected RequestCache<T> cache;


	protected ApiJob job;
	public AbsRequest(ApiJob job){
		this.job = job;
	}


	@Override
	public void destroy(){
		if(job!=null){
			job.cancel();
			job = null;
		}
		cancel();
		this.errorListener = null;
		this.tag = null;
		this.cache = null;
	}
	

	
	
	
	public Object getTag(){
		return tag;
	}

	
	
	@Override
	public Request setForceToRefresh(boolean force) {
		forceToRefresh = force;
		return this;
	}
	@Override
	public Request setUrl(String url){
		this.url = url;
		return this;
	}
	
	
	
	
	public Object getParams(Object object){
		return null;
	}
	
	/**
	 * 网址
	 * @return
	 */
	public String getUrl(){
		return url;
	}
	
	@Override
	public int compareTo(Job another) {
		return getPriority().ordinal() > another.getPriority().ordinal() ? 1 : -1;
	}
	
	/**
	 * 获取存储键值
	 * @return
	 */
	public String getCacheKey(){
		return cacheKey;
	}
	

	

	protected void makeCacheKey() {
		cacheKey= MD5Util.md5Appkey(url);
	}
	
	/**
	 * 缓存策略
	 * @return
	 */
	public CachePolicy getCachePolicy(){
		return job.getCachePolicy();
	}

	@Override
	public Request setTag(Object tag) {
		this.tag = tag;
		return this;
	}
	
	/**
	 * 取消
	 */
	public void cancel(){
		isCanceled = true;
	}
	/**
	 * 是否已经取消了
	 * @return
	 */
	public boolean isCanceled(){
		return isCanceled;
	}
	
	/**
	 * 任务优先级
	 * @return
	 */
	public Priority getPriority(){
		return Priority.NORMAL;
	}
	/**
	 * 解析
	 */
	abstract protected int parse(byte[] content,boolean fromCache);

	public int getMethod() {
		return method;
	}

	public void setMethod(int method) {
		this.method = method;
	}
	
	public T getData(){
		return job.getData();
	}
	
	

	public void setError(String error, boolean isNetworkError) {
		this.error = error;
		this.isNetworkError = isNetworkError;
	}

	/**
	 * 成功了
	 */
	abstract public void onSuccess();

	/**
	 * 失败了
	 */
	protected void onError() {
		if(errorListener!=null){
			errorListener.onRequestError(error, isNetworkError);
		}
		
	}
	
	
	/**
	 * 自己处理缓存
	 * @return
	 */
	public boolean loadFromCache(){
		return false;
	}
	
	/**
	 * 自己处理缓存
	 * @return
	 */
	public boolean writeToCache(){
		return false;
	}


	public String getError() {
		return error;
	}
	
	public boolean isNetworkError(){
		return isNetworkError;
	}
	
	public void setCache(RequestCache<T> cache){
		this.cache = cache;
	}
	
	public RequestCache<T> getCache(){
		return this.cache;
	}

	/**
	 * 当网络请求之前的准备工作
	 */
	protected void beforeRequest() {
		error = null;
	}

	/**
	 * 当写入缓存之前的准备工作
	 */
	protected void beforeWriteCache() {
		
		
	}
	
	protected int contentLength;
	protected int downloadLength;

	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}

	public void setProgress(int count) {
		this.downloadLength = count;
	}

	public void reload() {
		
	}
	/*
	@Override
	public Request setCachePolicy(CachePolicy cachePolicy){
		this.cachePolicy = cachePolicy;
		return this;
	}
	
	@Override
	public Request setErrorListener(IRequestError errorListener){
		this.errorListener = errorListener;
		return this;
	}

	*/
	
	int timeOut = DEFAULT_TIMEOUT;

	public int getTimeout() {
		return timeOut;
	}
}
