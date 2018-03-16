package com.damai.core;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;

import com.citywithincity.interfaces.IViewContainer;
import com.citywithincity.models.cache.CachePolicy;
import com.citywithincity.paylib.PayType;
import com.citywithincity.utils.Alert;
import com.damai.alert.AlertImpl;
import com.damai.alert.WaitImpl;
import com.damai.auto.LifeManager;
import com.damai.cache.MemoryCache;
import com.damai.dl.HostFragment;
import com.damai.dl.IPluginFragment;
import com.damai.helper.AdapterFactory;
import com.damai.helper.DMAdapter;
import com.damai.helper.DMAdapterFactory;
import com.damai.helper.DMAdapterFactory.IDMAdapterFactory;
import com.damai.lib.R;
import com.damai.note.ClassParser;
import com.damai.pay.AbsDMPay;
import com.damai.pay.DMPayFactory;
import com.damai.pay.DMPayModel;
import com.damai.pay.DMPayModelImpl;
import com.damai.pay.platform.AliPay;
import com.damai.pay.platform.WXPay;
import com.damai.util.FragmentUtil;
import com.damai.util.FragmentUtil.IFragmentAction;
import com.damai.widget.proxy.WidgetFactoryImpl;
import com.damai.widget.proxy.WidgetProxy;

