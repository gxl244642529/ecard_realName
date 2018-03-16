package com.damai.core;

import android.graphics.Bitmap;

public interface ImageCache {
	Bitmap get(String key);
	void put(String key,Bitmap data);
}
