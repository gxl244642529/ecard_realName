package com.citywithincity.ecard.pay;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.citywithincity.ecard.R;
import com.damai.pay.DMPay;

public class ECardCashierItem extends RelativeLayout implements Checkable {

	private TextView textView;
	private RadioButton imageView;
	
	public ECardCashierItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setData(DMPay pay){
		textView.setText(pay.getTitle());
		Drawable drawable = getResources().getDrawable(pay.getIcon());
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),drawable.getMinimumHeight());
		textView.setCompoundDrawables(drawable, null, null, null);
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		textView = (TextView) findViewById(R.id._text_view);
		imageView = (RadioButton) findViewById(R.id._image_view);
	}

	@Override
	public void setChecked(boolean checked) {
		imageView.setChecked(checked);
	}

	@Override
	public boolean isChecked() {
		return imageView.isChecked();
	}

	@Override
	public void toggle() {
		imageView.toggle();
	}

}
