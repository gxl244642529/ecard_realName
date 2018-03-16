package com.citywithincity.ecard.myecard.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.citywithincity.ecard.R;
import com.damai.auto.DMFragment;
import com.damai.helper.a.Event;

public class BindECardStep1 extends DMFragment {

	public interface IBindECardStep1{
		void onBindNormal();
		void onBindExt();
	}
	
	private IBindECardStep1 listener;
	
	public void setListener(IBindECardStep1 listener){
		this.listener = listener;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view= inflater.inflate(R.layout.fragment_bind_step1,null);
		return view;
	}

	@Override
	public void onDestroy() {
		listener = null;
		super.onDestroy();
	}
	
	
	@Event
	public void bind_normal_card(){
		listener.onBindNormal();
	}
	
	
	@Event
	public void bind_ext_card(){
		listener.onBindExt();
	}
	
	
	@Event
	public void bind_by_scan(){
		
	}

	
	

}
