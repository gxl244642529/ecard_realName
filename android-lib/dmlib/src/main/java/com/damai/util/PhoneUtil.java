package com.damai.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

public class PhoneUtil {
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
										Intent phoneIntent = new Intent(
												Intent.ACTION_DIAL);
										phoneIntent.setData(Uri.parse("tel:"
												+ phoneNum));
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
