package com.damai.widget;

import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;

import com.citywithincity.interfaces.IViewContainer;
import com.damai.helper.IValue;

public class FormCheckBox extends CheckBox implements FormElement,IValue {

	public FormCheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void setValue(Object data) {
		setChecked((Boolean)data);
	}

	@Override
	public String validate(Map<String, Object> data) {
		data.put(((IViewContainer)getContext()).idToString(getId()), isChecked());
		return null;
	}

}
