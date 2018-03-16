package com.citywithincity.utils;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.View;
import android.widget.Toast;

import com.citywithincity.interfaces.DialogListener;
import com.damai.alert.IAlert;
import com.damai.alert.IWait;

public class Alert {
	public static interface IInputListener {
		void onInput(String text);

		void onInputCancel();
	}

	public static interface IDialog {
		void dismiss();

		/**
		 * 设置内容
		 * 
		 * @param resID
		 * @return
		 */
		IDialog setContent(int resID);

		/**
		 * 设置内容
		 * 
		 * @param resID
		 * @return
		 */
		IDialog setContent(View contentView);

		/**
		 * 显示
		 */
		void show();

		/**
		 * 设置标题
		 * 
		 * @param title
		 * @return
		 */
		IDialog setTitle(String title);

		/**
		 * 
		 * @param ok
		 * @return
		 */
		IDialog setOkText(String ok);

		/**
		 * 
		 * @param ok
		 * @return
		 */
		IDialog setOkText(int ok);

		/**
		 * 
		 * @param cancel
		 * @return
		 */
		IDialog setCancelText(String cancel);

		/**
		 * 
		 * @param cancel
		 * @return
		 */
		IDialog setCancelText(int cancel);

		/**
		 * 
		 * @param v
		 */
		IDialog setCancelOnTouchOutside(boolean v);

		/**
		 * 设置监听
		 * 
		 * @param listener
		 * @return
		 */
		IDialog setDialogListener(DialogListener listener);

		/**
		 * 初始化视图
		 * 
		 * @param listener
		 * @return
		 */
		IDialog setOnInitContentViewListener(OnDialogInitContentView listener);
	}

	public static interface ICDCDialogListener {
		/**
		 * 
		 * @param dialogId
		 */
		void onCDCDialogEvent(int buttonId, int dialogId);
	}

	public static interface IOnSelect<T> {
		void onSelectData(int position, T data);
	}

	public static interface IOnSelectData<T> extends IOnSelect<T> {

		String getLabel(T data);
	}

	/**
	 * 
	 * @param context
	 */
	public static <T> void select(Context context, String title,
			List<T> selectData, int selectedIndex,
			IOnSelectData<T> onSelectListener) {

		gAlert.select(context, title, selectData, selectedIndex,
				onSelectListener);
	}

	/**
	 * 选择
	 * 
	 * @param context
	 * @param options
	 * @param onSelectListener
	 */
	public static void select(Context context, String title, String[] options,
			int selectedIndex, IOnSelect<String> onSelectListener) {
		gAlert.select(context, title, options, selectedIndex, onSelectListener);

	}

	public static <T> void showSelect(Context context,
			final List<T> selectData, final IOnSelectData<T> onSelectListener) {
		gAlert.showSelect(context, selectData, onSelectListener);
	}

	public static void showSelect(Context context, String[] selectData,
			DialogInterface.OnClickListener onSelectListener) {
		gAlert.showSelect(context, selectData, onSelectListener);
	}

	public static final int OK = 0x1;
	public static final int CANCEL = 0x2;
	public static final int OK_CANCEL = OK | CANCEL;

	/**
	 * 显示自定义的视图
	 * 
	 * @param context
	 * @param contentView
	 * @param title
	 * @param type
	 * @param listener
	 */
	public static IDialog popup(Context context, View contentView,
			String title, int type, DialogListener listener) {

		return gAlert.popup(context, contentView, title, type, listener);
	}

	public static IDialog popup(Context context, View contentView, String title) {
		return popup(context, contentView, title, OK, null);
	}

	public static interface OnDialogInitContentView {
		void onInitContentView(View view);
	}

	private static Context appContext;

	public static void setApplicationContext(Context context) {
		appContext = context;
	}

	public static void showShortToast(String text) {
		Toast.makeText(appContext, text, Toast.LENGTH_SHORT).show();
	}

	public static void showShortToast(CharSequence text) {
		Toast.makeText(appContext, text, Toast.LENGTH_SHORT).show();
	}

	public static void showShortToast(int resID) {
		Toast.makeText(appContext, resID, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 弹出对话框
	 * 
	 * @param context
	 * @return
	 */
	public static void alert(Context context, String title, String message) {
		gAlert.alert(context, title, message);
	}

	public static void alert(Context context, String message) {
		gAlert.alert(context, message);
	}

	/**
	 * 确认 增加下次不再显示
	 * 
	 * @param context
	 * @param dialogId
	 * @param listener
	 */
	public static void confirmCheck(Context context, int dialogId,
			int okTextId, String title, int resID, ICDCDialogListener listener) {

		gAlert.confirmCheck(context, dialogId, okTextId, title, resID, listener);

	}

	/**
	 * 弹出对话框
	 * 
	 * @param context
	 * @return
	 */
	public static IDialog alert(Context context, String title, String message,
			DialogListener listener) {
		return gAlert.alert(context, title, message, listener);
	}

	/**
	 * 原始
	 * 
	 * @param context
	 * @return
	 */
	public static IDialog dialog(Context context, int type) {
		return gAlert.dialog(context, type);
	}

	public static IDialog confirm(Context context, String message,
			DialogListener listener) {
		return confirm(context, message, null, listener);
	}

	public static IDialog confirm(Context context, String title,
			String message, DialogListener listener) {
		return gAlert.confirm(context, title, message, listener);
	}

	/**
	 * 原始popup
	 */
	public static Dialog rawPopup(Context context, View contentView) {
		return gAlert.rawPopup(context, contentView);
	}

	/**
	 * 
	 * @param context
	 * @param title
	 * @param dialogType
	 */
	public static IDialog input(final Context context, String title,
			int dialogType, String defaultValue, final IInputListener listener) {
		return gAlert.input(context, title, dialogType, defaultValue, listener);
	}

	public static void setBtnText(String ok) {

	}

	/**
	 * wait
	 */

	public static void waitCanceledOnTouchOutside(boolean cancel) {

	}

	private static IAlert gAlert;
	private static IWait gWait;

	public static void setWait(IWait wait) {
		gWait = wait;
	}

	public static void setAlert(IAlert alert) {
		gAlert = alert;
	}

	/**
	 * 默认情况下，不能取消
	 * 
	 * @param context
	 * @param title
	 */
	public static void wait(Context context, String title) {
		gWait.wait(context, title,false,null);
	}

	public static void wait(Context context, int title) {
		String strTitle = context.getResources().getString(title);
		gWait.wait(context, strTitle,false,null);
	}

	public static void wait(Context context, String title,OnCancelListener onCancelListener) {
		gWait.wait(context, title, true,onCancelListener);
	}


	public static void wait(Context context, String title, boolean canCancel,
			OnCancelListener onCancelListener) {
		gWait.wait(
				context,
				title,
				canCancel, onCancelListener);
	}

	public static void cancelWait() {
		gWait.cancelWait();
	}
}
