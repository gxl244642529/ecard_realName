package com.citywithincity.plugin;



import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.citywithincity.interfaces.IViewContainer;

public abstract class AbsPlugin implements IPlugin,IViewContainer {

	protected Activity mContext;
	protected Resources mResources;
	private IPluginContainer mObserver;
	private String mTitle;
	private View mView;
	private AssetManager mAssetManager;
	
	
	public void onAttach(Activity activity, Resources res,AssetManager am,IPluginContainer observer) {
		mContext =activity;
		this.mResources = res;
		this.mObserver = observer;
		mAssetManager = am;
	}
	
	protected abstract void onSetContent(Bundle savedInstanceState);
	
	
	
	@Override
	public View findViewById(int id){
		return mView.findViewById(id);
	}
	
	
	@Override
	public Activity getActivity(){
		return mContext;
	}
	

	public View createView(Bundle savedInstanceState){
		onSetContent(savedInstanceState);
		return mView;
	}
	
	public LayoutInflater getLayoutInflater(){
		return mContext.getLayoutInflater();
	}
	
	public void setContentView(View view){
		mView = view;
		mObserver.onSetContentView(mView);
	}
	
	public void setContentView(int resID){
		mView = inflate(resID);
		mObserver.onSetContentView(mView);
	}
	
	public String getTitle(){
		return mTitle;
	}
	
	public void setTitle(String title){
		mTitle = title;
	}
	
	public void setTitle(int titleRes){
		mTitle = getResources().getString(titleRes);
	}
	
	@Override
	public void startActivity(Class<? extends IPlugin> clazz){
		mObserver.onEventStartActivity(clazz);
	}
	
	public Resources getResources(){
		return mResources;
	}
	
	public AssetManager getAssets(){
		return mAssetManager;
	}

	@Override
	public View inflate(int layoutId) {
		return mObserver.inflate(layoutId);
	}

	@Override
	public void onDestroy() {
		mObserver = null;
		mContext = null;
		mResources = null;
	}

	@Override
	public void onResume() {
		
	}

	@Override
	public void onStop() {
		
	}
	@Override
	public void onRestart(){
		
	}
	/**
	 * 
	 * @param outState
	 */
	@Override
	public void onSaveInstanceState(Bundle outState){
		
	}
	
	@Override
	public void onNewIntent(Intent intent){
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		
	}

}
