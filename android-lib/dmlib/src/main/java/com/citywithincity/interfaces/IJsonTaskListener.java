package com.citywithincity.interfaces;

import java.util.List;

public interface IJsonTaskListener<T> {
	Object beforeParseData();
	void afterParseData(List<T> data,Object result);
}
