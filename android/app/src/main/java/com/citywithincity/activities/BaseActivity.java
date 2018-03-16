package com.citywithincity.activities;

import android.os.Bundle;
import android.widget.TextView;

import com.citywithincity.auto.tools.Notifier;
import com.citywithincity.ecard.R;
import com.citywithincity.interfaces.IViewContainer;
import com.citywithincity.models.http.JsonTaskManager;
import com.citywithincity.utils.ViewUtil;
import com.damai.auto.DMActivity;


public abstract class BaseActivity extends DMActivity implements IViewContainer {

	private String title;
	/**
	 * 存储内容:
	 * object[0] = method
	 * object[1] = name
	 */
	//private Map<Integer, Object[]> idToMethodMap = new LinkedHashMap<Integer,  Object[]>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		JsonTaskManager.getInstance().onCreate(this);
		super.onCreate(savedInstanceState);
		
		Notifier.register(this);
		ViewUtil.registerEvent(this);
	}
	
	
	protected boolean isLogin(){
		return JsonTaskManager.getInstance().isLogin();
	}
	
	protected abstract void onSetContent(Bundle savedInstanceState);
	
	protected void setTitle(String title){
		this.title = title;
		TextView textView = (TextView) findViewById(R.id._title_text);
		if(textView!=null){
			textView.setText(title);
		}
	}
	@Override
	public void setTitle(int titleId) {
		String title = getString(titleId);
		setTitle(title);
	}
	

	
	@Override
	protected void onDestroy() {
		JsonTaskManager.getInstance().onDestroy(this);
		Notifier.unregister(this);
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		JsonTaskManager.getInstance().onPause(this);
		super.onPause();

	}
	

	@Override
	protected void onResume() {
		super.onResume();
		JsonTaskManager.getInstance().onResume(this);
	}
	
	
	protected String getPageName(){
		return title;
	}


}
