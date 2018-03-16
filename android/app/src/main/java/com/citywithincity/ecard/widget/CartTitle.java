package com.citywithincity.ecard.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.citywithincity.auto.NotificationMethod;
import com.citywithincity.auto.Observer;
import com.citywithincity.auto.tools.Notifier;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.selling.activities.SCartActivity;
import com.citywithincity.ecard.selling.models.CartModel;
import com.citywithincity.models.http.JsonTaskManager;

import java.util.List;

@Observer
public class CartTitle extends RelativeLayout {

	private static Integer gNumber;
	private TextView textNumber;
	
	public CartTitle(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		final OnClickListener listener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(JsonTaskManager.getInstance().isLogin()){
					getContext().startActivity(new Intent(getContext(),SCartActivity.class));
				}else{
					JsonTaskManager.getInstance().requestLogin((Activity)getContext());
				}
			}
		};
		setOnClickListener(listener);
	}
	
	
	@NotificationMethod(CartModel.LIST)
	public void onGetList(List result,boolean isLastPage){
		gNumber = result.size();
		textNumber.setText(String.valueOf(gNumber));
	}
	
	
	@Override
	protected void onDetachedFromWindow() {
		Notifier.unregister(this);
		super.onDetachedFromWindow();
	}
	
	@Override
	protected void onFinishInflate() {
		if(isInEditMode())return;
		Notifier.register(this);
		textNumber = (TextView)findViewById(R.id.num);
		if(gNumber!=null){
			textNumber.setText(String.valueOf(gNumber));
		}else{
			textNumber.setText("");
		}
	}
	
}
