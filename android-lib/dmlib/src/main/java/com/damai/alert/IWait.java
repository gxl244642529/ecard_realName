package com.damai.alert;

import android.content.Context;
import android.content.DialogInterface.OnCancelListener;


/**
 * 在屏幕上面展示一个等待对话框
 * @author renxueliang
 *
 */
public interface IWait {
	
	/**
	 * 以下常亮为取消级别，当用户手动操作导致取消的时候，会出现的反应。
	 */
	
	/**
	 * 不能取消
	 */
	//public static final int CancelLevel_NoCancel = 0;
	
	/**
	 * 取消的同时，取消正在执行的任务
	 */
	//public static final int CancelLevel_CancelJob = 1;
	
	/**
	 * 取消的同时，退出当前activity
	 */
	//public static final int CancelLevel_ExitActivity = 2;
	
	
	/**
	 * 取消的同时，退出当前fragment
	 */
	//public static final int CancelLevel_ExitFragment = 3;
	
	/**
	 * 取消的同时，退出当前activity或者fragment（自动判断）
	 */
//	public static final int CancelLevel_CancelTop = 4;

	
	/**
	 * 可取消，取消时调用onCancelListener
	 * @param title
	 * @param onCancelListener
	 */
	void wait(Context context,String title,boolean cancelable,OnCancelListener onCancelListener);
	
	
	
	
	/**
	 * 取消等待，此时不会触发取消级别中指定的操作
	 */
	void cancelWait();
}
