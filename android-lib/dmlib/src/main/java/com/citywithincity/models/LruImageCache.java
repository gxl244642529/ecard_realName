package com.citywithincity.models;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.damai.http.cache.ImageCache;

public class LruImageCache implements ImageCache {
	protected LruCache<String, Bitmap> imageCache2 = new LruCache<String, Bitmap>(
			30);
	public Bitmap getBitmap(String url) {
		return imageCache2.get(url);
	}

	public void putBitmap(String url, Bitmap bitmap) {
		imageCache2.put(url, bitmap);
	}

}
