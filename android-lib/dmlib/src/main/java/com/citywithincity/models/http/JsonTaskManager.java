package com.citywithincity.models.http;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.citywithincity.auto.ApiArray;
import com.citywithincity.auto.ApiDetail;
import com.citywithincity.auto.ApiObject;
import com.citywithincity.auto.ApiSuccess;
import com.citywithincity.auto.ApiValue;
import com.citywithincity.auto.Crypt;
import com.citywithincity.auto.JsonVo;
import com.citywithincity.auto.tools.AutoCreator;
import com.citywithincity.auto.tools.BaseEventHandler;
import com.citywithincity.auto.tools.BaseItemEventHandler;
import com.citywithincity.auto.tools.Command;
import com.citywithincity.auto.tools.Notifier;
import com.citywithincity.auto.tools.ViewDataSetter;
import com.citywithincity.interfaces.IActivityCycleListener;
import com.citywithincity.interfaces.IArrayJsonTask;
import com.citywithincity.interfaces.IDestroyable;
import com.citywithincity.interfaces.IDetailJsonTask;
import com.citywithincity.interfaces.IJsonParser;
import com.citywithincity.interfaces.IJsonTask;
import com.citywithincity.interfaces.IJsonTaskManager;
import com.citywithincity.interfaces.ILog;
import com.citywithincity.interfaces.IObjectJsonTask;
import com.citywithincity.interfaces.IOnResume;
import com.citywithincity.interfaces.IRequestResult;
import com.citywithincity.interfaces.IValueJsonTask;
import com.citywithincity.interfaces.ImageRequestLisener;
import com.citywithincity.models.LocalData;
import com.citywithincity.models.LogFactory;
import com.citywithincity.models.cache.CachePolicy;
import com.citywithincity.models.http.core.Request;
import com.citywithincity.mvc.ModelHelper;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.Http;
import com.citywithincity.utils.MD5Util;
import com.citywithincity.utils.ViewUtil;
import com.damai.core.DMAccount;
import com.damai.core.DMLib;
import com.damai.core.DeviceUtil;
import com.damai.core.HttpJob;
import com.damai.core.JobListener;
import com.damai.http.cache.ImageCache;
import com.damai.push.Push;

