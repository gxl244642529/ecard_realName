package com.citywithincity.interfaces;

public interface IObjectJsonTask<T> extends IJsonTask {
	IObjectJsonTask<T> setListener(IRequestSuccess<T> listener);
	IObjectJsonTask<T> setParser(IJsonParser<T> parser);
}
