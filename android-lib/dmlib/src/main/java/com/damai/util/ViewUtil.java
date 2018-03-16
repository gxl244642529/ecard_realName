package com.damai.util;

import android.app.Activity;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

public class ViewUtil {
	public static int screenWidth;
	public static int screenHeight;
	public static int statusBarHeight;
	
	protected static float densityDpi;
	
	
	public static float dipToPx(int dip) {
		return dip * densityDpi;
	}

	public static float pxToDip(int px) {
		return px / densityDpi;
	}

	
	public static void initParam(Activity context) {

		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		densityDpi = (float) dm.densityDpi / 160;

		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;

		Rect frame = new Rect();
		context.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		statusBarHeight = frame.top;
	}
	
	public static View findTopView(View view) {
		while(true){
			ViewParent parent = view.getParent();
			if(parent==null){
				return view;
			}
			view = (View)parent;
		}
	}
	
	
	public static int getMeasuredHeight(View view){
		int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED); 
		int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED); 
		view.measure(w, h); 
		return view.getMeasuredHeight(); 
	}
	
	
	@SuppressWarnings("unchecked")
	public static <T> T findView(ViewGroup viwGroup,Class<T> clazz){
		for(int i=0 , c = viwGroup.getChildCount(); i < c; ++i){
			View view = viwGroup.getChildAt(i);
			if(view.getClass().isAssignableFrom(clazz)){
				return (T)view;
			}
		}
		return null;
	}

	
}
