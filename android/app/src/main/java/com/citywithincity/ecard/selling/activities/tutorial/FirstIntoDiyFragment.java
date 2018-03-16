package com.citywithincity.ecard.selling.activities.tutorial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.citywithincity.ecard.R;
import com.citywithincity.models.LocalData;
import com.citywithincity.models.LocalData.LocalDataMode;
import com.citywithincity.utils.FragmentUtil;

public class FirstIntoDiyFragment extends Fragment{
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
		View view = inflater.inflate(R.layout.fragment_first_into_diy, null);
		view.findViewById(R.id.btn_instru).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LocalData.createDefault(LocalDataMode.MODE_WRITE).putBoolean("FirstIntoDIY", false)
				.destroy();
				FragmentUtil.removeFragment(getActivity(), FirstIntoDiyFragment.this, true);
			}
		});
		return view;
    }

}
