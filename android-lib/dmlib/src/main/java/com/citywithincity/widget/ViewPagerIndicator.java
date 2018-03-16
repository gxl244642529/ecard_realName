package com.citywithincity.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.damai.lib.R;

public class ViewPagerIndicator extends LinearLayout {

	int res;
	int padding;
	int currentIndex=-1;
	int count;
	public ViewPagerIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		setGravity(Gravity.CENTER);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable._indicator);
		res = a.getResourceId(R.styleable._indicator_indicator_view, R.layout._indicator_view);
		padding  = (int) a.getDimension(R.styleable._indicator_indicator_padding, R.dimen._indicator_padding);
		
		a.recycle();
	}
	
	public void setIndicatorCount(int count){
		this.count = count;
		LayoutInflater layoutInflater = LayoutInflater.from(getContext());
		this.removeAllViews();
		for (int i = 0; i < count; ++i) {
			View view = layoutInflater.inflate(res, null);
			LayoutParams lp =new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			if(i>0){
				lp.leftMargin = padding;
			}else{
				view.setSelected(true);
				currentIndex = 0;
			}
			addView(view,lp);
		}
	}
	
	public void setCurrent(int index){
		if(index!=currentIndex){
			if(currentIndex>=0){
				getChildAt(currentIndex).setSelected(false);
			}
			getChildAt(index).setSelected(true);
			currentIndex = index;
		}
		
	}
	
	public int getCount() {
		return count;
	}
	
	

}
