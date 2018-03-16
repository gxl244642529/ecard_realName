package com.damai.core;

import android.content.Context;

import com.citywithincity.models.cache.CachePolicy;
import com.citywithincity.utils.NetworkUtil;
import com.damai.core.Cache.CacheExpire;
import com.damai.core.Cache.CacheResult;
import com.damai.dmlib.DMParseException;

public class CacheHandler implements JobHandler<HttpJobImpl> {
	public static class Cache3GExpire implements CacheExpire {
		public static final int EXPIRE_TIME = 3 * 60 * 60 * 1000;

		@Override
		public boolean isExpire(long time) {
			return System.currentTimeMillis() - time > EXPIRE_TIME;
		}
	}

	public static class CacheWifiExpire implements CacheExpire {
		public static final int EXPIRE_TIME = 20 * 60 * 1000;

		@Override
		public boolean isExpire(long time) {
			return System.currentTimeMillis() - time > EXPIRE_TIME;
		}
	}

	public static class CachePermanentExpire implements CacheExpire {
		@Override
		public boolean isExpire(long time) {
			return false;
		}
	}

	public static final CacheExpire CACHE_3G = new Cache3GExpire();
	public static final CacheExpire CACHE_WIFI = new CacheWifiExpire();
	public static final CacheExpire CACHE_PERMANANT = new CachePermanentExpire();

	private Cache cache;
	private Context context;
	@SuppressWarnings("rawtypes")
	private DataParser[] dataParsers;
	private JobListener<JobImpl> listener;

	protected ApiParser[] parsers;
	protected JobManagerImpl jobManager;
	
	@SuppressWarnings("rawtypes")
	public CacheHandler(Context context,
			Cache cache,
			DataParser[] dataParsers,
			JobListener<JobImpl> listener,JobManagerImpl jobManager) {
		this.cache = cache;
		this.context = context.getApplicationContext();
		this.dataParsers = dataParsers;
		this.listener = listener;
		this.jobManager = jobManager;
		
	}

	private boolean checkAndParseCache(HttpJobImpl job) {
		String key = job.getCacheKey();
		CachePolicy policy = job.getCachePolicy();
		if(policy==null){
			return false;
		}
		switch (policy) {
		case CachePolicy_TimeLimit: {
			// 判断当前的网络环境
			CacheExpire cacheExpire;
			if (NetworkUtil.NETWORK_3G == NetworkUtil.getNetworkType(context)) {
				cacheExpire = CACHE_3G;
			} else {
				cacheExpire = CACHE_WIFI;
			}
			CacheResult result = cache.get(key, cacheExpire);
			if (result == null || result.isExpire()
					|| !parseResult(job, result.getData())) {
				return false;
			}
			return true;
		}
		case CachePolity_Permanent: {
			// 检查缓存，如果有则使用，没有的话，检查网络,如没有则返回
			byte[] content = cache.get(key);
			if (content != null && parseResult(job, content)) {
				return true;
			}
			// 解析
			return false;
		}
		case CachePolity_UseCacheFirst: {
			// 如果过期，则刷新
			CacheExpire cacheExpire;
			if (NetworkUtil.NETWORK_3G == NetworkUtil.getNetworkType(context)) {
				cacheExpire = CACHE_3G;
			} else {
				cacheExpire = CACHE_WIFI;
			}
			// 检查缓存
			CacheResult result = cache.get(key, cacheExpire);
			if (result == null) {
				return false;
			}
			// 解析，并返回
			if (result == null || !parseResult(job, result.getData())
					|| result.isExpire()) {
				return false;
			}
			return true;
		}
		case CachePolity_Offline: {
			// 检查缓存，如果有则使用，没有的话，检查网络,如没有则返回
			byte[] content = cache.get(key);
			if (content != null && parseResult(job, content)) {
				return true;
			}
			if (NetworkUtil.NETWORK_WIFI != NetworkUtil.getNetworkType(context)) {
				// 没有3g网络，酒退出
				return true;
			}
			// 有网络，下载
			return false;
		}
		default:
			break;
		}
		return false;
	}


	@SuppressWarnings("unchecked")
	private boolean parseResult(HttpJobImpl job, byte[] bs) {
		try {
			Object parsedData = null;
			// 使用数据类型对应的解析器解析
			parsedData = dataParsers[job.dataType].parseData(job, bs);
			
			// 投递
			job.data = parsedData;
			listener.onJobSuccess(job);
			return true;
		} catch (DMParseException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void handleJob(HttpJobImpl job) {
		if(checkAndParseCache(job)){
			
			return;
		}
		if(job.isCanceled()){
			return;
		}
		//添加到对应的队列中
		jobManager.reload(job);
	}

	@Override
	public void shutdown() {
		
	}

	

}
