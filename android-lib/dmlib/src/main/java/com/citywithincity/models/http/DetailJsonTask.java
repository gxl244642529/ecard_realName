package com.citywithincity.models.http;

import org.json.JSONException;
import org.json.JSONObject;

import com.citywithincity.auto.Observer;
import com.citywithincity.interfaces.IDetailJsonTask;
import com.citywithincity.interfaces.IObjectJsonTask;
import com.damai.core.ApiJob;

@Observer
class DetailJsonTask<T> extends ObjectJsonTask<T> implements IDetailJsonTask<T> {
	
	protected Object id;
	
	protected DetailJsonTask(ApiJob apiJob){
		super(apiJob);
	}
	@Override
	protected void makeCacheKey() {
		//cacheKey = getDataKey().append("_").append(id).toString();
	}
	@Override
	protected int parseResult(JSONObject json,boolean fromCache) throws JSONException {
		if(json.has("version"))
		{
			version = json.getInt("version");
		}
		return super.parseResult(json, fromCache);
	}

	@Override
	public IObjectJsonTask<T> setId(Object id) {
		this.id = id;
		this.put("id", id);
		return this;
	}
	
	@Override
	public IObjectJsonTask<T> setId(String name, Object id) {
		this.id = id;
		this.put(name, id);
		return this;
	}
	
}