public abstract class JsonTaskManager implements IJsonParser<DMAccount>,IRequestResult<DMAccount>,
		IJsonTaskManager, INotLoginListener{

	protected static final int MESSAGE_LOGIN = 1;
	public static final String SUB_USER_INFO = "ecard_userInfo";

	private Context currentContext;
	private Map<Object, Map<String, Request>> taskMap;

	protected static Application application;
	protected static Context appContext;
	// 基本url
	protected static String baseUrl;
	
	public static String javaServerUrl;


	protected static IJsonTaskManager instance;

	//private Cache<byte[]> cache;
	//private DefaultRequestQueue requestQueue;
	private int defaultImageResId;
	private ImageCache imageCache;
	//private Network network;
//	protected IJsonTaskFactory[] factories;
	/**
	 * jsonparser
	 */
	protected Map<Class<?>, IJsonParser<?>> jsonParserMap;

	public JsonTaskManager() {
	}

/*	protected Cache<?> getCache() {
		return cache;
	}
*/
	public void setDefaultImageResId(int id) {
		defaultImageResId = id;
	}

	

	public static IJsonTaskManager getInstance() {
		return instance;
	}

	public static boolean hasLogin() {
		return instance.isLogin();
	}
	

	@Override
	public void setImageSrc(String url, ImageView imageView) {
        DMLib.getJobManager().loadImage(url,imageView);

	}

	@Override
	public void setImageSrc(String url, ImageView imageView, int defaultResId) {
        DMLib.getJobManager().loadImage(url,imageView);
	}

	

	
	@Override
	public IDestroyable loadImage(String url, final ImageRequestLisener listener){
		return DMLib.getJobManager().loadImage(url, new JobListener<HttpJob>() {
            @Override
            public boolean onJobError(HttpJob job) {
                listener.onImageError(job.getUrl(),true);
                return false;
            }

            @Override
            public void onJobProgress(HttpJob job) {
                listener.onImageProgress(job.getUrl(),job.getTotalBytes(),job.getCurrentBytes());
            }

            @Override
            public void onJobSuccess(HttpJob job) {
                Bitmap bitmap = job.getData();
                listener.onImageSuccess(job.getUrl(),bitmap);
            }
        });
	}
	

	@Override
	public void destroy() {
		taskMap.clear();
		clazz = null;
		taskMap = null;
		currentContext = null;
		appContext = null;
		application = null;
	}



	@Override
	public void requestLogin(Activity context) {
		callLoginActivity(context);
	}

	@Override
	public List<Activity> getRunningActivities() {
		List<Activity> list = new LinkedList<Activity>();
		for (Object iterable_element : taskMap.keySet()) {
			if (iterable_element instanceof Activity) {
				list.add((Activity) iterable_element);
			}
		}
		return list;
	}

	// 退出程序
	@Override
	public void exit(Context context) {
		List<Activity> list = getRunningActivities();
		for (Activity activity : list) {
			int i = 0;
			i++;
			activity.finish();
			LogFactory.setConfig(ILog.Level_Info, ILog.Type_File, context);
			LogFactory.getLog(activity.getClass()).info(activity.getClass().getName() + i +" : finished");
		}

		System.exit(0);
	}

	@Override
	public void setUserInfoClass(Class<? extends DMAccount> clazz) {
		this.clazz = clazz;
	}

	/**
	 * 清空缓存
	 */
	public void clearCache() {
	//	cache.clear();
	//	VersionManager.getInstance().clear();
	}

	@Override
	public void setBaseUrl(String baseUrl) {
		JsonTaskManager.baseUrl = baseUrl;
	}


	@Override
	public void setApplication(Application app) {
		Http.setJsonTaskManager(this);
		ModelHelper.setJsonTaskManager(this);
		ViewDataSetter.setJsonTaskManager(this);
		BaseEventHandler.setJsonTaskManager(this);
		AutoCreator.setItemEventHandlerCreator(BaseItemEventHandler.CREATOR);
		ViewUtil.setBinddataViewSetterCreator(ViewDataSetter.CREATOR);
		ViewUtil.setEventHandler(BaseEventHandler.CREATOR);
		
		appContext = app.getApplicationContext();
		jsonParserMap = new LinkedHashMap<Class<?>, IJsonParser<?>>();

	//	cache = new DiskCache(appContext);
		
	/*	if(false){
			network = new HttpUrlConnectionNetwork();
		}else{
			network = new HttpClientNetwork(new PersistentCookieStore(appContext));
		}*/
	//	requestQueue = new DefaultRequestQueue(app.getApplicationContext(),cache,network);
		
		application = app;

		LocalData.setContext(appContext);
		taskMap = new HashMap<Object, Map<String, Request>>();
		currentContext = app.getApplicationContext();
		// 设置application
	//	VersionManager.getInstance().setAppContext(appContext);

		// 加载用户信息
		
		
	/*	factories = new IJsonTaskFactory[2];
		factories[0] = new DefaultJsonTaskFactory(this, baseUrl);
		factories[1] = new JavaTaskFactory(this, javaServerUrl);
	*/
	}
	
	

	@SuppressWarnings("unchecked")
	public <T> IJsonParser<T> createParser(Class<T> clazz) {
		if (jsonParserMap.containsKey(clazz)) {
			return (IJsonParser<T>) jsonParserMap.get(clazz);
		}
		IJsonParser<T> parser = null;
		if (clazz.isAnnotationPresent(JsonVo.class)) {
			parser = new AnnotationParser<T>(clazz);
		} else {
			parser = new DefaultJsonParser<T>(clazz);
		}

		jsonParserMap.put(clazz, parser);
		return parser;
	}

	protected DMAccount loadUserInfo() {
		return DMAccount.get();
	}

	public static Application getApplication() {
		return application;
	}

	public static Context getApplicationContext() {
		return appContext;
	}

	@Override
	public Object getCurrentContext() {
		return currentContext;
	}


	/**
	 * 查找api
	 * 
	 * @param api
	 * @return
	 */
	protected AbsJsonTask<?> findTask(String api) {
		if (currentContext != null) {
			Map<String, Request> thisMap = taskMap.get(currentContext);
			if (thisMap == null) {
				thisMap = new HashMap<String, Request>();
				taskMap.put(currentContext, thisMap);
			}
			return (AbsJsonTask<?>) thisMap.get(api);
		}

		// 如果没有知道到
		for (Map<String, Request> thisMap : taskMap.values()) {
			if (thisMap.containsKey(api)) {
				return (AbsJsonTask<?>) thisMap.get(api);
			}
		}

		return null;

	}

	protected Request getTask(String key) {
		if (currentContext != null) {
			Map<String, Request> thisMap = taskMap.get(currentContext);
			if (thisMap == null) {
				thisMap = new HashMap<String, Request>();
				taskMap.put(currentContext, thisMap);
			}
			return thisMap.get(key);
		}

		return null;
	}

	@Override
	public void cancel(Object context) {

	}

	@Override
	public void onPause(Activity context) {
		// client.cancelRequests(context, true);
		if (cycleListener != null) {
			cycleListener.onPause(context);
		}
	}

	@Override
	public void onCreate(Activity context) {
		currentContext = context;
		taskMap.put(context, new HashMap<String, Request>());
		if (cycleListener != null) {
			cycleListener.onCreate(context);
		}
	}

	@Override
	public void onResume(Activity context) {
		currentContext = context;
		if (cycleListener != null) {
			cycleListener.onResume(context);
		}
	}

	private IActivityCycleListener cycleListener;

	@Override
	public void setActivityCycleListener(IActivityCycleListener listener) {
		cycleListener = listener;
	}

	@Override
	public void cancelRequest() {
	//	requestQueue.cancelRequests(currentContext);
		// client.cancelRequests(currentContext, true);
	}

	@Override
	public void onDestroy(Activity context) {
		if (cycleListener != null) {
			cycleListener.onDestroy(context);
		}
		loginListenerReference = null;
	//	requestQueue.cancelRequests(context);
		Map<String, Request> tasks = taskMap.get(context);
		if (tasks != null) {
			Iterator<Entry<String, Request>> iter = tasks.entrySet().iterator();
			while (iter.hasNext()) {
				Entry<String, Request> entry = iter.next();
				Request val = entry.getValue();
				val.destroy();
				iter.remove();
			}
			taskMap.remove(context);
		}

	}


	@Override
	public String getDeviceID() {
		return Push.getUdid();
	}
	
	public IValueJsonTask<Object> newTask(String api){
		return new ValueJsonTask(DMLib.getJobManager().createArrayApi(api));
	}

	@Override
	public IValueJsonTask<Object> createValueJsonTask(String api,int factory) {
		ValueJsonTask task = (ValueJsonTask) getTask(api);
		if (task == null) {
			task = new ValueJsonTask(DMLib.getJobManager().createObjectApi(api));
			task.setCachePolicy(CachePolicy.CachePolity_NoCache);
			onCreateTask(task,api, false,factory);
		}
		return task;
	}

	@Override
	public IValueJsonTask<Integer> createIntegerJsonTask(String api,int factory){
		IntegerJsonTask task = (IntegerJsonTask) getTask(api);
		if (task == null) {
			task = new IntegerJsonTask(DMLib.getJobManager().createObjectApi(api));
			task.setCachePolicy(CachePolicy.CachePolity_NoCache);
			onCreateTask(task,  api, false,factory);
		}
		return task;
	}
	@Override
	public IValueJsonTask<Boolean> createBooleanJsonTask(String api,int factory){
		BooleanJsonTask task = (BooleanJsonTask) getTask(api);
		if (task == null) {
			task = new BooleanJsonTask(DMLib.getJobManager().createObjectApi(api));
			task.setCachePolicy(CachePolicy.CachePolity_NoCache);
			onCreateTask(task, api, false,factory);
		}
		return task;
	}
	@Override
	public <T> ArrayJsonTask<T> createArrayJsonTask(String api,
			CachePolicy cachePolicy, boolean isPrivate,int factory) {
		@SuppressWarnings("unchecked")
		ArrayJsonTask<T> task = (ArrayJsonTask<T>) getTask(api);
		if (task == null) {
			// 从缓存池中取出任务
			task = new ArrayJsonTask<T>(DMLib.getJobManager().createArrayApi(api));
			onCreateTask(task,  api, isPrivate,factory);
			task.setCachePolicy(cachePolicy);
		}
		return task;
	}

	@Override
	public <T> ArrayJsonTask<T> createArrayJsonTask(String api,
			CachePolicy cachePolicy, boolean isPrivate, Class<T> clazz,int factory) {
		@SuppressWarnings("unchecked")
		ArrayJsonTask<T> task = (ArrayJsonTask<T>) getTask(api);
		if (task == null) {
			// 从缓存池中取出任务
			task = new ArrayJsonTask<T>(DMLib.getJobManager().createArrayApi(api));
			onCreateTask(task,api, isPrivate,factory);
			task.setParser(createParser(clazz));
			task.setCachePolicy(cachePolicy);
		}
		return task;
	}

	public void onCreateTask(AbsJsonTask<?> task, String api,
			boolean isPrivate,int factory) {
	//	task.setApi(api);
        task.setFactory(factory);
	//	factories[factory].onCreateTask(task, api);
		
		task.setTag(currentContext);
		DMAccount account = DMAccount.get();
		if(account!=null && TextUtils.isEmpty(account.getId()) && isPrivate){
			task.setUserId(account.getId());
		}
	//	task.setDeviceID(getDeviceID());
	//	task.setVersionManager(VersionManager.getInstance());
	//	task.setRequestQueue(requestQueue);

		Map<String, Request> tasks = taskMap.get(currentContext);
		tasks.put(api, task);
	}
	


	@Override
	public <T> ObjectJsonTask<T> createObjectJsonTask(String api,
			CachePolicy cachePolicy, Class<T> clazz,int factory) {
		@SuppressWarnings("unchecked")
		ObjectJsonTask<T> task = (ObjectJsonTask<T>) getTask(api);
		if (task == null) {
			task = new ObjectJsonTask<T>(DMLib.getJobManager().createObjectApi(api));
			task.setCachePolicy(cachePolicy);
			task.setParser(createParser(clazz));
			onCreateTask(task, api, false,factory);
		}
		return task;
	}

	public <T> ObjectJsonTask<T> createObjectJsonTask(String api,
			CachePolicy cachePolicy,int factory) {
		@SuppressWarnings("unchecked")
		ObjectJsonTask<T> task = (ObjectJsonTask<T>) getTask(api);
		if (task == null) {
			task = new ObjectJsonTask<T>(DMLib.getJobManager().createObjectApi(api));
			task.setCachePolicy(cachePolicy);
			onCreateTask(task, api, false,factory);
		}
		return task;
	}

	@Override
	public boolean onNotLogin(AbsJsonTask<?> task) {
		// 先登录，然后再执行一遍task
		if (DMAccount.isLogin()) {
			currentTask = new WeakReference<AbsJsonTask<?>>(task);
			autoLogin(getLoginApi(), loginListener, true);
			return true;
		}
		return false;
	}

	/*
	 * private WeakReference<IJsonTask> currentTask;
	 */
	/**
	 * 自动登录
	 */

	protected void autoLogin(String api, IRequestResult<DMAccount> listener,
			boolean auto) {
		ObjectJsonTask<DMAccount> loginTask = createObjectJsonTask(api,
				CachePolicy.CachePolity_NoCache,0);
		loginTask.setParser(this);
		loginTask.setListener(listener);
		if (auto) {
			loginTask.setWaitMessage(null);
		} else {
			loginTask.setWaitMessage("正在登录...");
		}
		onBeforeLogin(loginTask, DMAccount.get());
		loginTask.put(PUSH_ID, getPushID());
		loginTask.enableAutoNotify();
		loginTask.execute();
	}

	protected IRequestResult<DMAccount> loginListener = new IRequestResult<DMAccount>() {

		private WeakReference<Activity> wContext;

		@Override
		public void onRequestSuccess(DMAccount result) {
			// 重新执行一下
			JsonTaskManager.this.onRequestSuccess(result);
			executeCurrentTask();
		}

		@Override
		public void onRequestError(String errMsg, boolean isNetworkError) {
			if (isNetworkError) {
				onCurrentError(errMsg, isNetworkError);
			} else {
				Activity context = null;
				if (currentContext instanceof Activity) {
					context = (Activity) currentContext;
				} else {
					Alert.showShortToast("您的登录信息已经过期,请重新登录");
					return;
				}
				wContext = new WeakReference<Activity>(context);
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				AlertDialog mAlertDialog = builder
						.setTitle("登录错误")
						.setPositiveButton(android.R.string.ok,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										callLoginActivity(wContext.get());
										wContext.clear();
										wContext = null;
									}
								}).setMessage("您的登录信息已经过期,请重新登录")
						.setOnCancelListener(new OnCancelListener() {
							@Override
							public void onCancel(DialogInterface dialog) {
								wContext.get().finish();
								wContext.clear();
								wContext = null;
							}
						}).create();
				mAlertDialog.show();
			}

		}
	};
	protected WeakReference<AbsJsonTask<?>> currentTask;

	protected void executeCurrentTask() {
		if (currentTask != null) {
			AbsJsonTask<?> task = currentTask.get();
			if (task != null && !task.isDestroyed()) {
				task.reload();
			}
			currentTask.clear();
			currentTask = null;
		}
	}

	protected void onCurrentError(String error, boolean isNetworkError) {
		if (currentTask != null) {
			AbsJsonTask<?> task = currentTask.get();
		/*	if (task != null) {
				requestQueue.onError(task, error, isNetworkError);
			}*/
			currentTask.clear();
			currentTask = null;
		}
	}

	
	@Override
	public void setImageCache(ImageCache imageCache){
		this.imageCache = imageCache;
	}
	/**
	 * 登录activity
	 */
	protected abstract void callLoginActivity(Activity context);

	protected abstract void onBeforeLogin(IJsonTask loginTask, DMAccount userInfo);

	protected abstract String getLoginApi();

	protected WeakReference<IRequestResult<DMAccount>> loginListenerReference;

	/**
	 * 登录,外部调用
	 * 
	 * @param username
	 * @param pwd
	 */
	@Override
	public void login(String username, String pwd,
			IRequestResult<DMAccount> listener) {
		loginListenerReference = new WeakReference<IRequestResult<DMAccount>>(
				listener);
		preLogin(username, pwd);
	}

	@Override
	public void login(String username, String pwd) {
		loginListenerReference = null;
		preLogin(username, pwd);
	}

	/**
	 * 自动登录
	 * 
	 * @param username
	 * @param pwd
	 */
	void preLogin(String username, String pwd) {
		pwd = MD5Util.md5Appkey(pwd);
		DMAccount account = DMAccount.get();
		account.setAccount(username);
		account.setPassword(pwd);
		autoLogin(getLoginApi(), this, false);
	}

	@Override
	public void onRequestSuccess(DMAccount result) {
		DMAccount account = DMAccount.get();
		account.setId(result.getId());
		account.setAccount(result.getAccount());
		if (loginListenerReference != null) {
			IRequestResult<DMAccount> l = loginListenerReference.get();
			if (l != null) {
				l.onRequestSuccess(result);
			}
		}
		
		Notifier.notifyObservers(LOGIN_SUCCESS, account);
	}

	@Override
	public DMAccount parseResult(JSONObject json) throws JSONException {
		DMAccount userInfo = DMAccount.get();
		onParseUserInfo(userInfo, json);
		saveUserInfo(userInfo);
		return userInfo;
	}

	protected abstract void onParseUserInfo(DMAccount userInfo, JSONObject json)
			throws JSONException;

	/**
	 * 保存到缓存
	 */
	protected void saveUserInfo(DMAccount userInfo) {
		userInfo.save();
	}

	@Override
	public void onRequestError(String errMsg, boolean isNetworkError) {
		if (loginListenerReference != null) {
			IRequestResult<DMAccount> l = loginListenerReference.get();
			if (l != null) {
				l.onRequestError(errMsg, isNetworkError);
			}
		}
	}

	//
	abstract public String getPushID();

	protected Class<? extends DMAccount> clazz;

	protected DMAccount createUserInfo() {
		return DMAccount.get();
	}

	/**
	 * 登出
	 */
	@Override
	public void logout() {

		clearSession();
		
		Notifier.notifyObservers(LOGOUT);
		
		

	}

	// 设置用户信息
	@Override
	public void onSetUserInfo(IOnResume listener) {
		listener.onResumeSetData(DMAccount.get());
	}

	/**
	 * 获取用户信息
	 * 
	 * @return
	 */
	@Override
	public <T extends DMAccount> T getUserInfo() {
		return DMAccount.get();
	}
	/**
	 * 移除缓存,匹配模式
	 */
	public void removeAllCache(String key){
		//getCache().removeAllCache(key);
     //   DMLib.getJobManager().clearCache(key);
	}


	/**
	 * 是否登录
	 */
	@Override
	public boolean isLogin() {
		return DMAccount.isLogin();
	}

	/**
	 * 判断activity是否运行
	 * 
	 * @param clazz
	 * @return
	 */
	@Override
	public boolean isRunning(Class<? extends Activity> clazz) {
		for (Activity iterable_element : getRunningActivities()) {
			if (iterable_element.getClass().equals(clazz))
				return true;
		}
		return false;
	}

	@Override
	public <T> IDetailJsonTask<T> createDetailJsonTask(String api,
			CachePolicy cachePolicy, Class<T> clazz,int factory) {
		@SuppressWarnings("unchecked")
		DetailJsonTask<T> task = (DetailJsonTask<T>) getTask(api);
		if (task == null) {
			task = new DetailJsonTask<T>(DMLib.getJobManager().createObjectApi(api));
			task.setCachePolicy(cachePolicy);
			task.setParser(createParser(clazz));
			onCreateTask(task, api, false,factory);
		}
		return task;
	}

	private Map<Class<?>, Object> modelMap = new LinkedHashMap<Class<?>, Object>();

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getModel(Class<T> modelClass) {
		if (modelMap.containsKey(modelClass)) {
			return (T) modelMap.get(modelClass);
		}
		synchronized (modelMap) {
			if (!modelMap.containsKey(modelClass)) {
				T modelT;
				try {
					modelT = createModel(modelClass);
					modelMap.put(modelClass, modelT);
					return modelT;
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return (T) modelMap.get(modelClass);
		}

	}

	@SuppressWarnings("unchecked")
	public <T> T createModel(Class<T> modelClass)
			throws InstantiationException, IllegalAccessException {
		if(modelClass.isInterface()){
			return (T) Proxy.newProxyInstance(modelClass.getClassLoader(),
					new Class<?>[] { modelClass }, new MyProxyHandler(
							getMethodInfoCache(modelClass)));
		}
		
		return modelClass.newInstance();
	}

	MethodInfo getMethodInfo(Map<Method, MethodInfo> cache, Method method) {
		//synchronized (cache) {
			MethodInfo methodInfo = cache.get(method);
			if (methodInfo == null) {
				methodInfo = createMethodInfo(method);
				cache.put(method, methodInfo);
			}
			return methodInfo;
		//}
	}

	private Map<Class<?>, Map<Method, MethodInfo>> serviceMethodInfoCache = new HashMap<Class<?>, Map<Method, MethodInfo>>();

	/**
	 * 创建methodinfo
	 * 
	 * @param method
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected MethodInfo createMethodInfo(Method method) {
		MethodInfo result = null;
		Crypt crypt = method.getAnnotation(Crypt.class);
		if (method.isAnnotationPresent(ApiArray.class)) {
			
			
			ArrayMethodInfo info = new ArrayMethodInfo();
			ApiArray task = method.getAnnotation(ApiArray.class);
			info.api = task.api();
			info.cachePolicy = task.cachePolicy();
			info.isPrivate = task.isPrivate();
			info.clazz = task.clazz();
			info.paged = task.paged();
			info.params = task.params();
			info.factory = task.factory();
			info.crypt = crypt==null?0:crypt.value();
			if(!task.waitingMessage().equals("")){
				info.waitingMessage = task.waitingMessage();
			}
			result = info;
		} else if (method.isAnnotationPresent(ApiDetail.class)) {
			DetailMethodInfo info = new DetailMethodInfo();
			ApiDetail task = method.getAnnotation(ApiDetail.class);
			info.api = task.api();
			info.cachePolicy = task.cachePolicy();
			info.clazz = task.clazz();
			info.params = task.params();
			info.factory = task.factory();
			info.crypt = crypt==null?0:crypt.value();
			if(!task.waitingMessage().equals("")){
				info.waitingMessage = task.waitingMessage();
			}
			result = info;

		} else if (method.isAnnotationPresent(ApiObject.class)) {

			ApiObject task = method.getAnnotation(ApiObject.class);
			ObjectMethodInfo info = new ObjectMethodInfo();
			info.api = task.api();
			info.cachePolicy = task.cachePolicy();
			info.params = task.params();
			info.clazz = task.clazz();
			info.factory = task.factory();
			info.crypt = crypt==null?0:crypt.value();
			if(!task.waitingMessage().equals("")){
				info.waitingMessage = task.waitingMessage();
			}
			result = info;
			
			
		} else if (method.isAnnotationPresent(ApiValue.class)) {
			ApiValue task = method.getAnnotation(ApiValue.class);
			ValueMethodInfo info = new ValueMethodInfo();
			info.api = task.api();
			info.cachePolicy = task.cachePolicy();
			info.params = task.params();
			info.factory = task.factory();
			info.crypt = crypt==null?0:crypt.value();
			if(!task.waitingMessage().equals("")){
				info.waitingMessage = task.waitingMessage();
			}
			result = info;
		}
		
		if(method.isAnnotationPresent(ApiSuccess.class)){
			Notifier.register( method.getAnnotation(ApiSuccess.class).value());
		}
		
		Class<?> returnType = method.getReturnType();
		if(returnType.getName().equals("void")){
			result.isReturn = false;
		}else{
			result.isReturn = true;
		}
		
		return result;
	}
	
	
	
	
	/**
	 * 解析method
	 * 
	 * @author Randy
	 * 
	 */
	protected abstract class MethodInfo implements InvocationHandler {
		public Class<? extends Command>[] apiSuccess;
		public boolean isReturn;
		public String api;
		public boolean isPrivate;
		public CachePolicy cachePolicy;
		public String[] params;
		public String waitingMessage;
		public int factory;
		public int crypt;
	}

	protected class ValueMethodInfo<T> extends MethodInfo {
		protected IValueJsonTask<Object> createTask(String api,int factory) {
			return createValueJsonTask(api,factory);
		}

		@SuppressWarnings("unchecked")
		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			IValueJsonTask<Object> task = createTask(api,factory);
			((AbsJsonTask<?>)task).setFactory(factory);
			task.enableAutoNotify();
			task.setWaitMessage(waitingMessage);
			task.setCrypt(crypt);
			int count = params.length;
			for (int i = 0; i < count; ++i) {
				if (params[i].equals("")) {
					task.putAll((Map<String, Object>) args[i]);
				} else {
					task.put(params[i], args[i]);
				}
			}
			task.execute();

			return null;
		}
	}

	protected class ObjectMethodInfo<T> extends MethodInfo {
		public Class<?> clazz;

		@SuppressWarnings("unchecked")
		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			IObjectJsonTask<T> task = (IObjectJsonTask<T>) createObjectJsonTask(
					api, cachePolicy, clazz,factory);
			task.enableAutoNotify();
			((AbsJsonTask<?>)task).setFactory(factory);
			task.setWaitMessage(waitingMessage);
			task.setCrypt(crypt);
			for (int i = 0, count = params.length; i < count; ++i) {
				task.put(params[i], args[i]);
			}
			task.execute();
			return isReturn ? task : null;
		}

	}

	protected class DetailMethodInfo<T> extends MethodInfo {
		public Class<?> clazz;

		@SuppressWarnings("unchecked")
		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			IDetailJsonTask<T> task = (IDetailJsonTask<T>) createDetailJsonTask(
					api, cachePolicy, clazz,factory);
			((AbsJsonTask<?>)task).setFactory(factory);
			task.setId(params[0], args[0]);
			task.enableAutoNotify();
			task.setCrypt(crypt);
			for (int i = 1, count = params.length; i < count; ++i) {
				task.put(params[i], args[i]);
			}
			task.execute();
			return isReturn ? task : null;
		}

	}

	protected class ArrayMethodInfo<T> extends MethodInfo {

		public Class<?> clazz;
		public boolean paged;

		@SuppressWarnings("unchecked")
		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			IArrayJsonTask<T> task = (IArrayJsonTask<T>) createArrayJsonTask(api, cachePolicy, isPrivate, clazz,factory);
			((AbsJsonTask<?>)task).setFactory(factory);
			if (cachePolicy != CachePolicy.CachePolity_NoCache) {
				task.setCondition(params);
			}
			task.setCrypt(crypt);
			task.enableAutoNotify();
			int count = params.length;
			if (paged) {
				task.setPosition((Integer) args[0]);
				for (int i = 0; i < count; ++i) {
					task.put(params[i], args[i + 1]);
				}
			} else {
				for (int i = 0; i < count; ++i) {
					task.put(params[i], args[i]);
				}
			}

			task.execute();

			return isReturn ? task : null;
		}
	}

	Map<Method, MethodInfo> getMethodInfoCache(Class<?> service) {
		synchronized (serviceMethodInfoCache) {
			Map<Method, MethodInfo> methodInfoCache = serviceMethodInfoCache
					.get(service);
			if (methodInfoCache == null) {
				methodInfoCache = new LinkedHashMap<Method, MethodInfo>();
				serviceMethodInfoCache.put(service, methodInfoCache);
			}
			return methodInfoCache;
		}
	}

	class MyProxyHandler implements InvocationHandler {
		private Map<Method, MethodInfo> methodDetailsCache;

		public MyProxyHandler(Map<Method, MethodInfo> param) {
			methodDetailsCache = param;
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			// If the method is a method from Object then defer to normal
			// invocation.
			if (method.getDeclaringClass() == Object.class) { // 1
				return method.invoke(this, args);
			}

			// Load or create the details cache for the current method.
			final MethodInfo methodInfo = getMethodInfo(methodDetailsCache,
					method); // 2

			return methodInfo.invoke(proxy, method, args);
		}

	}

	public void unregisterModel(Class<?> clazz){
		Object model = modelMap.get(clazz);
		if(model!=null){
			if(model instanceof IDestroyable){
				((IDestroyable)model).destroy();
			}
			modelMap.remove(clazz);
		}
		
	}
	
	
	
	public IValueJsonTask<Object> createValueJsonTask(String api){
		return createValueJsonTask(api,0);
	}
	public IValueJsonTask<Integer> createIntegerJsonTask(String api){
		return createIntegerJsonTask(api,0);
	}
	public IValueJsonTask<Boolean> createBooleanJsonTask(String api){
		return createBooleanJsonTask(api,0);
	}

	public <T> IArrayJsonTask<T> createArrayJsonTask(String api, CachePolicy cachePolicy,boolean isPrivate){
		return createArrayJsonTask(api,cachePolicy,isPrivate,0);
	}
	public <T> IArrayJsonTask<T> createArrayJsonTask(String api, CachePolicy cachePolicy,boolean isPrivate,Class<T> clazz){
	
		return createArrayJsonTask(api,cachePolicy,isPrivate,clazz,0);
	}
	public <T> IDetailJsonTask<T> createDetailJsonTask(String api,CachePolicy cachePolicy,Class<T> clazz){
		return createDetailJsonTask(api,cachePolicy,clazz,0);
	}
	
	public <T> IObjectJsonTask<T> createObjectJsonTask(String api,CachePolicy cachePolicy,Class<T> clazz){
		return createObjectJsonTask(api, cachePolicy, clazz,0);
	}

}
