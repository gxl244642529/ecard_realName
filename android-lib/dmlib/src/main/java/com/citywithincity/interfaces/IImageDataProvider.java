package com.citywithincity.interfaces;

import android.widget.ImageView;

public interface IImageDataProvider {
	void load(ImageView imageView,String url);
	void load(ImageView imageView,String url,int defaultLoadingResID);
}
