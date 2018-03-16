package com.citywithincity.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.citywithincity.auto.tools.FormError;
import com.citywithincity.interfaces.IWidget;

/**
 * 可以设置value,每一个元素的value
 * @author randy
 *
 */
public class RadioGroup extends android.widget.RadioGroup implements IWidget<Object> {

	public RadioGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void setOnWidgetValueChangeListener(
			com.citywithincity.interfaces.IWidget.OnWidgetValueChangeListener<Object> listener) {
		
	}
	
	public void setValue(int index,Object value){
		getChildAt(index).setTag(value);
	}

	@Override
	public Object getValue() throws FormError {
		int id =getCheckedRadioButtonId();
		if(id>0){
			RadioButton button = (RadioButton) findViewById(id);
			return button.getTag();
		}
		return null;
	}

	@Override
	public void setValue(Object value) {
		for (int i = 0, count = this.getChildCount(); i < count; ++i) {
			RadioButton view = (RadioButton) this.getChildAt(i);
			if(view.getTag().equals(value)){
				this.check(view.getId());
			}
		}
	}

	@Override
	public String[] getPropertyNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		
		for (int i = 0, count = this.getChildCount(); i < count; ++i) {
			RadioButton view = (RadioButton) this.getChildAt(i);
			view.setEnabled(enabled);
		}
	}
	
}
