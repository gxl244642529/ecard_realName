package com.citywithincity.ecard.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.citywithincity.ecard.R;

public class BindECardStep2_2 extends Fragment implements OnClickListener {
	public interface IBindStep2{
		void onStep2(int resID);
	}
	
	
	private int layoutResID;
	private IBindStep2 listener;
	
	public void setLayout(int layoutResID){
		this.layoutResID=layoutResID;
	}
	public void setListener( IBindStep2 listener){
		this.listener  = listener;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(layoutResID, null);
		view.findViewById(R.id._next).setOnClickListener(this);
	//	view.findViewById(R.id.)
		return view;
	}

	@Override
	public void onClick(View v) {
		listener.onStep2(layoutResID);
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		listener = null;
		super.onDestroy();
	}
}
