package com.citywithincity.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.damai.lib.R;

public class FragmentUtil {
	public static void replaceFragment(FragmentActivity context, int contentID,
			Fragment fragment, boolean playAnimation, boolean addToBackStack) {
		FragmentManager fragmentManager = context.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if (playAnimation)
			fragmentTransaction.setCustomAnimations(R.anim._push_left_in,R.anim._push_left_out);
		fragmentTransaction.replace(contentID, fragment);
		if (addToBackStack) {
			fragmentTransaction.addToBackStack(null);
		}
		fragmentTransaction.commit();
	}

	public static void removeFragment(FragmentActivity context,
			Fragment fragment, boolean playAnimation) {
		FragmentManager fragmentManager = context.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		if (playAnimation)
			fragmentTransaction.setCustomAnimations(R.anim._push_right_in,
					R.anim._push_right_out);

		fragmentTransaction.remove(fragment).commit();
	}

	public static void addFragment(FragmentActivity context, int contentResID,
			Fragment fragment) {
		addFragment(context, contentResID, fragment, true, true);
	}

	public static void addFragment(FragmentActivity context, int contentResID,
			Fragment fragment, boolean playAnimation, boolean addToBackStack) {
		FragmentManager fragmentManager = context.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		if (playAnimation) {
			fragmentTransaction.setCustomAnimations(
					R.anim._base_slide_right_in, 0, 0, R.anim._push_right_out);
		}
		fragmentTransaction.add(contentResID, fragment);
		if (addToBackStack) {
			fragmentTransaction.addToBackStack(null);
		}

		fragmentTransaction.commit();
	}
}
