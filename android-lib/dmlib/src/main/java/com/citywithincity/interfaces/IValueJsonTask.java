package com.citywithincity.interfaces;

public interface IValueJsonTask<T> extends IJsonTask {
	IJsonTask setListener(IRequestResult<T> listener);
}
