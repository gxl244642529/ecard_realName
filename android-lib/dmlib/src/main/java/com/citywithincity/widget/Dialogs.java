package com.citywithincity.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;

import com.citywithincity.utils.ViewUtil;
import com.damai.lib.R;

public class Dialogs {
	
	public static final int CENTER = 1;
	public static final int BOTTOM = 2;
	
	
	private static Dialog currentDialog;
	
	private static OnDismissListener dismissListener;
	/**
	 * 
	 * @param view
	 */
	public static void addPopup(Context context,View contentView,int type,int minWidth,int maxHeight) {
		
		int gravity;
		int style;
		if(type==CENTER){
			gravity = Gravity.CENTER;
			style =  R.style._dialog_center_popup;
		}else{
			gravity = Gravity.BOTTOM | Gravity.LEFT;
			style =  R.style._dialog_bottom_popup;
		}
		
		Dialog customDialog = new Dialog(context, style);
		WindowManager.LayoutParams localLayoutParams = customDialog.getWindow()
				.getAttributes();
		localLayoutParams.gravity =gravity;// Gravity.BOTTOM | Gravity.LEFT;
		localLayoutParams.x = 0;
		localLayoutParams.y = 0;

		customDialog.onWindowAttributesChanged(localLayoutParams);
		customDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		customDialog.setCanceledOnTouchOutside(false);
		customDialog.setCancelable(true);
		customDialog.setCanceledOnTouchOutside(true);
		
		if(minWidth == LayoutParams.MATCH_PARENT){
			contentView.setMinimumWidth(ViewUtil.screenWidth);
		}else if(minWidth == LayoutParams.WRAP_CONTENT){
			
		}else{
			contentView.setMinimumWidth(minWidth);
		}
		if(maxHeight == LayoutParams.MATCH_PARENT){
			contentView.setMinimumHeight(ViewUtil.screenHeight);
		}else if(maxHeight ==  LayoutParams.WRAP_CONTENT){
			
		}else{
			contentView.setMinimumHeight(maxHeight);
		}
		
		customDialog.setOnDismissListener(diaListener);
		customDialog.setContentView(contentView);
		customDialog.show();
		currentDialog = customDialog;
	}
	
	
	public static void setDismissListener(OnDismissListener listener){
		dismissListener = listener;
	}
	
	public static void hide(){
		if(currentDialog!=null){
			currentDialog.dismiss();
		}
	}
	
	public static boolean isShowing(){
		return currentDialog!=null;
	}
	
	private static OnDismissListener diaListener = new OnDismissListener() {
		
		@Override
		public void onDismiss(DialogInterface dialog) {
			currentDialog = null;
			if(dismissListener!=null){
				dismissListener.onDismiss(dialog);
				dismissListener = null;
			}
			
		}
	};

	
	
	
	
	
}
