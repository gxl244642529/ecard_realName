package com.damai.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.citywithincity.interfaces.IViewContainer;
import com.damai.helper.IPersistent;
import com.damai.helper.IValue;
import com.damai.lib.R;
public class FormRadioGroup extends RadioGroup implements FormElement,IValue,IPersistent {

	private Object[] values;
	private Object value;
	private String hint;
	private List<RadioButton> list;
	public FormRadioGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(isInEditMode()){
			return;
		}
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable._radio);
		String optionsString = a.getString(R.styleable._radio_values);
		hint = a.getString(R.styleable._radio_radio_hint);
		if(hint==null){
			throw new RuntimeException("You must define \"radio_hint\" in RadioGroup.");
		}
		//格式为  int|V,V,V string|V,V,V
		try{
			String[] arr = optionsString.split("\\|");
			String type = arr[0];
			int t = 0;
			if(type.startsWith("int")){
				t = 1;
			}else if(type.startsWith("bool")){
				t=2;
			}
			String[] contents = arr[1].split(",");
			values = new Object[contents.length];
			int index = 0;
			for (String string : contents) {
				switch (t) {
				case 0:
					values[index++] = string;
					break;
				case 1:
					values[index++] =  Integer.parseInt(string);
					break;
				default:
					values[index++] =  Boolean.parseBoolean(string);
					break;
				}
			}
		}catch(Throwable e){
			throw new RuntimeException("RuntimeException when get radion values ,the format is int|V,V,V or string|V,V,V",e);
		}
		
		
		a.recycle();
	}

	
	
	@Override
	protected void onFinishInflate() {
		if(isInEditMode()){
			return;
		}
		list = new ArrayList<RadioButton>(getChildCount());
		for(int i=0 , c = getChildCount(); i < c; ++i){
			View view = getChildAt(i);
			if(view instanceof RadioButton){
				list.add((RadioButton)view);
			}
		}
	}


	@Override
	public String validate(Map<String, Object> data) {
		Object value = getValue();
		if (value == null) {
			return hint;
		}
		data.put( ((IViewContainer)getContext()).idToString(getId())  , value);
		return null;
	}
	
	
	

	@Override
	public void setValue(Object value) {
		int index = 0;
		for (Object v : values) {
			if(v.equals(value)){
				RadioButton button = list.get(index);
				button.setChecked(true);
				break;
			}
			++index;
		}
	}

	@Override
	public Object getValue() {
		value = null;
		int index = 0;
		for (RadioButton button : list) {
			if(button.isChecked()){
				value = values[index];
				break;
			}
			index ++;
		}
		return value;
	}

}
