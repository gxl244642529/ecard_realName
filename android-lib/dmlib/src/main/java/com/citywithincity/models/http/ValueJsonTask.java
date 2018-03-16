package com.citywithincity.models.http;

import org.json.JSONException;
import org.json.JSONObject;

import com.citywithincity.auto.tools.Notifier;
import com.citywithincity.interfaces.IJsonTask;
import com.citywithincity.interfaces.IRequestResult;
import com.citywithincity.interfaces.IValueJsonTask;
import com.citywithincity.models.cache.CachePolicy;
import com.damai.core.ApiJob;

public class ValueJsonTask  extends AbsJsonTask<Object> implements IValueJsonTask<Object> {
	
	
	protected IRequestResult<Object> listener;
	
	protected ValueJsonTask(ApiJob job){
		super(job);
		setCachePolicy(CachePolicy.CachePolity_NoCache);
	}
	
	public IJsonTask setListener(IRequestResult<Object> listener){
		this.listener = listener;
		return this;
	}
	
	
	@Override
	protected int parseResult(JSONObject json,boolean fromCache) throws JSONException {
		
		/*
		if (!json.isNull(RESULT)) {
			data=json.get(RESULT);
		}else {
			data = null;
		}*/
		
		return ParseResult.OK;
	}
	
	@Override
	public void onSuccess() {
		Object data = job.getData();
		if(listener!=null){
			listener.onRequestSuccess(data);
		}
		/*if(waitMessage!=null){
			Alert.cancelWait();
		}*/
		if(isAutoNotify()){
			Notifier.notifyObservers(job.getApi(),data);
		}
	}
	

}
