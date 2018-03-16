package com.citywithincity.widget;

import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.citywithincity.auto.tools.FormError;
import com.citywithincity.ecard.R;
import com.citywithincity.interfaces.IWidget;
import com.citywithincity.utils.Alert;


public class AddAndSubEditText extends LinearLayout implements IWidget<Integer> {
	
	private EditText mEditText;
	private View add;
	private View sub;
	private int num = 1;
	private int maxNumber;
	private OnWidgetValueChangeListener<Integer> listener;
	
	private String minToast;
	private String maxToast;

	
	
	public AddAndSubEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	protected AddAndSubEditText(Context context) {
		super(context);
	}
	
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (isInEditMode()) {
			return;
		}
		mEditText = (EditText) findViewById(R.id.num);
		mEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
		mEditText.setText("1");
		add = findViewById(R.id.add);
		sub = findViewById(R.id.sub);
		add.setClickable(true);
		sub.setClickable(true);
		add.setOnClickListener(addListener);
		sub.setOnClickListener(subListener);
	}
	
	private OnClickListener addListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			int count = num+1;
			if(count >= maxNumber){
				if(maxNumber>0){
					count = maxNumber;
					if (!TextUtils.isEmpty(maxToast)) {
						Alert.showShortToast(maxToast);
					}
				}else{
					count = num;
				}
			}
			num = count;
//			checkEnabled();
			mEditText.setText(String.valueOf(num));
			if (listener != null)
			{
				listener.onWidgetValueChange(AddAndSubEditText.this);
			}
		}
	};
	
	private OnClickListener subListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			int count = num-1;
			if(count<=1){
				count = 1;
				if (!TextUtils.isEmpty(minToast)) {
					Alert.showShortToast(minToast);
				}
			}
			num = count;
//			checkEnabled();
			mEditText.setText(String.valueOf(num));
			if (listener != null)
			{
				listener.onWidgetValueChange(AddAndSubEditText.this);
			}
		}
	};
	
	
	
	public void setMaxCount(int count){
		this.maxNumber = count;
		checkEnabled();
	}
	
	private void checkEnabled(){
		sub.setEnabled(num>0);
		add.setEnabled(num<maxNumber);
	}
	
	public void setCount(int count){
		setValue(count);
	}
	
	@Override
	public Integer getValue() throws FormError {
		return num;
	}

	@Override
	public void setValue(Integer value) {
		num = value;
		mEditText.setText(String.valueOf(num));
		checkEnabled();
	}

	@Override
	public String[] getPropertyNames() {
		return new String[]{"count"};
	}

	@Override
	public void setOnWidgetValueChangeListener(
		com.citywithincity.interfaces.IWidget.OnWidgetValueChangeListener<Integer> listener) {
		this.listener = listener;
	}
	
	public void setMinToast(String minTips){
		this.minToast = minTips;
	}
	
	public void setMaxToast(String maxTips){
		this.maxToast = maxTips;
	}
	
	public void setToast(String minTips, String maxTips){
		this.minToast = minTips;
		this.maxToast = maxTips;
	}

}
