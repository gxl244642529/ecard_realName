package com.damai.dl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.citywithincity.interfaces.IViewContainer;
import com.citywithincity.utils.FragmentUtil;
import com.damai.auto.LifeManager;
import com.damai.core.ILife;
import com.damai.helper.ActivityResult;
import com.damai.note.ClassParser;

public class HostFragment extends Fragment implements IHostFragment,IViewContainer {

	private IPluginFragment pluginFragment;
	private IHostActivity host;
	
	public HostFragment(){
		
	}
	
	public void setFragment(IPluginFragment value){
		this.pluginFragment = value;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		pluginFragment.setHost((IHostActivity)activity);
		pluginFragment.setHostFragment(this);
		host  = (IHostActivity)activity;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LifeManager.onCreate(this);
		ClassParser.parse(pluginFragment);
		pluginFragment.onCreate(savedInstanceState);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		host.setOverrideResources(true);
		View view= pluginFragment.onCreateView(inflater, container, savedInstanceState);
		pluginFragment.setView(view);
		host.setOverrideResources(false);
		return  view;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		pluginFragment.onViewCreated(view, savedInstanceState);
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		LifeManager.onResume(this);
		pluginFragment.onResume();
	}
	
	@Override
	public void onStop() {
		super.onStop();
		pluginFragment.onStop();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		pluginFragment.onPause();
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		pluginFragment.onDestroy();
	}

	@Override
	public void finishFragment() {
		FragmentUtil.removeFragment(getActivity(), this, true);
	}

	@Override
	public void startActivityForResult(ActivityResult result, Intent intent,
			int requestCode) {
		((IViewContainer)getActivity()).startActivityForResult(result, intent, requestCode);
	}

	@Override
	public View findViewById(int id) {
		return getView().findViewById(id);
	}

	@Override
	public void addLife(ILife life) {
		pluginFragment.addLife(life);
	}

	@Override
	public View findViewByName(String name) {
		return findViewById(pluginFragment.getViewId(name));
	}

	@Override
	public int getViewId(String name) {
		return pluginFragment.getViewId(name);
	}

	@Override
	public void finish() {
		finishFragment();
	}

	@Override
	public String idToString(int id) {
		return pluginFragment.idToString(id);
	}

	@Override
	public IPluginBase getPlugin() {
		return pluginFragment;
	}
	
}
