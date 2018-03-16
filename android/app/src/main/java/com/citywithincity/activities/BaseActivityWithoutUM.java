package com.citywithincity.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.citywithincity.auto.tools.Notifier;
import com.citywithincity.ecard.R;
import com.citywithincity.interfaces.IViewContainer;
import com.citywithincity.models.http.JsonTaskManager;
import com.citywithincity.utils.ViewUtil;


public abstract class BaseActivityWithoutUM extends Activity implements IViewContainer {

	private String title;
	/**
	 * 存储内容:
	 * object[0] = method
	 * object[1] = name
	 */
	//private Map<Integer, Object[]> idToMethodMap = new LinkedHashMap<Integer,  Object[]>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		JsonTaskManager.getInstance().onCreate(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		onSetContent(savedInstanceState);
		View view = findViewById(R.id._title_left);
		if(view!=null){
			view.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					backToPrevious();
				}
			});
		}
		
		
		Notifier.register(this);
		ViewUtil.registerEvent(this);
	}
	
	protected void backToPrevious(){
		finish();
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

	@Override
	public Activity getActivity() {
		return this;
	}

}
