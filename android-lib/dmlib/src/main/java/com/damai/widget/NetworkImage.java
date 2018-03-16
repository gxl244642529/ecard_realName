package com.damai.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.damai.core.DMLib;
import com.damai.core.HttpJob;
import com.damai.core.JobListener;
import com.damai.helper.IValue;
import com.damai.lib.R;
public class NetworkImage extends RelativeLayout implements IValue, JobListener<HttpJob> {
	


	private String currentUrl;
	private ImageView imageView;
	private RoundProgressBar progressBar;
	
	
	
	public NetworkImage(Context context){
		super(context);
		progressBar = createProgressBar(context);
		imageView = createImageView(context);
		progressBar.setVisibility(View.GONE);
	}
	
	public NetworkImage(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public NetworkImage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	
	private RoundProgressBar createProgressBar(Context context){
		RoundProgressBar progressBar = new RoundProgressBar(context);
		int size = (int) context.getResources().getDimension(R.dimen._progress_bar_size);
		LayoutParams layoutParams = new LayoutParams(size,size);
		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		addView(progressBar,layoutParams);
		progressBar.setMax(100);
		progressBar.setProgress(0);
		return progressBar;
	}
	
	
	private ImageView createImageView(Context context){
		ImageView imageView = new ImageView(context);
		imageView.setScaleType(ScaleType.FIT_XY);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		addView(imageView,layoutParams);
		return imageView;
	}
	
	
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		Context context = getContext();
		progressBar = (RoundProgressBar) findViewById(R.id._progress_bar);
		if(progressBar==null){
			progressBar = createProgressBar(context);
		}
		
		imageView = (ImageView) findViewById(R.id._image_view); 
		if(imageView==null){
			imageView = createImageView(context);
		}
		progressBar.setVisibility(View.GONE);
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		
	}
	
	public void load(String url){
		if(url.equals(currentUrl)){
			return;
		}
		DMLib.getJobManager().loadImage(url,this);
		currentUrl = url;
		progressBar.setVisibility(View.VISIBLE);
	}


	/**
	 * 这里一定是string
	 */
	@Override
	public void setValue(Object data) {
		load((String)data);
	}


	@Override
	public void onJobSuccess(HttpJob job) {
		Bitmap bitmap = (Bitmap) job.getData();
		imageView.setImageBitmap(bitmap);
		progressBar.setVisibility(View.GONE);
	}

	public ImageView getImageView(){
		return imageView;
	}

	@Override
	public void onJobProgress(HttpJob job) {
		progressBar.setProgress((int)((float)job.getCurrentBytes() * 100 / job.getTotalBytes()));
	}


	@Override
	public boolean onJobError(HttpJob job) {
		progressBar.setVisibility(View.GONE);
		return false;
	}

}
