package com.damai.dl;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.citywithincity.utils.FragmentUtil;
import com.damai.auto.DMFragment;
import com.damai.lib.R;

public class PluginFragment extends DMFragment {
	
	public void replaceTo(Context context, boolean playAnimation,
			boolean addToBackStack) {
		FragmentUtil.replaceFragment((FragmentActivity)context, R.id._container, this, playAnimation, addToBackStack);
	}

}
