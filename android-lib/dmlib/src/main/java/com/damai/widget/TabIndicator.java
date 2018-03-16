package com.damai.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.citywithincity.utils.MessageUtil;
import com.citywithincity.utils.MessageUtil.IMessageListener;
import com.damai.interfaces.ITabIndicator;
import com.damai.lib.R;
/**
 * tab指示器
 * @author Randy
 *
 */
public class TabIndicator extends LinearLayout implements ITabIndicator, IMessageListener {
	
	private int backgroundColor;
	private int tabCount;
	private int lastIndex;
	/**
	 * 样式有，
	 * 
	 * n个原点(view,中心或者右边对其），定义背景
	 * 下划线（动画滑动,整个视图），高亮，一个在屏幕滑动
	 * 
	 * @param context
	 * @param attrs
	 */
	
	public TabIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable._indicator);
		backgroundColor = a.getColor(R.styleable._indicator_indicator_color, Color.parseColor("#ff0000"));
		a.recycle();
		a = context.obtainStyledAttributes(attrs, R.styleable._group_view);
		tabCount = a.getInt(R.styleable._group_view_group_count, 0);
		if(tabCount==0){
			throw new RuntimeException("请在layout中定义group_count,确定视图数量");
		}
		a.recycle();
		View view = new View(getContext());
		LayoutParams lParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		addView(view,lParams);
		view.setBackgroundColor(backgroundColor);
	}
	
	OnGlobalLayoutListener layoutListener = new OnGlobalLayoutListener() {
		
		@SuppressWarnings("deprecation")
		@Override
		public void onGlobalLayout() {
			MessageUtil.sendMessage(TabIndicator.this);
			
			getViewTreeObserver().removeGlobalOnLayoutListener(layoutListener);
		}
	};
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);
	}

	@Override
	public void setCurrentIndex(int index, boolean animated) {
		int width = getMeasuredWidth();
		View view = getChildAt(0);
		AnimationSet set = new AnimationSet(true);
		set.setDuration(150);
		set.setFillAfter(true);
		TranslateAnimation animation = new TranslateAnimation( 
				Animation.ABSOLUTE, width*lastIndex/tabCount, 
				Animation.ABSOLUTE, width*index/tabCount, 
				Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
		set.addAnimation(animation);
		view.startAnimation(set);
		lastIndex = index;
	}

	

	@Override
	public void setTabCount(int count) {
		tabCount = count;
		onMessage(0);
	}

	@Override
	public void onMessage(int message) {
		int width = getMeasuredWidth();
		View view = getChildAt(0);
		LayoutParams lParams = (LayoutParams) view.getLayoutParams();
		lParams.width = width / tabCount;
		view.setLayoutParams(lParams);
	}
	
	

}
