package com.damai.alert;


import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.utils.Alert.ICDCDialogListener;
import com.citywithincity.utils.Alert.IDialog;
import com.citywithincity.utils.Alert.IInputListener;
import com.citywithincity.utils.Alert.IOnSelect;
import com.citywithincity.utils.Alert.IOnSelectData;


/**
 * 在屏幕展示一个对话框
 * @author renxueliang
 *
 */
public interface IAlert {
	
	
	public static final int OK = 0x1;
	public static final int CANCEL = 0x2;
	public static final int OK_CANCEL = OK | CANCEL;
	/**
	 * 带有确定按钮的对话框
	 * @param title
	 */
	void alert(Context context,String title);
	IDialog popup(Context context, View contentView,
			String title, int type, DialogListener listener);
	IDialog alert(Context context, String title, String message,
			DialogListener listener);
	void alert(Context context, String title, String message);
	IDialog dialog(Context context, int type) ;
	IDialog confirm(Context context, String title,
			String message, DialogListener listener);
	Dialog rawPopup(Context context, View contentView) ;
	<T> void showSelect(Context context,
			List<T> selectData, final IOnSelectData<T> onSelectListener) ;
	<T> void select(Context context, String title,
			final List<T> selectData, int selectedIndex,
			final IOnSelectData<T> onSelectListener) ;
	void select(Context context, String title,
			final String[] options, int selectedIndex,
			final IOnSelect<String> onSelectListener);
	void showSelect(Context context, String[] selectData,
			DialogInterface.OnClickListener onSelectListener);
	IDialog input(final Context context, String title,
			int dialogType, String defaultValue, IInputListener listener);
	void confirmCheck(Context context, int dialogId,
			int okTextId, String title, int resID, ICDCDialogListener listener);
}
