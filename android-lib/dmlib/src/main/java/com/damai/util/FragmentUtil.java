package com.damai.util;

import android.content.Context;

public class FragmentUtil {

	public static interface IFragmentAction {
		void addTo(Object fragment, Context context,boolean playAnimation, boolean addToBackStack);

		void replaceTo(Object fragment, Context context,boolean playAnimation, boolean addToBackStack);
	}

	private static IFragmentAction gFragmentAction;

	public static void setFragmentAction(IFragmentAction action) {
		gFragmentAction = action;
	}

	public static void addTo(Object fragment, Context context,boolean playAnimation, boolean addToBackStack) {
		gFragmentAction.addTo(fragment, context,playAnimation,addToBackStack);
	}

	public static void replaceTo(Object fragment, Context context,boolean playAnimation, boolean addToBackStack) {
		gFragmentAction.replaceTo(fragment, context,playAnimation,addToBackStack);
	}
}
