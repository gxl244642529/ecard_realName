package com.damai.widget;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.damai.interfaces.PopupListener;
import com.damai.lib.R;
public class Dialogs {
	

	
	
	
	/**
	 * 从底部弹出对话框，并且使用title和一个ok按钮
	 * @param context
	 * @param contentView
	 * @param title
	 * @param onClickListener
	 * @return
	 */
	public static <T extends View> Dialog createBottomPopup(Activity context,final T contentView,String title,final PopupListener<T> onClickListener){
		View container =Popups.createContentView(context, R.layout._bottom_popup);
		TextView textView = (TextView) container.findViewById(R.id._text_view);
		textView.setText(title);
		
		ViewGroup viewGroup = (ViewGroup) container.findViewById(R.id._container);
		viewGroup.addView(contentView);
		final Dialog dialog = Popups.bottomPopup(context, container);
		container.findViewById(R.id._id_ok).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				onClickListener.onPopup(R.id._id_ok,contentView);
			}
		});
		dialog.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				onClickListener.onPopup(R.id._id_cancel,contentView);
			}
		});
		return dialog;
	}
	

	
}
