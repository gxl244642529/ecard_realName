package com.citywithincity.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.damai.lib.R;
import com.damai.widget.Popups;


/**
 * 底部弹出框
 * @author Randy
 *
 */
public class ActionSheet implements OnClickListener, OnDismissListener {

	public static interface OnActionSheetListener{
		void onActionSheet(int index);
	}
	
	public static interface OnActionSheetCancelListener{
		void onActionSheetCancel();
	}
	
	
	private OnActionSheetListener listener;
	private OnActionSheetCancelListener cancelListener;
	private Dialog customDialog;
	private final String[] labels;
	private final Activity context;
	private String title;
	
	public ActionSheet(Activity contexts,String[] labels){
		this.labels = labels;
		this.context = contexts;
	}
	
	
	public ActionSheet setOnActionSheetListener(OnActionSheetListener listener){
		this.listener = listener;
		return this;
	}
	
	public ActionSheet setOnActionSheetCancelListener(OnActionSheetCancelListener listener){
		cancelListener = listener;
		return this;
	}
	
	public ActionSheet setTitle(String title){
		this.title = title;
		return this;
	}
	
	
	
	public void show(){
		this.customDialog = Popups.bottomPopup(context, createContentView());
		this.customDialog.setOnDismissListener(this);
	}
	
	private View createContentView(){
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View contentView = layoutInflater.inflate(R.layout._action_sheet, null);
		 @SuppressWarnings("deprecation")
		int screenWidth = context.getWindowManager().getDefaultDisplay().getWidth();
		contentView.setMinimumWidth(screenWidth);
		ViewGroup viewGroup = (ViewGroup)contentView.findViewById(R.id._container);
		int index = 1000;
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,(int) context.getResources().getDimension(R.dimen._action_sheet_button_height));
		lp.topMargin = (int)context.getResources().getDimension(R.dimen._action_sheet_button_top_margin);
		
		for (String label : labels) {
			Button child = (Button)layoutInflater.inflate(R.layout._action_sheet_button,null);
			child.setText(label);
			child.setTag(index++);
			child.setOnClickListener(this);
			viewGroup.addView(child,lp);
		}
		if(title!=null){
			((TextView)contentView.findViewById(R.id._text_view)).setText(title);
		}
		
		contentView.findViewById(R.id._action_sheet_cancel).setOnClickListener(this);
		return contentView;
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id._action_sheet_cancel){
			if(cancelListener!=null){
				cancelListener.onActionSheetCancel();
			}
			
		}else{
			int index = (Integer)v.getTag();
			index -= 1000;
			if(listener!=null)
				listener.onActionSheet(index);
		}
		
		
		customDialog.dismiss();
		
		
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		listener = null;
		cancelListener = null;
		customDialog = null;
	}
	
}
