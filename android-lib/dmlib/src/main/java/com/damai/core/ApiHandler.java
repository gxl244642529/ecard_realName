package com.damai.core;

import com.damai.dmlib.LibBuildConfig;

public class ApiHandler implements JobHandler<ApiJobImpl> {
	private ApiServerHandler[] handlers;
	
	public ApiHandler(){
		handlers = new ApiServerHandler[LibBuildConfig.MAX_SERVERS];
	}
	
	public ApiServerHandler[] getHandlers(){
		return handlers;
	}
	
	/**
	 * 注册服务器的handler
	 * @param server
	 * @param handler
	 */
	public void register(int server,ApiServerHandler handler){
		if(server > handlers.length-1){
			throw new RuntimeException("Server index " + server + " is out of range " + handlers.length);
		}
		handlers[server] = handler;
	}
	
	@Override
	public void handleJob(ApiJobImpl job) {
		//下载任务
		//解析任务
		//
		handlers[job.server].handleJob(job);
	}

	@Override
	public void shutdown() {
		for (ApiServerHandler handler : handlers) {
			if(handler!=null){
				handler.shutdown();
			}
		}
	}

	public void clearSession() {
		for (ApiServerHandler handler : handlers) {
			if(handler!=null){
				handler.clearSession();
			}
		}
	}

}
