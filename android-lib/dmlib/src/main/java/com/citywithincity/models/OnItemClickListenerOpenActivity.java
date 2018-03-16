package com.citywithincity.models;

import java.io.Serializable;

import android.app.Activity;
import android.content.Intent;

import com.citywithincity.interfaces.IOnItemClickListener;

public class OnItemClickListenerOpenActivity<T extends Serializable> implements IOnItemClickListener<T> {

	private int requestCode;
	private Class<? extends Activity> clsRef;
	
	public OnItemClickListenerOpenActivity(Class<? extends Activity> clsRef){
		this( clsRef,0);
	}
	
	public OnItemClickListenerOpenActivity(Class<? extends Activity> clsRef,int requestCode){
		this.requestCode = requestCode;
		this.clsRef = clsRef;
	}

	@Override
	public void onItemClick(Activity activity, T data,int position) {
		Intent intent = new Intent(activity,clsRef);
		intent.putExtra("data", data);
		if(requestCode>0){
			activity.startActivityForResult(intent, requestCode);
		}else{
			activity.startActivity(intent);
		}
	}
	
	

}
