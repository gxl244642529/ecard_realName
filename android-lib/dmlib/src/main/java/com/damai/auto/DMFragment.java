package com.damai.auto;

import java.util.HashSet;
import java.util.Set;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.citywithincity.interfaces.IViewContainer;
import com.citywithincity.utils.FragmentUtil;
import com.damai.core.DMLib;
import com.damai.core.ILife;
import com.damai.helper.ActivityResult;

public class DMFragment extends Fragment implements IViewContainer {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DMLib.getJobManager().onCreate(this);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		DMLib.getJobManager().onViewCreate(this);
	}
	
	@Override
	public void onPause() {
		if(lifeSet!=null){
			for (ILife life : lifeSet) {
				life.onPause(this);
			}
		}
		super.onPause();
	}
	

	@Override
	public void onDestroy() {
		lifeSet = null;
		DMLib.getJobManager().onDestroy(this);
		super.onDestroy();
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		if(lifeSet!=null){
			for (ILife life : lifeSet) {
				life.onResume(this);
			}
		}
		DMLib.getJobManager().onResume(this);
	}


	@Override
	public View findViewById(int id) {
		return getView().findViewById(id);
	}

	public void onNewIntent(Intent intent){
		if(lifeSet!=null){
			for (ILife life : lifeSet) {
				life.onNewIntent(intent,this);
			}
		}
	}
	private Set<ILife> lifeSet;
	@Override
	public void addLife(ILife life) {
		if (lifeSet == null) {
			lifeSet = new HashSet<ILife>();
		}
		if(!lifeSet.contains(life)){
			lifeSet.add(life);
		}
		
	}

	
	public String getPackageName() {
	
		return getActivity().getPackageName();
	}
	@Override
	public View findViewByName(String name) {
		//查找
		Resources resources = getResources();
		int id = resources.getIdentifier(name, "id", getPackageName());
		return findViewById(id);
	}

	@Override
	public int getViewId(String name) {
		Resources resources = getResources();
		return resources.getIdentifier(name, "id", getPackageName());
	}

	@Override
	public void finish() {

		FragmentUtil.removeFragment(getActivity(),this,true);
	}



	
	
	protected IViewContainer getContainer(){
		return (IViewContainer)getActivity();
	}

	@Override
	public String idToString(int id) {
		return getContainer().idToString(id);
	}

	@Override
	public void startActivityForResult(ActivityResult result, Intent intent,
			int requestCode) {
		((IViewContainer)getActivity()).startActivityForResult(result, intent, requestCode);
	}

	
}
