package com.citywithincity.interfaces;

import android.graphics.Bitmap;

public interface ImageRequestLisener extends IRequestError{
	void onImageSuccess(String url,Bitmap bitmap);
	void onImageProgress(String url,int total,int progress);
	void onImageError(String url,boolean networkError);
}