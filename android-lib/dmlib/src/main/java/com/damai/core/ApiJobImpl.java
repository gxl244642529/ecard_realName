package com.damai.core;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Looper;
import android.view.View;

import com.citywithincity.models.cache.CachePolicy;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.MessageUtil;
import com.citywithincity.utils.MessageUtil.IMessageListener;
import com.damai.auto.LifeManager;

/**
 * 表示一项api任务
 * @author Randy
 *
 */
public class ApiJobImpl extends HttpJobImpl implements ApiJob, OnCancelListener {


    protected Class<?> entity;
    protected int crypt;
    protected View button;

    /**
     *
     */
    protected OnApiMessageListener onJobMessageListener;
    /**
     * 参数
     */
    protected Map<String, Object> params;


    /**
     * 服务器消息
     */
    private DMMessage message;
    /**
     * 是否可以取消，用户登录等
     */
    private boolean cancelable;

    protected boolean notify = true;

    ApiJobImpl(){
    	super();
        params = new TreeMap<String, Object>();
        setDeliverType(DeliverTypes.DeliverType_Api);
        setHandlerType(HandlerTypes.HandlerType_Api);
       
    }


    public Map<String, Object> getParams(){
        return params;
    }



    /**
     * 不通知
     */
    public void disableNotify(){
        notify = false;
    }

    public void setOnMessageListener(OnApiMessageListener onMessageListener){
        this.onJobMessageListener = onMessageListener;
    }

    @Override
    public void destroy() {
        params = null;
        super.destroy();
    }

    public Class<?> getEntity() {
        return entity;
    }

    /**
     * 支持bean转json
     * @param key
     * @param obj
     * @return
     */
    public <T> ApiJobImpl putBean(String key,T obj){
        params.put(key, obj);
        return this;
    }

    /**
     * 支持list转json
     * @param key
     * @param array
     * @return
     */
    public <T> ApiJobImpl putList(String key,List<T> array){
        params.put(key, array);
        return this;
    }
    /**
     * 不推荐调用这个
     * @param key
     * @param data
     * @return
     */
    public <T> ApiJobImpl putObject(String key,T data){
        params.put(key, data);
        return this;
    }

    public ApiJobImpl put(String key,String data){
        params.put(key, data);
        return this;
    }
    public ApiJobImpl put(String key,Integer data){
        params.put(key, data);
        return this;
    }
    public ApiJobImpl put(String key,Boolean data){
        params.put(key, data);
        return this;
    }
    public ApiJobImpl put(String key,Double data){
        params.put(key, data);
        return this;
    }


    public ApiJobImpl put(String key, Long value) {
        params.put(key, data);
        return this;
    }

    /**
     * 支持文件
     * @param key
     * @param data
     * @return
     */
    public ApiJobImpl put(String key,File data){
        params.put(key, data);
        return this;
    }

    public void setEntity(Class<?> entity) {
        this.entity = entity;
    }

    public void removeParam(String key){
        params.remove(key);
    }

    public String getApi() {
        return id;
    }

    public void setApi(String api) {
        this.id = api;
    }



    public int getCrypt() {
        return crypt;
    }

    public void setCrypt(int crypt) {
        this.crypt = crypt;
    }

    public CachePolicy getCachePolicy() {
        return cachePolicy;
    }

    public void setCachePolicy(CachePolicy cachePolicy) {
        this.cachePolicy = cachePolicy;
    }

    private void onStart(){
    	if(button!=null){
            button.setEnabled(false);
        }

        if(waitingMessage!=null){
            if(cancelable){
                Alert.wait(LifeManager.getActivity(), waitingMessage,true,this);
            }else{
                Alert.wait(LifeManager.getActivity(), waitingMessage);
            }
        }
    }
    
    public void execute() {
    	if(button!=null || waitingMessage!=null){
    		if(Looper.myLooper() == Looper.getMainLooper()){
    			onStart();
    		}else{
    			MessageUtil.sendMessage(new IMessageListener(){

    				@Override
    				public void onMessage(int message) {
    					onStart();
    				}
        			
        		});
    		}
    	}
        //开始执行
        ((JobManagerImpl)DMLib.getJobManager()).addJob(this);
    }
    public void reload() {
        ((JobManagerImpl)DMLib.getJobManager()).reload(this);
    }

    @Override
    protected String makeDataKey() {
        StringBuilder sb = new StringBuilder();
        sb.append(getApi().replace('/', '_'));
        for (Entry<String, Object> entry : params.entrySet()) {
            String key = entry.getKey();
            if(key.equals("deviceID")){
                continue;
            }
            if(key.equals("position")){
                continue;
            }
            sb.append('_').append(entry.getValue());
        }
        if(params.containsKey("position")){
            sb.append('_').append(params.get("position"));
        }
        
        DMAccount account= DMAccount.get();
        if(account!=null && account.getId()!=null){
        	sb.append(account.getId());
        }
        
        return sb.toString();
    }

    public View getButton() {
        return button;
    }

    public void setButton(View button) {
        this.button = button;
    }

    public void setOnJobSuccessListener(OnApiSuccessListener onJobSuccessListener) {
        super.setOnJobSuccessListener(onJobSuccessListener);
    }

    public void setPosition(int position) {
        DMServerManager.setPosition(server, position, params);
        //DMServers.getUrl()(server);
        //params.put("position", position);
    }

    public void putAll(Map<String, Object> data) {
        params.putAll(data);
    }
    private String waitingMessage;
    public void setWaitingMessage(String waiting) {
        this.waitingMessage = waiting;
    }

    public String getWaitingMessage(){
        return this.waitingMessage;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        cancel();
    }

    public boolean isCancelable() {
        return cancelable;
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public DMMessage getMessage() {
        return message;
    }

    public void setMessage(DMMessage message) {
        this.message = message;
    }


    @Override
    public void setApiListener(ApiListener listener) {
        onJobSuccessListener = listener;
        onJobErrorListener = listener;
        onJobMessageListener = listener;

    }


	@Override
	public void setOnSuccessListener(OnJobSuccessListener<ApiJob> listener) {
		 onJobSuccessListener = listener;
	}


	@Override
	public boolean is(String api) {
		return id.equals(api);
	}






}