public class JobManagerImpl implements JobManager, JobListener<JobImpl>,
		OnApiMessageListener, Callback, IJobLife, DMPayFactory, IDMAdapterFactory, IFragmentAction {
	/**
	 * 线程数量
	 */
	public static final int THREAD_COUNT = 4;

	private ArrayContainer<JobQueue> queues;
	private JobQueue[] jobQueues;
	private SimpleJobQueue cacheQueue;
	/**
	 * 优先级最高,一般来说，处理api任务
	 */
	private JobQueue highQueue;

	/**
	 * 优先级最低,一般来说，处理其他任务
	 */
	private JobQueue lowQueue;

	@SuppressWarnings("rawtypes")
	private ArrayContainer<DataParser> parserArrayContainer;
	private ArrayContainer<JobListener<JobImpl>> jobListenerArrayContainer;
	private ApiHandler apiHandler;

	private JobListener<JobImpl>[] jobListeners;
	/**
     *
     */
	public static final int PRORITY_HEIGH = 0;
	/**
     *
     */
	public static final int PRORITY_LOW = 1;

	private Cache cache;

	protected final Handler handler;

	private ImageLoader imageLoader;
	private static int DEFAULT_POOL_SIZE = 4096;
	private final ByteArrayPool mPool = new ByteArrayPool(DEFAULT_POOL_SIZE);
	private ApiDelivery apiDelivery;
	private Context appContext;

	public JobManagerImpl() {
		FragmentUtil.setFragmentAction(this);
		queues = new ArrayContainer<JobQueue>();
		highQueue = new JobQueue();
		lowQueue = new JobQueue();
		parserArrayContainer = new ArrayContainer<DataParser>();
		jobListenerArrayContainer = new ArrayContainer<JobListener<JobImpl>>();
		apiHandler = new ApiHandler();
		handler = new Handler(Looper.getMainLooper(), this);
	}

	/**
	 * 注册api处理器
	 * 
	 * @param server  服务器下标
	 * @param serverHandler api处理器
	 */
	public void registerApiServerHandler(int server,
			ApiServerHandler serverHandler) {

		DMServerManager.registerServer(server, serverHandler);
		apiHandler.register(server, serverHandler);

	}

	/**
	 * 注册任务类型在哪个队列中处理 每一个线程都对应一个handler组 如果一个handler需要被多个线程处理，那么应该执行多次，
	 * 每一次的JobHander都为new出来的新对象
	 * 
	 * @param handlerType
	 */
	public void registerHandlerType(int handlerType, int prority,
			JobHandler<? extends JobImpl> handler) {
		if (prority == PRORITY_HEIGH) {
			queues.register(handlerType, highQueue);
			highQueue.register(handlerType, handler);
		} else {
			queues.register(handlerType, lowQueue);
			lowQueue.register(handlerType, handler);
		}
	}

	/**
	 * 注册任务投递方式 应该是线程安全的
	 */
	public void registerDeliverType(int deliverType,
			JobListener<? extends JobImpl> listener) {
		jobListenerArrayContainer.register(deliverType, listener);
	}

	/**
	 * 注册数据解析方式(只有网络任务才有这个） 应该是线程安全的
	 * 
	 * @param dataType
	 * @param parser
	 */
	@SuppressWarnings("rawtypes")
	public void registerDataParser(int dataType, DataParser parser) {
		parserArrayContainer.register(dataType, parser);
	}

	/**
	 * 启动
	 * 
	 * @param appContext
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void startup(Context appContext) {
	
		this.appContext = appContext.getApplicationContext();
		DMAdapterFactory.setFactory(this);
		Alert.setApplicationContext(appContext);
		Alert.setAlert(new AlertImpl());
		Alert.setWait(new WaitImpl());
		try {
			cache = new DiskCache(appContext);
		} catch (IOException e) {
			cache = new MemoryCache();
		}

		for (ApiServerHandler serverHandler : apiHandler.getHandlers()) {
			if (serverHandler != null) {
				serverHandler.initParam(this, this, mPool, cache);
			}

		}

		IdReflectImpl idReflectImpl = new IdReflectImpl();
		idReflectImpl.init(appContext.getResources(),
				appContext.getPackageName(), appContext.getClassLoader());
		IdManager.setDefault(idReflectImpl);

		WidgetProxy.setFactory(new WidgetFactoryImpl());

		registerDataParser(DataTypes.DataType_Image, new ImageDataParser());
		registerDataParser(DataTypes.DataType_Text, new TextDataParser());
		registerDataParser(DataTypes.DataType_Json, new JsonDataParser());
		registerDataParser(DataTypes.DataType_File, new FileDataParser());

		registerHandlerType(HandlerTypes.HandlerType_Api, PRORITY_HEIGH,
				apiHandler);
		imageLoader = new ImageLoader(this);
		registerDeliverType(DeliverTypes.DeliverType_Image, imageLoader);
		registerDeliverType(DeliverTypes.DeliverType_Normal,new DefaultJobDelivery(this));
		apiDelivery = new ApiDelivery(this, ClassParser.getNotificationParser());
		registerDeliverType(DeliverTypes.DeliverType_Api, apiDelivery);

		DMServerManager.getApiParsers(DataTypes.DataType_ApiArray);
		parserArrayContainer.register(
				DataTypes.DataType_ApiArray,
				new ApiDataParser(DMServerManager
						.getApiParsers(DataTypes.DataType_ApiArray)));
		parserArrayContainer.register(
				DataTypes.DataType_ApiObject,
				new ApiDataParser(DMServerManager
						.getApiParsers(DataTypes.DataType_ApiObject)));
		parserArrayContainer.register(
				DataTypes.DataType_ApiPage,
				new ApiDataParser(DMServerManager
						.getApiParsers(DataTypes.DataType_ApiPage)));

		DataParser[] parsers = parserArrayContainer
				.toArray(new DataParser[parserArrayContainer.size()]);
		jobListeners = new JobListener[jobListenerArrayContainer.size()];
		jobListenerArrayContainer.toArray(jobListeners);

		CacheHandler cacheHandler = new CacheHandler(appContext, cache,
				parsers, this, this);
		cacheQueue = new SimpleJobQueue(cacheHandler);
		cacheQueue.start();

		for (int i = 0; i < THREAD_COUNT; ++i) {
			NetHandler netHandler = new NetHandler(cache, mPool, this, parsers);
			registerHandlerType(HandlerTypes.HandlerType_Http, PRORITY_LOW,
					netHandler);
		}

		queues.toArray(new JobQueue[queues.size()]);
		jobQueues = queues.getArray();

		lowQueue.start();
		highQueue.start();
	}

	public void clearSession() {
		apiHandler.clearSession();
	}

	public ApiJobImpl createObjectApi(String api) {
		return createJob(api, DataTypes.DataType_ApiObject);
	}

	public ApiJobImpl createPageApi(String api) {
		return createJob(api, DataTypes.DataType_ApiPage);
	}

	public ApiJobImpl createArrayApi(String api) {
		return createJob(api, DataTypes.DataType_ApiArray);
	}

	private ApiJobImpl createJob(String api, int type) {
		ApiJobImpl job = new ApiJobImpl();
		job.setApi(api);
		job.setRelated(LifeManager.getCurrent());
		job.dataType = type;
		return job;
	}

	/**
	 * 
	 * @param job
	 */
	public void addJob(JobImpl job) {
		onAddJob(job);
		if (job instanceof HttpJob) {
			// 根据缓存类型使用队列
			HttpJobImpl httpJob = (HttpJobImpl) job;
			if (httpJob.cachePolicy != CachePolicy.CachePolity_NoCache) {
				// 这里加入
				cacheQueue.add(httpJob);
				return;
			}
		}
		jobQueues[job.handlerType].add(job);

	}

	/**
	 * 直接加入网络队列
	 * 
	 * @param job
	 */
	public void reload(HttpJobImpl job) {
		onAddJob(job);
		jobQueues[job.handlerType].add(job);
	}

	private Set<JobImpl> paddingJobs = new HashSet<JobImpl>();

	private synchronized void onAddJob(JobImpl job) {

		paddingJobs.add(job);
	}

	public void onRemoveJob(JobImpl job) {
		synchronized (this) {
			paddingJobs.remove(job);
		}

		job.destroy();
	}

	/**
	 * 下载
	 */
	@Override
	public HttpJob download(String url, File destFile,
			JobListener<HttpJob> listener) {
		HttpJobImpl job = new HttpJobImpl();
		job.setOnJobSuccessListener(listener);
		job.setOnJobErrorListener(listener);
		job.setOnJobProgressListener(listener);
		job.cachePolicy = CachePolicy.CachePolity_NoCache;
		job.dataType = DataTypes.DataType_File;
		job.handlerType = (HandlerTypes.HandlerType_Http);
		job.deliverType = (DeliverTypes.DeliverType_Normal);
		job.url = url;
		job.data = destFile;
		job.related = (LifeManager.getCurrent());
		addJob(job);
		return job;
	}

	public JobImpl loadImage(String url, JobListener<HttpJob> listener) {
		HttpJobImpl job = new HttpJobImpl();
		job.setOnJobSuccessListener(listener);
		job.setOnJobErrorListener(listener);
		job.setOnJobProgressListener(listener);
		job.cachePolicy = CachePolicy.CachePolity_Permanent;
		job.dataType = DataTypes.DataType_Image;
		job.handlerType = (HandlerTypes.HandlerType_Http);
		job.deliverType = (DeliverTypes.DeliverType_Normal);
		job.url = url;
		job.related = (LifeManager.getCurrent());
		addJob(job);
		return job;
	}

	public void loadImage(String url, ImageView imageView) {
		imageLoader.loadImage(url, imageView);

	}

	@Override
	public void onJobSuccess(JobImpl job) {
		Message msg = handler.obtainMessage(DeliverMsgType.SUCCESS, job);
		handler.sendMessage(msg);
	}

	@Override
	public boolean onApiMessage(ApiJob job) {
		Message msg = handler.obtainMessage(DeliverMsgType.MESSAGE, job);
		handler.sendMessage(msg);
		return true;
	}

	@Override
	public void onJobProgress(JobImpl job) {
		if (job.onJobProgressListener != null) {
			Message msg = handler.obtainMessage(DeliverMsgType.PROGRESS, job);
			handler.sendMessage(msg);
		}
	}

	@Override
	public boolean onJobError(JobImpl job) {
		Message msg = handler.obtainMessage(DeliverMsgType.ERROR, job);
		handler.sendMessage(msg);
		return true;
	}

	private String pushId;

	public String getPushId() {
		return pushId == null ? "unbind" : pushId;
	}

	public void setPushId(String pushId) {
		this.pushId = pushId;
	}

	@Override
	public boolean handleMessage(Message msg) {
		JobImpl job = (JobImpl) msg.obj;

		if (job.isCanceled())
			return true;
		switch (msg.what) {
		case DeliverMsgType.SUCCESS: {
			jobListeners[job.getDeliverType()].onJobSuccess(job);
		}
			break;
		case DeliverMsgType.ERROR: {
			jobListeners[job.getDeliverType()].onJobError(job);
		}
			break;
		case DeliverMsgType.PROGRESS: {
			jobListeners[job.getDeliverType()].onJobProgress(job);
		}
			break;
		case DeliverMsgType.MESSAGE: {
			apiDelivery.onApiMessage((ApiJobImpl) job);
			break;
		}
		}

		return true;
	}

	public void clearCache() {
		cache.clear();
	}

	public JobImpl get(String url, CachePolicy cachePolicy,
			JobListener<HttpJob> listener) {
		HttpJobImpl job = new HttpJobImpl();
		job.setCachePolicy(cachePolicy);
		job.setUrl(url);
		job.setDataType(DataTypes.DataType_Text);
		job.setHandlerType(HandlerTypes.HandlerType_Http);
		job.setRelated(LifeManager.getCurrent());
		addJob(job);
		return job;
	}

	private DMPayFactory factory;

	public DMPayFactory getPayFactory() {
		return factory;
	}

	public void setPayFactory(DMPayFactory factory) {
		this.factory = factory;
	}

	@Override
	public void clearCache(ApiJob apiJob) {
		//
		cache.removeAllCache(apiJob.getCacheKey());
	}

	@Override
	public void onCreate(IViewContainer container) {
		LifeManager.onCreate(container);
		ClassParser.parse(container);
	}

	@Override
	public void onViewCreate(IViewContainer container) {

		ClassParser.register(container);
	}

	@Override
	public void stopRequest(Object context) {
		synchronized (paddingJobs) {
			Iterator<JobImpl> it = paddingJobs.iterator();
			while (it.hasNext()) {
				JobImpl job = it.next();
				if (job.getRelated() == context) {
					job.cancel();
					job.destroy();
					it.remove();
				}
			}
		}
	}
	@Override
	public void onDestroy(IViewContainer obj) {
		synchronized (paddingJobs) {
			Iterator<JobImpl> it = paddingJobs.iterator();
			while (it.hasNext()) {
				JobImpl job = it.next();
				if (job.getRelated() == obj) {
					job.cancel();
					job.destroy();
					it.remove();
				}
			}
		}
		imageLoader.onDestroy(obj);
		LifeManager.onDestroy(obj);
		ClassParser.unregister(obj);
	}

	@Override
	public void onResume(IViewContainer container) {
		LifeManager.onResume(container);
	}

	@Override
	public DMPayModel createPayModel(String moduleName, int[] supportPayTypes) {
		DMPayModelImpl model = new DMPayModelImpl();
		model.setFactory(this);
		model.setSupportPayTypes(supportPayTypes);
		model.setBusinessId(moduleName);
		return model;
	}



	/**
     *
     */
	@Override
	public AbsDMPay createPay(int type) {
		if (type == PayType.PAY_ALIPAY.value()) {
			return new AliPay(LifeManager.getActivity());
		} else if (type == PayType.PAY_WEIXIN.value()) {
			return new WXPay(LifeManager.getActivity(), wxId);
		}
		return factory.createPay(type);
	}

	private String wxId;

	public void setWXId(String wxId) {
		this.wxId = wxId;
	}

	@Override
	public Context getApplicationContext() {
		return appContext;
	}

	@Override
	public <T> DMAdapter<T> create(IViewContainer context, int itemResId) {
		return AdapterFactory.create(context, itemResId);
	}

	@Override
	public void addTo(Object fragment, Context context,boolean playAnimation, boolean addToBackStack) {
		HostFragment hostFragment = new HostFragment();
		hostFragment.setFragment((IPluginFragment)fragment);
		com.citywithincity.utils.FragmentUtil.addFragment((FragmentActivity)context, R.id._container,hostFragment,playAnimation,addToBackStack);
	}

	@Override
	public void replaceTo(Object fragment, Context context,boolean playAnimation, boolean addToBackStack) {
		HostFragment hostFragment = new HostFragment();
		hostFragment.setFragment((IPluginFragment)fragment);
		com.citywithincity.utils.FragmentUtil.replaceFragment((FragmentActivity)context, R.id._container,hostFragment,playAnimation,addToBackStack);
	}

}
