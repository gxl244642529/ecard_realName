package com.citywithincity.models.http;

import org.json.JSONException;
import org.json.JSONObject;

import com.citywithincity.auto.tools.Notifier;
import com.citywithincity.interfaces.IJsonTask;
import com.citywithincity.interfaces.IRequestResult;
import com.citywithincity.interfaces.IValueJsonTask;
import com.citywithincity.models.cache.CachePolicy;
import com.damai.core.ApiJob;

abstract class AbsValueJsonTask<T>  extends AbsJsonTask<T> implements IValueJsonTask<T> {
	
	
	protected IRequestResult<T> listener;
	
	protected AbsValueJsonTask(ApiJob job){
		super(job);
		setCachePolicy(CachePolicy.CachePolity_NoCache);
	}



	@Override
	public void destroy() {
		this.listener = null;
		super.destroy();
	}
	
	public IJsonTask setListener(IRequestResult<T> listener){
		this.listener = listener;
		return this;
	}
	

	@Override
	protected int parseResult(JSONObject json,boolean fromCache) throws JSONException {
		Object data= json.get(RESULT);
		onParserParseResult(data);
		return ParseResult.OK;
	}
	
	protected abstract T onParserParseResult(Object data) ;

	@Override
	public void onSuccess() {

        Object d = job.getData();
        T data = onParserParseResult(d);
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
