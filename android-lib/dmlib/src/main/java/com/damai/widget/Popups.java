package com.damai.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;

import com.damai.interfaces.PopupListener;
import com.damai.lib.R;

public class Popups {
	/**
	 * 从底部弹出对话框，并且使用title和一个ok按钮
	 * @param context
	 * @param contentView
	 * @param title
	 * @param onClickListener
	 * @return
	 */
	public static <T extends View> Dialog createBottomPopup(Activity context,final T contentView,final PopupListener<T> onClickListener){
		int screenWidth = context.getWindowManager().getDefaultDisplay().getWidth();
		contentView.setMinimumWidth(screenWidth);
		final Dialog dialog = bottomPopup(context, contentView);
		View okView = contentView.findViewById(R.id._id_ok);
		if(okView!=null){
			okView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					onClickListener.onPopup(R.id._id_ok,contentView);
				}
			});
		}
		View cancelView = contentView.findViewById(R.id._id_cancel);
		if(cancelView!=null){
			cancelView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					onClickListener.onPopup(R.id._id_cancel,contentView);
				}
			});
		}
		
		dialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				onClickListener.onPopup(R.id._id_cancel,contentView);
			}
		});
		return dialog;
	}
	
	public static Dialog bottomPopup(Context context,View contentView){
		Dialog customDialog = new Dialog(context, R.style._dialog_bottom_popup);
		WindowManager.LayoutParams localLayoutParams = customDialog.getWindow()
				.getAttributes();
		localLayoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
		localLayoutParams.x = 0;
		localLayoutParams.y = 0;

		customDialog.onWindowAttributesChanged(localLayoutParams);
		customDialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		customDialog.setCanceledOnTouchOutside(false);
		customDialog.setCancelable(true);
		customDialog.setCanceledOnTouchOutside(true);
		
		customDialog.setContentView(contentView);
		customDialog.show();
		return customDialog;
	}
	
	
	static View createContentView(Activity context,int layout){
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View contentView = layoutInflater.inflate(layout, null);
		 @SuppressWarnings("deprecation")
		int screenWidth = context.getWindowManager().getDefaultDisplay().getWidth();
		contentView.setMinimumWidth(screenWidth);
		
		return contentView;
	}
}
