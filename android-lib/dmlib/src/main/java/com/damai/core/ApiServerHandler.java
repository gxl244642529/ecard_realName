package com.damai.core;


public abstract class ApiServerHandler extends AbsHandler<ApiJobImpl> implements ServerHandler {
	
	protected JobListener<JobImpl> listener;
	protected ApiNetwork network;
	protected OnApiMessageListener messageListener;
	protected Cache cache;
	protected ByteArrayPool mPool;
	public void initParam(JobListener<JobImpl> listener,OnApiMessageListener apiMessageListener,ByteArrayPool pool,Cache cache){
		this.listener = listener;
		mPool = pool;
		this.messageListener = apiMessageListener;
		network = new ApiNetwork(pool);
		this.cache = cache;
	}

	public void clearSession() {
		network.clearSession();
	}
	
	@Override
	protected void cancelProgress() {
		network.cancel();
	}
}
