package com.citywithincity.models.http;

import org.json.JSONException;
import org.json.JSONObject;

import com.citywithincity.auto.tools.Notifier;
import com.citywithincity.interfaces.IJsonParser;
import com.citywithincity.interfaces.IObjectJsonTask;
import com.citywithincity.interfaces.IRequestError;
import com.citywithincity.interfaces.IRequestSuccess;
import com.damai.core.ApiJob;

public class ObjectJsonTask<T> extends AbsJsonTask<T> implements
		IObjectJsonTask<T> {
	protected IRequestSuccess<T> listener;
	protected IJsonParser<T> parser;
	protected boolean visited;

	protected ObjectJsonTask(ApiJob job) {
		super(job);

	}

	@Override
	public void destroy() {
		parser = null;
		listener = null;
		super.destroy();
	}

	@Override
	protected int parseResult(JSONObject json, boolean fromCache)
			throws JSONException {
	/*	if (!json.isNull(RESULT)) {
			JSONObject obj = json.getJSONObject(RESULT);
			if (parser != null) {
				data = parser.parseResult(obj);
			} else {
				return ParseResult.ERROR;
			}
		}else {
			data = null;
		}*/

		return ParseResult.OK;
	}

	public IObjectJsonTask<T> setListener(IRequestSuccess<T> listener) {
		this.listener = listener;
		if (listener instanceof IRequestError) {
			this.errorListener = (IRequestError) listener;
		}

		return this;
	}

	public IObjectJsonTask<T> setParser(IJsonParser<T> parser) {
		this.parser = parser;
		return this;
	}

	@Override
	public void onSuccess() {
		/*if (waitMessage != null) {
			Alert.cancelWait();
		}*/

		visited = true;
		JSONObject json = job.getData();
		T data = null;
		if(parser!=null && json!=null){
			try {
				data=parser.parseResult(json);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		if (listener != null) {
			listener.onRequestSuccess(data);
		}
		if (isAutoNotify()) {
			Notifier.notifyObservers(job.getApi(), data);
		}
	}

	@Override
	protected void makeCacheKey() {

	}
}
