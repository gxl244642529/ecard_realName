package com.damai.core;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.citywithincity.interfaces.IViewContainer;
import com.citywithincity.models.cache.CachePolicy;
import com.damai.pay.DMPayModel;

public interface JobManager{
	/**
	 * 生命周期
	 * @param container
	 */
	void onCreate(IViewContainer container);
	
	Context getApplicationContext();
	/**
	 * 视图创建完毕
	 * @param container
	 */
	void onViewCreate(IViewContainer container);
	
	/**
	 * 销毁
	 * @param obj
	 */
	void onDestroy(IViewContainer obj);
	
	
	/**
	 * 
	 * @param container
	 */
	void onResume(IViewContainer container);
	
	/**
	 * 清除全部缓存
	 */
	void clearCache();
	
	/**
	 * 启动，一般在application的onCreate中调用
	 * @param appContext
	 */
	void startup(Context appContext);
	
	/**
	 * 
	 * @param apiJob
	 */
	void clearCache(ApiJob apiJob);
	
	/**
	 * 创建api任务
	 * @param api
	 * @return
	 */
	ApiJob createObjectApi(String api);
	ApiJob createPageApi(String api) ;
	ApiJob createArrayApi(String api) ;
	
	/**
	 * 加载图片
	 * @param url
	 * @param listener
	 * @return
	 */
	Job loadImage(String url, JobListener<HttpJob> listener);
	
	/**
	 * 
	 * @param url
	 * @param imageView
	 */
	void loadImage(String url, ImageView imageView) ;
	
	
	
	void setPushId(String pushId);
	
	
	String getPushId();
	
	/**
	 * get方式获取数据
	 * @param url
	 * @param cachePolicy
	 * @param listener
	 * @return
	 */
	Job get(String url, CachePolicy cachePolicy,JobListener<HttpJob> listener);
	
	/**
	 * 下载文件
	 * @param url
	 * @param destFile
	 * @param listener
	 * @return
	 */
	HttpJob download(String url,File destFile,JobListener<HttpJob> listener);
	
	/**
	 * 
	 */
	void clearSession();
	
	
	/**
	 * 
	 * @param moduleName
	 * @param supportPayTypes
	 * @return
	 */
	DMPayModel createPayModel(String moduleName, int[] supportPayTypes);

	void stopRequest(Object context);
}
