package com.damai.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.damai.interfaces.ITabIndicator;
import com.damai.lib.R;

/**
 * 滚动指示器，用于广告等
 * @author renxueliang
 *
 */
public class ScrollIndicator  extends LinearLayout implements ITabIndicator  {

	@TargetApi(Build.VERSION_CODES.HONEYCOMB) public ScrollIndicator(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public ScrollIndicator(Context context) {
		super(context);
		init(context);
	}

	public ScrollIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	int count;
	int currentIndex=-1;
	int size;
	int padding;
	private void init(Context context){
		Resources resources = context.getResources();
		size = (int) resources.getDimension(R.dimen._indicator_size);
		padding = (int) resources.getDimension(R.dimen._indicator_padding);
		setOrientation(LinearLayout.HORIZONTAL);
	}

	@Override
	public void setCurrentIndex(int index, boolean animated) {
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
	
	@Override
	public void setTabCount(int count) {
		this.count = count;
		this.removeAllViews();
		
		if(count>1){
			setVisibility(View.VISIBLE);
			for (int i = 0; i < count; ++i) {
				View view = new View(getContext());
				view.setBackgroundResource(R.drawable.round_indicator);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(size,size);
				if(i==0){
					view.setSelected(true);
					currentIndex = 0;
				}else{
					lp.leftMargin = padding;
				}
				addView(view,lp);
			}
		}else{
			setVisibility(View.GONE);
		}
		
		
	}

}
