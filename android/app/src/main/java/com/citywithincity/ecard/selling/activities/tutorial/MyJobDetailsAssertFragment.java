package com.citywithincity.ecard.selling.activities.tutorial;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.citywithincity.ecard.R;
import com.citywithincity.models.LocalData;
import com.citywithincity.models.LocalData.LocalDataMode;

@SuppressLint("NewApi")
public class MyJobDetailsAssertFragment extends Fragment{
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
		View view = inflater.inflate(R.layout.my_jobs_details_assert, null);
		view.findViewById(R.id.btn_instru).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LocalData.createDefault(LocalDataMode.MODE_WRITE).putBoolean("FirstIntoDIYEdit", false)
				.destroy();
				getFragmentManager().beginTransaction()
		    	.remove(MyJobDetailsAssertFragment.this)
		    	.commit();
			}
		});
		return view;
    }
}
