package com.damai.core;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import com.citywithincity.interfaces.IDestroyable;
import com.damai.http.api.a.Api;

public class M {
	
	
	private static class MethodInfo{
		private ApiJob job;
		private Api api;
		
		public MethodInfo(Api api){
			this.api = api;
		}
		
		public Object invoke(Object[] args){
			if(this.job == null || this.job.isDestroied()){
				this.job = createJob(api);
			}
			String[] params = api.params();
			int i=0;
			for (String string : params) {
				job.putObject(string, args[i++]);
			}
			job.execute();
			return this.job;
		}
		
		protected static ApiJob createJob(Api api){
			ApiJob job = null;
			switch (api.type()) {
			case Api.Array:
				job = DMLib.getJobManager().createArrayApi(api.name());
				break;
			case Api.Object:
				job = DMLib.getJobManager().createObjectApi(api.name());
				break;
			case Api.Page:
				job = DMLib.getJobManager().createPageApi(api.name());
				break;
			}
			job.setCrypt(api.crypt());
			job.setServer(api.server());
			job.setCachePolicy(api.cachePolicy());
			job.setEntity(api.entity());
			return job;
		}
	}
	
	
	private static Map<Method, MethodInfo> map = new HashMap<Method, MethodInfo>();
	
	public static class Invoker implements InvocationHandler{
		
		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			MethodInfo methodInfo = map.get(method);
			if(methodInfo ==null){
				Api api = method.getAnnotation(Api.class);
				methodInfo = new MethodInfo(api);
			}
			return methodInfo.invoke(args);
		}
		
	}
	
	private static Invoker invoker = new Invoker();
	
	private static Map<Class<?>, Object> modelMap = new HashMap<Class<?>, Object>();
	
	/**
	 * 获取api的动态代理
	 * @param modelClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static final <T> T get(Class<T> modelClass){
		if(modelMap.containsKey(modelClass)){
			return (T) modelMap.get(modelClass);
		}
		if(modelClass.isInterface()){
			Object proxy = Proxy.newProxyInstance(modelClass.getClassLoader(), new Class<?>[]{modelClass}, invoker);
			modelMap.put(modelClass, proxy);
			return (T)proxy;
		}
		try {
			Object result = modelClass.newInstance();
			modelMap.put(modelClass, result);
			return (T)result;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	
	public static void remove(Class<?> modelClass){
		Object result = modelMap.get(modelClass);
		if(result!=null ){
			if((result instanceof IDestroyable)){
				((IDestroyable)result).destroy();
			}
			modelMap.remove(modelClass);
		}
	}
}
