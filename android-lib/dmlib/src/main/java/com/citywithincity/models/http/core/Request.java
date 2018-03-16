package com.citywithincity.models.http.core;

import com.citywithincity.interfaces.IDestroyable;
import com.damai.jobqueue.Job;

public interface Request extends Job,IDestroyable {
	Request setTag(Object tag);
	Request setUrl(String url);
	Request setForceToRefresh(boolean force);
//	Request setCachePolicy(CachePolicy cachePolicy);
//	Request setErrorListener(IRequestError errorListener);
	Object getTag();
}
