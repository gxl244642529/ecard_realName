package com.citywithincity.interfaces;

import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.widget.ImageView;

import com.citywithincity.auto.NotificationList;
import com.citywithincity.auto.NotificationList.Notification;
import com.citywithincity.models.cache.CachePolicy;
import com.citywithincity.models.vos.Null;
import com.damai.core.DMAccount;
import com.damai.http.cache.ImageCache;

/**
 * 通知列表
 * @author randy
 *
 */
@NotificationList({
@Notification(name=IJsonTaskManager.LOGIN_SUCCESS,type=DMAccount.class,description="登录成功"),
@Notification(name=IJsonTaskManager.LOGOUT,type=Null.class,description="退出登录")
}
)
public interface IJsonTaskManager extends IDestroyable,IModelManager {
	
	public static final String LOGIN_SUCCESS = "IJsonTaskManager.loginSuccess";
	public static final String LOGOUT = "IJsonTaskManager.logout";
	
	
	
	public static final String DEVICE_ID="deviceID";
	public static final String PUSH_ID = "pushID";

	void setUserInfoClass(Class<? extends DMAccount> clazz);
	
	<T> IJsonParser<T> createParser(Class<T> clazz);
	
	//退出程序
	void exit(Context context);
	//设置用户信息
	void onSetUserInfo(IOnResume listener);
	/**
	 * 
	 * @param context
	 */
	void requestLogin(Activity context);
	
	void setImageCache(ImageCache imageCache);
	
	
	
	/**
	 * 设置图片
	 */
	void setImageSrc(String url,ImageView imageView);
	
	void setImageSrc(String url,ImageView imageView,int defaultResId);
	
	
	/**
	 * 设置api调用url前缀
	 * @param baseUrl
	 */
	void setBaseUrl(String baseUrl);
	
	/**
	 * 设置app，一般在application的onCreate中调用
	 * @param app
	 */
	void setApplication(Application app) ;
	

	/**
	 * 在fragment、activity中onPause调用
	 * @param context    		activty|fragment
	 */
	void onCreate(Activity context);
	/**
	 * 在fragment、activity中onDestroy调用
	 * @param context activty|fragment
	 */
	void onDestroy(Activity context) ;
	
	String getPushID();
	/**
	 * 在fragment、activity中onResume调用
	 * @param context    		activty|fragment
	 */
	void onResume(Activity context);
	
	/**生存周期相关 start**/
	/**
	 * 在fragment、activity中onPause调用
	 */
	void onPause(Activity context);

	
	void setActivityCycleListener(IActivityCycleListener listener);
	
	
	/**
	 * 获取当前上下文
	 * @return
	 */
	Object getCurrentContext();
	
	
	
	/**
	 * 清除cookie，一般在登出时候调用
	 */
	void clearSession();
	
	/**
	 * 清空缓存
	 */
	void clearCache();
	
	/**
	 * 取消所有网络操作
	 */
	void cancelRequest();
	
	/**
	 * 取消与当前相关上下文环境有关的网络操作
	 * @param context	   在onResume中设置的
	 */
	void cancel(Object context);
	
	
	/**
	 * 获取正在运行的activity
	 * @return
	 */
	List<Activity> getRunningActivities();
	

	
	/**
	 * 获取设置id
	 * @return
	 */
	String getDeviceID();
	
	/**创建任务相关 start**/
	IValueJsonTask<Object> createValueJsonTask(String api,int factory);
	IValueJsonTask<Integer> createIntegerJsonTask(String api,int factory);
	IValueJsonTask<Boolean> createBooleanJsonTask(String api,int factory);

	<T> IArrayJsonTask<T> createArrayJsonTask(String api, CachePolicy cachePolicy,boolean isPrivate,int factory);
	<T> IArrayJsonTask<T> createArrayJsonTask(String api, CachePolicy cachePolicy,boolean isPrivate,Class<T> clazz,int factory);
	<T> IDetailJsonTask<T> createDetailJsonTask(String api,CachePolicy cachePolicy,Class<T> clazz,int factory);	
	<T> IObjectJsonTask<T> createObjectJsonTask(String api,CachePolicy cachePolicy,Class<T> clazz,int factory);
	
	/**创建任务相关 start**/
	IValueJsonTask<Object> createValueJsonTask(String api);
	IValueJsonTask<Integer> createIntegerJsonTask(String api);
	IValueJsonTask<Boolean> createBooleanJsonTask(String api);

	<T> IArrayJsonTask<T> createArrayJsonTask(String api, CachePolicy cachePolicy,boolean isPrivate);
	<T> IArrayJsonTask<T> createArrayJsonTask(String api, CachePolicy cachePolicy,boolean isPrivate,Class<T> clazz);
	<T> IDetailJsonTask<T> createDetailJsonTask(String api,CachePolicy cachePolicy,Class<T> clazz);	
	<T> IObjectJsonTask<T> createObjectJsonTask(String api,CachePolicy cachePolicy,Class<T> clazz);
	
	

	/**
	 * 登录
	 * @param username
	 * @param pwd
	 * @param listener
	 */
	void login(String username, String pwd, IRequestResult<DMAccount> listener) ;
	
	void login(String username, String pwd);
	
	//账号注销
	void logout();
	
	
	/**
	 * 获取用户信息
	 * @return
	 */
	<T extends DMAccount> T getUserInfo();
	/**
	 * 是否登录
	 */
	boolean isLogin();
	
	/**
	 * 判断activity是否运行
	 * @param clazz
	 * @return
	 */
	boolean isRunning(Class<? extends Activity> clazz);
	
	/**
	 * 移除缓存,匹配模式
	 */
	void removeAllCache(String key);
	
	



	/**
	 * 加载图片
	 * @param url
	 * @param listener
	 * @return
	 */
	IDestroyable loadImage(String url, ImageRequestLisener listener);

	IValueJsonTask<Object> newTask(String api);
	
	
}
