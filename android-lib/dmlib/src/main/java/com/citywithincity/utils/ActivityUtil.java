package com.citywithincity.utils;

import java.io.Serializable;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.citywithincity.mvc.ModelHelper;

public class ActivityUtil {
	public static void startActivity(Context context,
			Class<? extends Activity> clazz) {
		context.startActivity(new Intent(context, clazz));
	}

	public static void startActivity(Context context,
			Class<? extends Activity> clazz, Bundle data) {
		Intent intent = new Intent(context, clazz);
		intent.putExtras(data);
		context.startActivity(intent);
	}
	
	public static void startActivity(Class<? extends Activity> clazz) {
		Activity activity = ModelHelper.getActivity();
		activity.startActivity(new Intent(activity, clazz));
	}
	public static void onDetail(Context context,Class<? extends Activity> clazz,Object data){
		ModelHelper.setListData(data);
		context.startActivity(new Intent(context, clazz));
	}
	
	public static void startActivity(Context context,
			Class<? extends Activity> clazz, Serializable data) {
		Intent intent = new Intent(context, clazz);
		intent.putExtra("data", data);
		context.startActivity(intent);
	}
	
	public static void startActivityForResult(Activity context,
			Class<? extends Activity> clazz, Serializable data,int requestCode) {
		Intent intent = new Intent(context, clazz);
		intent.putExtra("data", data);
		context.startActivityForResult(intent,requestCode);
	}

	public static void startActivityForResult(Activity context,
			Class<? extends Activity> clazz, int requestCode) {
		context.startActivityForResult(new Intent(context, clazz), requestCode);
	}

	public static void startActivityForResult(Activity context,
			Class<? extends Activity> clazz, Bundle bundle, int requestCode) {
		Intent intent = new Intent(context, clazz);
		intent.putExtras(bundle);
		context.startActivityForResult(intent, requestCode);
	}

	/**
	 * 拨打电话
	 * 
	 * @param context
	 * @param phoneNum
	 */
	public static void makeCall(final Context context, String phoneNum) {
		String inputStr = phoneNum;
		if (inputStr.trim().length() != 0) {

			if (phoneNum.indexOf('|') > 0) {
				final String[] phones = phoneNum.split("\\|");
				Dialog dialog = new AlertDialog.Builder(context)
						.setItems(phones,
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										String phoneNum = phones[which];
										Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
										phoneIntent.setData(Uri.parse("tel:" + phoneNum));
										// 启动
										context.startActivity(phoneIntent);
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub

									}
								}).create();
				dialog.show();

			} else {
				Intent phoneIntent = new Intent(Intent.ACTION_DIAL);
				phoneIntent.setData(Uri.parse("tel:" + phoneNum));
				// 启动
				context.startActivity(phoneIntent);
			}

		}
	}

}
