package com.damai.widget;

import java.util.Map;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.citywithincity.interfaces.IViewContainer;
import com.citywithincity.utils.MD5Util;
import com.damai.helper.IPersistent;
import com.damai.lib.R;
public class FormEditText extends EditText implements FormElement,IPersistent{

	private boolean required;
	
	public FormEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.api);
		required = a.getBoolean(R.styleable.api_required, true);
		a.recycle();
	}

	@Override
	public String validate(Map<String, Object> data) {
		String content = getEditableText().toString().trim();
		if(required){
			if(TextUtils.isEmpty(content)){
				return this.getHint().toString();
			}
		}
		if( isPassword(getInputType())){
			content = MD5Util.md5Appkey(content);
		}
		
		//name
		data.put(((IViewContainer)getContext()).idToString(getId()), content);
		return null;
	}
	/*
	@Override
	public String getName() {
		return name;
	}*/

	@Override
	public Object getValue() {
		return  getEditableText().toString().trim();
	}

	@Override
	public void setValue(Object value) {
		if(value == null)return;
		setText(String.valueOf(value));
		
	}

	private static boolean isPassword(int inputType){
		 final int variation =
	                inputType & (EditorInfo.TYPE_MASK_CLASS | EditorInfo.TYPE_MASK_VARIATION);
	        return variation
	                == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD)
	                || variation
	                == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_WEB_PASSWORD)
	                || variation
	                == (EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD);
	}
	

}
