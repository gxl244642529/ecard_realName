package com.citywithincity.widget;



import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.citywithincity.widget.data.MenuData;
import com.damai.lib.R;


public class TreeSubItem extends RelativeLayout {

	private TextView txtView;
	
	public TreeSubItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		txtView = (TextView)findViewById(R.id._text_view);
	}
	
	public void setData(MenuData data){
		txtView.setText(data.label);
		txtView.setSelected(data.selected);
	}
	
	

}
