package com.citywithincity.ecard.widget;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.citywithincity.ecard.R;
import com.damai.util.ViewUtil;

public class PaneContainerView extends FrameLayout implements
		OnGlobalLayoutListener, OnGestureListener {

	public View mPaneButton;
	public MainPane mMainPane;
	private GestureDetector detector;
	private OnClickListener listener;

	// private ArrayList<View> array=new ArrayList<View>();

	public PaneContainerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		getViewTreeObserver().addOnGlobalLayoutListener(this);
		mScroller = new Scroller(context);

		detector = new GestureDetector(context, this);
	}
	@Override
	protected void onDetachedFromWindow() {
		listener = null;
		super.onDetachedFromWindow();
	}
	
	@Override
	public void setOnClickListener(OnClickListener l) {
		this.listener = l;
	}

	private int x;
	private int y;
	public void movoTo(int x,int y){
		this.x = x;
		this.y = y;
	}

	private float offsetX;
	private float offsetY;
	private OnTouchListener onTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int action = event.getAction() & MotionEvent.ACTION_MASK;
			switch (action) {
			case MotionEvent.ACTION_DOWN: {
				FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) mPaneButton
						.getLayoutParams();
				offsetX = event.getRawX() - lParams.leftMargin;
				offsetY = event.getRawY() - lParams.topMargin;
			}
				break;
			case MotionEvent.ACTION_MOVE: {
				FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) mPaneButton
						.getLayoutParams();
				int minX = 0;
				int maxX = getWidth() - mPaneButton.getWidth();
				int minY = 0;
				int maxY = getHeight() - mPaneButton.getHeight();
				lParams.leftMargin = (int) (event.getRawX() - offsetX);
				lParams.leftMargin = Math.min(
						Math.max(minX, lParams.leftMargin), maxX);
				lParams.topMargin = (int) (event.getRawY() - offsetY);
				lParams.topMargin = Math.min(Math.max(minY, lParams.topMargin),
						maxY);
				mPaneButton.setLayoutParams(lParams);
			}
				break;
			}
			detector.onTouchEvent(event);
			return true;
		}
	};

	@Override
	protected void onFinishInflate() {
		if (isInEditMode()) {
			return;
		}
		mPaneButton = findViewById(R.id.pane_button);
		mPaneButton.setOnTouchListener(onTouchListener);
		mMainPane = (MainPane) findViewById(R.id.main_pane);
		
		mMainPane.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(v.getId()==R.id.back){
					startBigToSmall();
				}else if(v.getId()==R.id.center) {
					startBigToSmall();
				} else{
					listener.onClick(v);
				}
				
			}
		});
	}

	private int centerDestSize;
	private int centerStartSize;
	private int centerDSize;
	private int destSize;

	private int destX;
	private int destY;
	private Scroller mScroller;
	private boolean smallToBig;

	@SuppressWarnings("deprecation")
	@Override
	public void onGlobalLayout() {
		
		destSize = (int) ((float)ViewUtil.screenWidth * 550 / 720);
		LayoutParams lParams = (LayoutParams)mMainPane.getLayoutParams();
		lParams.leftMargin = x - destSize/2;
		lParams.topMargin = y - destSize/2;
		lParams.width = destSize;
		lParams.height = destSize;
		mMainPane.layoutChild();
		mMainPane.setLayoutParams(lParams);
		
		
		// TODO Auto-generated method stub
		FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mPaneButton
				.getLayoutParams();
		lp.leftMargin = 20;
		int height = mPaneButton.getHeight();
		lp.topMargin = getHeight() - height - 50;
		mPaneButton.setLayoutParams(lp);
		getViewTreeObserver().removeGlobalOnLayoutListener(this);
		mMainPane.getViewTreeObserver().addOnGlobalLayoutListener(drawGlobalLayoutListener);
		createCache(mPaneButton.getWidth(),
				(int) ((float) mPaneButton.getWidth() * 108 / 139),
				lp.leftMargin, lp.topMargin,lParams);

	}
	
	private OnGlobalLayoutListener drawGlobalLayoutListener = new OnGlobalLayoutListener() {
		
		@Override
		public void onGlobalLayout() {
			// TODO Auto-generated method stub
			// 截图
			if(mMainPane.getWidth() == destSize){
				if(mMainPane.mPane.getVisibility() == View.VISIBLE){
					mMainPane.mPane.setDrawingCacheEnabled(true);
					mMainPane.mPane.buildDrawingCache();
					Bitmap src = mMainPane.mPane.getDrawingCache();

					mMainPane.mCache.setImageBitmap(src);

					mMainPane.mPane.setVisibility(View.INVISIBLE);
					
				}
				
			}
			
		}
	};

	public void createCache(int outerSize, int size, int startX, int startY,FrameLayout.LayoutParams lp) {
		centerDestSize = mMainPane.mCenter.getWidth();

		destX = lp.leftMargin +lp.width / 2;
		destY = lp.topMargin + lp.height / 2;

		startSize = outerSize;
		centerStartSize = size;

		

		lp.width = outerSize;
		lp.height = outerSize;
		lp.leftMargin = startX;
		lp.topMargin = startY;


		RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) mMainPane.mCenter
				.getLayoutParams();
		rlp.width = size;
		rlp.height = size;
		mMainPane.mCenter.setLayoutParams(rlp);

		
	}

	@Override
	public void computeScroll() {
		if(isInEditMode())return;
		if (smallToBig) {
			if (mScroller.computeScrollOffset()) {

				// 产生了动画效果，根据当前值 每次滚动一点
				// scrollTo(mScroller.getCurrX(), mScroller.getCurrY());

				float rate = (float) (mScroller.getCurrX() - startX) / dx;
				// 大小为
				int currentSize = (int) (rate * dsize + startSize);
				int centerSize = (int) (rate * centerDSize + centerStartSize);
				setCurrent(currentSize, mScroller.getCurrX() - currentSize / 2,
						mScroller.getCurrY() - currentSize / 2, centerSize);

				postInvalidate();
			} else {
				onComplete();
			}
		} else {
			if (mScroller.computeScrollOffset()) {

				// 产生了动画效果，根据当前值 每次滚动一点
				// scrollTo(mScroller.getCurrX(), mScroller.getCurrY());

				float rate = (float) (mScroller.getCurrX() - destX) / dx;
				// 大小为
				int currentSize = (int) (rate * dsize + destSize);
				int centerSize = (int) (rate * centerDSize + centerDestSize);
				setCurrent(currentSize, mScroller.getCurrX() - currentSize / 2,
						mScroller.getCurrY() - currentSize / 2, centerSize);

				postInvalidate();
			} else {
				mPaneButton.setVisibility(View.VISIBLE);
				mMainPane.setVisibility(View.INVISIBLE);
			}
		}

	}

	public void setCurrent(int currentSize, int x, int y, int centerSize) {
		FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) mMainPane
				.getLayoutParams();
		lParams.leftMargin = x;
		lParams.topMargin = y;
		lParams.width = currentSize;
		lParams.height = currentSize;
		mMainPane.setLayoutParams(lParams);

		RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) mMainPane.mCenter
				.getLayoutParams();
		rlp.width = centerSize;
		rlp.height = centerSize;
		mMainPane.mCenter.setLayoutParams(rlp);

	}

	public void onComplete() {
		//mMainPane.mCache.setVisibility(View.INVISIBLE);
		//mMainPane.mPane.setVisibility(View.VISIBLE);
	}

	private int startX;
	private int startY;
	private int dx;
	private int dsize;
	private int startSize;

	public void startSmallToBig() {
		
		
		
		
		mPaneButton.setVisibility(View.INVISIBLE);
		mMainPane.setVisibility(View.VISIBLE);
		
		smallToBig = true;
		int left;
		int top;
		FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mPaneButton.getLayoutParams();
		left = lp.leftMargin;
		top = lp.topMargin;
		startX = left + mPaneButton.getWidth() / 2;
		startY = top + mPaneButton.getHeight() / 2;
		dx = destX - startX;
		dsize = destSize - startSize;
		centerDSize = centerDestSize - centerStartSize;
		
		lp = (FrameLayout.LayoutParams) mMainPane.getLayoutParams();
		lp.leftMargin = left;
		lp.topMargin = top;
		mMainPane.setLayoutParams(lp);
		
		
		mScroller.startScroll(startX, startY, dx, destY - startY, 500);
		invalidate();
	}

	public void startBigToSmall() {
		// 从dest到startx
		smallToBig = false;
		dx = startX - destX;
		dsize = startSize - destSize;
		centerDSize = centerStartSize - centerDestSize;
		mScroller.startScroll(destX, destY, dx, startY - destY, 500);
		//mMainPane.mCache.setVisibility(View.VISIBLE);
		//mMainPane.mPane.setVisibility(View.INVISIBLE);
		invalidate();
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		startSmallToBig();
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

}
