package com.citywithincity.ecard.models;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.citywithincity.ecard.R;

public class ViewManager {
	FragmentManager fm;
	public ViewManager(FragmentActivity activity){
		fm = activity.getSupportFragmentManager();
	}
	
	public void pushView(Fragment fragment)
	{
		FragmentTransaction ft = fm.beginTransaction();
	//	ft.setCustomAnimations(R.anim.base_slide_right_in,0, 0, R.anim.push_right_out);
		ft.add(R.id._container, fragment);
		ft.addToBackStack(null);
		ft.commit();
	}
	
	public boolean canPopup(){
		return fm.getBackStackEntryCount() > 0 ;
	}
	
	public String getLastFragment(){
		return fm.getBackStackEntryAt(0).getName();
	}
	
	public void popupView(){
		fm.popBackStack();
	}
}
