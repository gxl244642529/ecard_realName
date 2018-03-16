package com.citywithincity.ecard.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.ui.activity.MyECardScanActivity;

public class BindECardStep1 extends Fragment implements OnClickListener {

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
		view.findViewById(R.id.bind_normal_card).setOnClickListener(this);
		view.findViewById(R.id.bind_ext_card).setOnClickListener(this);
		view.findViewById(R.id.bind_by_scan).setOnClickListener(this);
		return view;
	}

	@Override
	public void onDestroy() {
		listener = null;
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bind_normal_card:
			listener.onBindNormal();
			break;
		case R.id.bind_ext_card:
			listener.onBindExt();
			break;
		case R.id.bind_by_scan:
		{
			this.getActivity().startActivity(new Intent(getActivity(),MyECardScanActivity.class));
			this.getActivity().finish();
		}
			break;
		default:
			break;
		}
	}

	

}
