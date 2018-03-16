package com.citywithincity.models;

import android.util.Log;

import com.citywithincity.interfaces.ILogImpl;

/**
 * android 默认log
 * @author Randy
 *
 */
public class AndroidLog implements ILogImpl {
	private String tag;
	
	public AndroidLog(String tag){
		this.tag = tag;
	}
	
	@Override
	public void info(String message, Throwable error) {
		if(error==null)
		{
			Log.i(tag, message);
		}else{
			Log.i(tag, message==null?"":message, error);
		}
		
	}

	@Override
	public void error(String message, Throwable error) {
		if(error==null)
		{
			Log.e(tag, message);
		}else{
			Log.e(tag, message==null?"":message, error);
		}
	}

}
