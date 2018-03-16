package com.damai.core;

import com.citywithincity.models.cache.CachePolicy;
import com.citywithincity.utils.MD5Util;

public class HttpJobImpl extends JobImpl implements HttpJob {
    public static final int GET = 0;
    public static final int POST = 1;

    protected String dataKey;
    protected String url;
    protected int dataType;
    protected int server;
    protected Object extra;
    protected Object data;
    protected int totalBytes;
    protected int currentBytes;
    //错误信息，如网络错误等
    protected DMError error;
    protected CachePolicy cachePolicy;
    protected int retryTimes;
    protected int method;
    /**
     * 超时设置
     */
    protected int timeoutMs = 5000;
    
    public HttpJobImpl(){
    	super();
    	 cachePolicy = CachePolicy.CachePolity_NoCache;
    }

    public String getCacheKey(){
        if(dataKey==null){
            dataKey = makeDataKey();
        }
        return dataKey;
    }

    /**
     * 设置超时毫秒
     */
    public void setTimeoutMS(int timeoutMs){
        this.timeoutMs = timeoutMs;
    }

    @Override
    public void destroy() {
        super.destroy();
        extra = null;
        data = null;
    }

    protected String makeDataKey() {
        return MD5Util.md5Appkey(url);
    }


    public String getUrl() {
        return url;
    }


    public void setUrl(String url) {
        this.url = url;
    }


    public int getDataType() {
        return dataType;
    }


    public void setDataType(int dataType) {
        this.dataType = dataType;
    }


    public int getServer() {
        return server;
    }


    public void setServer(int server) {
        this.server = server;
    }


    @SuppressWarnings("unchecked")
    public <T> T getExtra() {
        return (T) extra;
    }


    public void setExtra(Object extra) {
        this.extra = extra;
    }


    @SuppressWarnings("unchecked")
    public <T> T getData() {
        return (T) data;
    }


    public void setData(Object data) {
        this.data = data;
    }


    public DMError getError() {
        return error;
    }


    public void setError(DMError error) {
        this.error = error;
    }


    public CachePolicy getCachePolicy() {
        return cachePolicy;
    }


    public void setCachePolicy(CachePolicy cachePolicy) {
        this.cachePolicy = cachePolicy;
    }




    public int getTotalBytes() {
        return totalBytes;
    }


    public void setTotalBytes(int totalBytes) {
        this.totalBytes = totalBytes;
    }


    public int getCurrentBytes() {
        return currentBytes;
    }

    @Override
    public void setRetryTimes(int times) {
        this.retryTimes = times;
    }

    public int getRetryTimes(){
        return retryTimes;
    }


    public void setCurrentBytes(int currentBytes) {
        this.currentBytes = currentBytes;
    }







}
