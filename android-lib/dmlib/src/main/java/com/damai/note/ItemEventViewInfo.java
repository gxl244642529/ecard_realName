package com.damai.note;

import java.lang.reflect.Method;

import android.view.View;

import com.damai.helper.DataSetter;
import com.damai.helper.IViewInfo;
import com.damai.helper.ItemEventDataSetter;

public class ItemEventViewInfo extends MethodInfo implements IViewInfo {


	/**
	 *  item 控件id
	 */
	private int id;
	private String confirm;
	
	public ItemEventViewInfo(Method method,int id,String confirm) {
		super(method);
		this.id = id;
		this.confirm = confirm.equals("") ? null : confirm;
	}
	
	public int getId(){
		return id;
	}
	
	@Override
	public DataSetter createSetter(View view) {
		return new ItemEventDataSetter(this,view.findViewById(id),confirm);
	}



	@Override
	public void clearObserver() {
		observer = null;
	}


}
