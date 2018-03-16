package com.damai.core;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.damai.dmlib.LibBuildConfig;

public class ImageCacheImpl implements ImageCache {
	private LruCache<String, Bitmap> cache;
	
	public ImageCacheImpl(){
		cache = new LruCache<String, Bitmap>(LibBuildConfig.MAX_IMAGE_CACHE_SIZE);
	}
	
	@Override
	public Bitmap get(String key) {
		return cache.get(key);
	}

	@Override
	public void put(String key, Bitmap data) {
		cache.put(key, data);
	}

}
