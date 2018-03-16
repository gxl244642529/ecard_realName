package com.damai.helper;

import android.view.View;
import android.view.View.OnClickListener;

import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.interfaces.IDestroyable;
import com.citywithincity.utils.Alert;
import com.damai.auto.LifeManager;
import com.damai.lib.R;
import com.damai.note.MethodInfo;

public class ItemEventDataSetter implements IDestroyable, DataSetter, OnClickListener, DialogListener{

	private MethodInfo info;
	private Object data;
	private String confirm;
	
	public ItemEventDataSetter(MethodInfo info,View view,String confirm){
		this.info = info;
		view.setOnClickListener(this);
		this.confirm = confirm;
	}
	
	/**
	 * 及时销毁
	 */
	@Override
	public void destroy() {
		info = null;
		data = null;
	}
	
	@Override
	public void setValue(Object data) {
		this.data=data;
	}
	@Override
	public void onClick(View arg0) {
		if(confirm!=null){
			Alert.confirm(LifeManager.getActivity(), confirm, this);
			return;
		}
		info.invoke(data);
	}

	@Override
	public void onDialogButton(int id) {
		if(id==R.id._id_ok){
			info.invoke(data);
		}
		
	}


	
}
