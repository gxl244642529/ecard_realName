package com.citywithincity.ecard.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.citywithincity.ecard.R;
import com.citywithincity.utils.MemoryUtil;
import com.damai.util.ViewUtil;

public class MainPane extends RelativeLayout  {

	public View mCenter;
	public View mPane;
	
	public ImageView mCache;

	
	private View[] views;
	
	private OnClickListener listener;
	
	public MainPane(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override
	protected void onDetachedFromWindow() {
		listener = null;
		MemoryUtil.clearArray(views);
		views = null;
		super.onDetachedFromWindow();
	}
	
	
	public void layoutChild() {
		int size = (int) ((float)ViewUtil.screenWidth * 87 / 720);
		for(int i=0; i < 4; ++i){
			setViewSize(views[i], size);
		}
	}
	
	
	private void setViewSize(View back,int size){
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)back.getLayoutParams();
		lp.width = size;
		lp.height = size;
		back.setLayoutParams(lp);
	}
	
	@Override
	public void setOnClickListener(OnClickListener listener){
		this.listener = listener;
	}
	
	
	
	@SuppressLint("CutPasteId")
	@Override
	protected void onFinishInflate() {
		mCenter = findViewById(R.id.center);
		mPane = findViewById(R.id.pane_bg);
		
		mCache = new ImageView(getContext());
		addView(mCache,0,new RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
		
		
		
		views = new View[5];
		views[0] = findViewById(R.id.back);
		views[1] = findViewById(R.id.main_pane_top);
		views[2] = findViewById(R.id.main_pane_left);
		views[3] = findViewById(R.id.main_pane_right);
		views[4] = findViewById(R.id.center);
		
		
		inViewIndex = -1;
		setOnTouchListener(panListener);
	}
	private int inViewIndex;
	protected final OnTouchListener panListener= new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int action = event.getAction() & MotionEvent.ACTION_MASK;
			
			switch (action) {
			case MotionEvent.ACTION_DOWN:
			{
				int x = (int) event.getX();
				int y = (int) event.getY();
				int index = 0;
				for (View view : views) {
					if(x > view.getLeft() && x<view.getRight() && y>view.getTop() && y < view.getBottom()){
						inViewIndex = index;
						break;
					}
					index ++;
				}
			}
				break;
			case MotionEvent.ACTION_UP:
			{
				int x = (int) event.getX();
				int y = (int) event.getY();
				int index = 0;
				for (View view : views) {
					if(x > view.getLeft() && x<view.getRight() && y>view.getTop() && y < view.getBottom()){
						if(index==inViewIndex){
							listener.onClick(views[index]);
						}
						break;
					}
					index ++;
				}
				inViewIndex = -1;
			}
				break;
			case MotionEvent.ACTION_CANCEL:
			{
				inViewIndex = -1;
			}
				break;
			default:
				break;
			}
			return true;
		}
	};
	


}
