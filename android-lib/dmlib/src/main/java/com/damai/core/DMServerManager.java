package com.damai.core;

import java.util.Map;

import com.damai.dmlib.LibBuildConfig;

public class DMServerManager {
	/**
	 * 服务器
	 */
	private static ServerHandler[] servers = new ServerHandler[LibBuildConfig.MAX_SERVERS];
	public static void setUrl(int index,String url){
		servers[index].setUrl(url);
		DMServers.setUrl(index, url);
	}
	public static void setPosition(int server,int position,Map<String,Object> param){
		servers[server].setPosition(position, param);
	}
	public static ServerHandler[] getServerHandlers(){
		return servers;
	}
	
	
	private static int getMax(){
		int max = 0;
		for (ServerHandler handler : servers) {
			if(handler==null){
				return max;
			}
			max++;
		}
		return max;
	}
	
	public static ApiParser[] getApiParsers(int dataType) {
		int max = getMax();
		ApiParser[] parsers = new ApiParser[max];
		//这里需要定义server数组
		if(dataType==DataTypes.DataType_ApiArray){
			
			for(int i=0; i < max; ++i){
				if(servers[i]!=null){
					parsers[i] = servers[i].getListParser();
				}
			}
			
			
		}else if(dataType==DataTypes.DataType_ApiObject){
			
			for(int i=0; i < max; ++i){
				if(servers[i]!=null){
					parsers[i] = servers[i].getObjectParser();
				}
			}
			
			
		}else if(dataType==DataTypes.DataType_ApiPage){
			
			for(int i=0; i < max; ++i){
				if(servers[i]!=null){
					parsers[i] = servers[i].getPageParser();
				}
			}
			
			
		}
		
		return parsers;
	}
	
	/**
	 * 设置url
	 * @param index
	 * @param start
	 * @param url
	 */
	public static void registerServer(int index,ServerHandler handler){
		servers[index] = handler;
	}

}
