package com.citywithincity.ecard.discard.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.discard.fragments.SelectSchoolFragment;
import com.citywithincity.ecard.discard.fragments.SelectSchoolFragment.SelectSchoolListener;
import com.damai.helper.IValue;
import com.damai.util.StrKit;
import com.damai.widget.FormElement;

import java.util.Map;
public class SelectSchoolView extends LinearLayout implements FormElement, SelectSchoolListener ,IValue{
	private TextView text;
	
	public SelectSchoolView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onFinishInflate() {
		text = (TextView) findViewById(R.id._select_hint);
		if(text==null){
			for(int i=0 , c = getChildCount(); i < c; ++i){
				View view = getChildAt(i);
				if(view instanceof TextView){
					text =(TextView) view;
					break;
				}
			}
		}
		if(text==null){
			throw new RuntimeException("FormSelectView must have a textview with id R.id._select_hint");
		}
		this.setOnClickListener(listener);
	}
	

	/**
	 * 
	 */
	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			//选择学校
			SelectSchoolFragment fragment = new SelectSchoolFragment();
			fragment.setListener(SelectSchoolView.this);
			fragment.replaceTo(getContext(),true,true);
		}
	};
	private String schoolName;

	@Override
	public void onSelectSchool(String schoolName, String schoolCode) {
		text.setText(schoolName);
	}


	@Override
	public String validate(Map<String, Object> data) {
		String value = text.getText().toString();
		if(StrKit.isBlank(value)){
			return "请选择学校";
		}
		data.put("schoolName", value);
		return null;
	}

	@Override
	public void setValue(Object value) {
		schoolName = (String) value;
		text.setText(schoolName);
	}

	
	public Object getValue() {
		return schoolName;
	}



}
