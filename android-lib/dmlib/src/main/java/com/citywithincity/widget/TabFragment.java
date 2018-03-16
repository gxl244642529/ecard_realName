package com.citywithincity.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.citywithincity.auto.tools.Notifier;
import com.citywithincity.interfaces.IViewContainer;
import com.citywithincity.utils.ViewUtil;
import com.damai.core.ILife;
import com.damai.helper.ActivityResult;
import com.damai.lib.R;


/**
 * 管理tabhost\tabpane里面的视图，使用本类
 * @author Randy
 *
 */
public abstract class TabFragment implements IViewContainer{
	
	private Context context;
	private View view;
	public abstract View onCreateView(LayoutInflater layoutInflater);
	
	protected View createView(Context context){
		this.view = onCreateView(LayoutInflater.from(context));
		onInitFragment(context);
		return view;
	}
	protected  abstract void onInitFragment(Context context);
	
	protected String getString(int resID){
		return getContext().getResources().getString(resID);
	}
	
	protected void setTitle(int resID){
		((TextView)findViewById(R.id._title_text)).setText(resID);
	}
	
	protected void setTitle(CharSequence title){
		((TextView)findViewById(R.id._title_text)).setText(title);
	}
	
	void onDeatch(){
		context = null;
		view =  null;
		Notifier.unregister(this);
	}
	
	void onAttach(Context context){
		this.context  =context;
		Notifier.register(this);
		ViewUtil.registerEvent(this);
	}
	
	public void onDestroy() {
		
	}
	
	public void onPause() {
		
	}

	public void onResume() {
		
	}
	
	public Context getContext(){
		return context;
	}

	@Override
	public View findViewById(int id) {
		return view.findViewById(id);
	}

	@Override
	public Activity getActivity() {
		return (Activity)getContext();
	}

	public void onStop() {
		// TODO Auto-generated method stub
		
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
	public void finish() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String idToString(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	

	
	
	
}
