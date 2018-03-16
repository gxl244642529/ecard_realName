package com.citywithincity.widget;



import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.citywithincity.widget.data.MenuData;
import com.damai.lib.R;

public class TreeMainItem extends RelativeLayout {

	private TextView textView;
	private View arrow;
	private ImageView imageView;
	public TreeMainItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override
	protected void onFinishInflate() {
		textView = (TextView)findViewById(R.id._text_view);
		imageView = (ImageView)findViewById(R.id._image_view);
		arrow = findViewById(R.id._id_arrow);
	}
	
	
	public void setData(MenuData data) {
		textView.setText(data.label);
		imageView.setImageResource(data.imageRes);
		boolean selected =data.selected;
		textView.setSelected(selected);
		imageView.setSelected(selected);
		if(selected)
		{
			setBackgroundResource(R.color._tree_main_selected);
		}else{
			setBackgroundResource(R.drawable._white_item_selector);
		}
		
		arrow.setVisibility(data.children==null ? View.GONE : View.VISIBLE);
		
	}
	

	
	
	

}
