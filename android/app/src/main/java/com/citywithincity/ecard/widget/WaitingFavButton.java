package com.citywithincity.ecard.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.citywithincity.ecard.R;

public class WaitingFavButton  extends FrameLayout{

	private Drawable _onIcon;
	private Drawable _offIcon;
	
	private ProgressBar _progressBar;
	private ImageView _imageView;
	
	
	
	@SuppressLint("Recycle")
	public WaitingFavButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.fav);
		_onIcon= a.getDrawable(R.styleable.fav_on_icon);
		_offIcon= a.getDrawable(R.styleable.fav_off_icon);
	}
	
	
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		Context context = getContext();
		_progressBar = new ProgressBar(context);
		_imageView = new ImageView(context);
		_progressBar.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, 
				android.view.ViewGroup.LayoutParams.MATCH_PARENT));
		addView(_progressBar);
		_imageView.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, 
				android.view.ViewGroup.LayoutParams.MATCH_PARENT));
		addView(_imageView);
		waiting();
	}
	

	public void waiting(){
		_imageView.setVisibility(View.GONE);
		_progressBar.setVisibility(View.VISIBLE);
		setEnabled(false);
	}


	public boolean getOn(){
		return _imageView.getVisibility()==View.VISIBLE;
	}

	
	
	public void setOn(boolean on){
		setEnabled(true);
		_imageView.setVisibility(View.VISIBLE);
		_progressBar.setVisibility(View.GONE);
		if(on){
			_imageView.setImageDrawable(_onIcon);
		}else{
			_imageView.setImageDrawable(_offIcon);
		}
	}
}
