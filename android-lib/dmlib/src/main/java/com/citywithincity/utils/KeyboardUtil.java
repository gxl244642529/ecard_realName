package com.citywithincity.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.citywithincity.utils.MessageUtil.IMessageListener;


public class KeyboardUtil {

	public static void showSoftKeyboard(final Context context,final View text,int delay)
	{
		text.requestFocus();
		MessageUtil.sendMessage(0,new IMessageListener() {
			
			@Override
			public void onMessage(int message) {
				// TODO Auto-generated method stub
				InputMethodManager imm=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(text, InputMethodManager.RESULT_SHOWN);
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
			}
		},delay);
	}
	/**
	 * 显示软键盘
	 */
	public static void showSoftKeyboard(Context context,View text)
	{
		showSoftKeyboard(context,text,300);
	}
	
	public static void hideSoftKeyboard(Context context,View view)
	{
		InputMethodManager imm=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘 
	}
	
}
