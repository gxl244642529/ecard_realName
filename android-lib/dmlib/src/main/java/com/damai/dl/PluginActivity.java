package com.damai.dl;

import android.content.Context;
import android.view.View;

import com.damai.auto.DMFragmentActivity;

public abstract class PluginActivity extends DMFragmentActivity {

	public View inflate(int resId){
		return getLayoutInflater().inflate(resId, null);
	}
	
	protected Context getContext(){
		return this;
	}

}
