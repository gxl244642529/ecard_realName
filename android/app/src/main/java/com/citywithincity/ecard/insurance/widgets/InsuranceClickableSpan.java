package com.citywithincity.ecard.insurance.widgets;

import android.app.Activity;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class InsuranceClickableSpan extends ClickableSpan{

	private IOnTextClickListener listener;
	private Activity context;
	private int colorId;
	
	private Object tag;

	public InsuranceClickableSpan(Activity context,int colorID) {
		super();
		this.context = context;
		colorId = colorID;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void updateDrawState(TextPaint ds) {
		ds.setColor(context.getResources().getColor(colorId));
	}
	
	@Override
	public void onClick(View widget) {
		listener.onTextClick(tag);
	}
	
	public void setListener(IOnTextClickListener listener){
		this.listener = listener;
	}
	
	public interface IOnTextClickListener {
		void onTextClick(Object tag);
	}

	public void setTag(Object object) {
		tag = object;
	}
	
	public Object getTag() {
		return tag;
	}
	
}
