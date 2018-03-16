package com.citywithincity.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.damai.lib.R;

public class TitleBar extends RelativeLayout {
	private TextView titleTextView;
	private LinearLayout rightContainer;
	
	private String title;
	private int rightRes;
	private int leftRes;
	
	public TitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable._title_bar);
		
		rightRes = a.getResourceId(R.styleable._title_bar_right_view, 0);
		title = a.getString(R.styleable._title_bar_title_text);
		leftRes = a.getResourceId(R.styleable._title_bar_left_view, 0);
		
		a.recycle();
	}
	
	
	@Override
	protected void onFinishInflate() {
		titleTextView = (TextView) findViewById(R.id._title_text);
		rightContainer = (LinearLayout) findViewById(R.id._title_right);
		LayoutInflater layoutInflater = LayoutInflater.from(getContext());
		if(rightRes>0){
			View view = layoutInflater.inflate(rightRes, null);
			rightContainer.addView(view);
		}
		if(title!=null){
			titleTextView.setText(title);
		}
	}

	public void setTitle(String title){
		titleTextView.setText(title);
	}
	
	public void setRightView(View view){
		if(view==null){
			rightContainer.removeAllViews();
		}else{
			rightContainer.addView(view);
		}
	}
	
	public View getRightView(){
		if(rightContainer.getChildCount()>0)
			return rightContainer.getChildAt(0);
		return null;
	}
	
}
