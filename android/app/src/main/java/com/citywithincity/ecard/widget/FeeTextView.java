package com.citywithincity.ecard.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.damai.helper.IValue;

public class FeeTextView extends TextView implements IValue{

	public FeeTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public FeeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public FeeTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setValue(Object data) {
		int fee = 0;
		if(data instanceof Integer){
			fee = (Integer)data;
		}else if(data instanceof String){
			fee = Integer.parseInt((String)data);
		}else if(data instanceof Double){
			fee = (int) ((Double)data * 100);
		}
		setText(String.format("%.02f", (float) fee / 100));
	}
	

}
