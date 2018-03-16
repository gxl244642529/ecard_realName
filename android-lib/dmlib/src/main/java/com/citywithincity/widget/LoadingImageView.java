package com.citywithincity.widget;



import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.citywithincity.interfaces.ImageRequestLisener;
import com.citywithincity.utils.Http;
import com.damai.lib.R;
public class LoadingImageView extends RelativeLayout implements  ImageRequestLisener{

	private View progressBar;
	private ImageView imageView;
	private boolean loading;
	public LoadingImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onFinishInflate() {
		progressBar = findViewById(R.id._progress_bar);
		imageView = (ImageView) findViewById(R.id._image_view);
	}
	
	/**
	 * 
	 * @param url
	 */
	public void load(String url)
	{
		
		progressBar.setVisibility(View.VISIBLE);
		Http.loadImage(url, this);
	}
	




	@Override
	public void onRequestError(String errMsg, boolean isNetworkError) {
		loading = false;
	}

	@Override
	public void onImageSuccess(String url, Bitmap bitmap) {
		// TODO Auto-generated method stub
		imageView.setImageBitmap(bitmap);
		progressBar.setVisibility(View.GONE);
		loading = false;
	}

	@Override
	public void onImageProgress(String url, int total, int progress) {
		
	}

	@Override
	public void onImageError(String url, boolean networkError) {
		// TODO Auto-generated method stub
		
	}
	
	public void setImage(int resid) {
		imageView.setBackgroundResource(resid);
		progressBar.setVisibility(View.GONE);
		loading = false;
	}

	
}
