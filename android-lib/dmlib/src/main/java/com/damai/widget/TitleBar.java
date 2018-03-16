package com.damai.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class TitleBar extends RelativeLayout {

	private ImageButton back;
	
	public TitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		back = new ImageButton(context);
		//R.style._back;
	}

}
