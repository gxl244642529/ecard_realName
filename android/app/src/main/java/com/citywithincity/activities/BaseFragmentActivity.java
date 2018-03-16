package com.citywithincity.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.citywithincity.auto.tools.Notifier;
import com.citywithincity.ecard.R;
import com.citywithincity.interfaces.IViewContainer;
import com.citywithincity.models.http.JsonTaskManager;
import com.citywithincity.utils.ViewUtil;
import com.damai.core.ILife;
import com.damai.helper.ActivityResult;
import com.umeng.analytics.MobclickAgent;

public abstract class BaseFragmentActivity extends FragmentActivity implements IViewContainer {
	

	private String title;
	
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
	protected boolean isLogin(){
		return JsonTaskManager.getInstance().isLogin();
	}
	@Override
	public Activity getActivity(){
		return this;
	}
	
	protected void backToPrevious(){
		finish();
	}
	
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
	
	protected abstract void onSetContent(Bundle savedInstanceState);

	@Override
	protected void onDestroy() {
		Notifier.unregister(this);
		JsonTaskManager.getInstance().onDestroy(this);
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		JsonTaskManager.getInstance().onPause(this);
		super.onPause();
		MobclickAgent.onPageEnd(getPageName());
		MobclickAgent.onPause(this);
	}
	
	
	protected String getPageName(){
		return title;
	}

	@Override
	protected void onResume() {
		super.onResume();
		JsonTaskManager.getInstance().onResume(this);
		MobclickAgent.onPageStart(getPageName()); // 统计页面
		MobclickAgent.onResume(this);
	}
	@Override
	public void startActivityForResult(ActivityResult result, Intent intent,
			int requestCode) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void addLife(ILife life) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public View findViewByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int getViewId(String name) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public String idToString(int id) {
		// TODO Auto-generated method stub
		return null;
	}
}
