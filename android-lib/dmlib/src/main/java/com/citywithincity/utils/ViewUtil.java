package com.citywithincity.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.citywithincity.auto.tools.AutoCreator.IBinddataViewSetterCreator;
import com.citywithincity.interfaces.IEventHandler;
import com.citywithincity.interfaces.IViewContainer;

public class ViewUtil {
	public static final String TAG = "ViewUtil";

	protected static float densityDpi;

	public static int screenWidth;
	public static int screenHeight;
	public static int statusBarHeight;
	
	

	public static void initParam(Activity context) {

		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		densityDpi = (float) dm.densityDpi / 160;

		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;

		Rect frame = new Rect();
		context.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		statusBarHeight = frame.top;
	}

	public static void setViewGroupOnClickListener(ViewGroup viewGroup,
			OnClickListener onClickListener) {
		for (int i = 0, count = viewGroup.getChildCount(); i < count; ++i) {
			View child = viewGroup.getChildAt(i);
			child.setTag(i);
			child.setOnClickListener(onClickListener);
		}
	}

	public static float dipToPx(int dip) {
		return dip * densityDpi;
	}

	public static float pxToDip(int px) {
		return px / densityDpi;
	}

	public static void setTextFieldValue(Activity context, int resID,
			String text) {
		((TextView) context.findViewById(resID)).setText(text);
	}

	public static void setTextFieldValue(View view, int id, String value) {
		if (value == null) {
			((TextView) view.findViewById(id)).setText("");
		} else {
			((TextView) view.findViewById(id)).setText(value);
		}
	}

	public static String getTextFieldValue(View view, int id) {
		return ((TextView) view.findViewById(id)).getText().toString().trim();
	}

	/**
	 * 设置绑定数据视图
	 * 
	 * @param data
	 * @param view
	 */
	public static void setBinddataViewValues(Object data, Activity view) {
		binddataViewSetterCreator.setBinddataViewValues(data, view);
	}
	protected static IEventHandler eventHandler;

	protected static IBinddataViewSetterCreator binddataViewSetterCreator;
	
	public static void setBinddataViewSetterCreator(IBinddataViewSetterCreator value){
		binddataViewSetterCreator  = value;
	}
	public static void setEventHandler(IEventHandler value){
		eventHandler  = value;
	}
	public static void setBinddataViewValues(Object data, View view,int layoutID) {
		binddataViewSetterCreator.setBinddataViewValues(data, view, layoutID);
	}
	
	public static void registerEvent(IViewContainer parent){
		eventHandler.registerEvent(parent);
	}
}
