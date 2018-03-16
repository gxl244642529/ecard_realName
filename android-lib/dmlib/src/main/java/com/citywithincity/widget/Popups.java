package com.citywithincity.widget;

import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.damai.lib.R;
public class Popups {
	
	static PopupWindow popupWindow;
	
	/**
	 * 增加下拉菜单
	 * @param contentView
	 * @param baseView
	 * @param dismissListener
	 */
	public static void addPopup(View contentView,View baseView,boolean outsideTouchable, OnDismissListener dismissListener){
		if(popupWindow!=null){
			popupWindow.dismiss();
		}
		PopupWindow pop = new PopupWindow(contentView, android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		pop.setFocusable(true);
		pop.setAnimationStyle(R.style._animation_top_popup);
		pop.setOnDismissListener(dismissListener);
		pop.setOutsideTouchable(outsideTouchable);
		pop.setBackgroundDrawable(new ColorDrawable(0x50000000));
		pop.showAsDropDown(baseView, 0, 0);
		popupWindow = pop;
	}

	public static void hide() {
		if(popupWindow!=null){
			popupWindow.dismiss();
			popupWindow = null;
		}
	}
}
