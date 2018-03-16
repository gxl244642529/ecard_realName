package com.citywithincity.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.nfc.NfcManager;

public class PermissionUtil {
	/**
	 * 检查是否有这个权限
	 * 
	 * @param context
	 * @param permission
	 * @return true or false
	 */
	public static boolean checkPermissions(Context context, String permission) {
		PackageManager localPackageManager = context.getPackageManager();
		return localPackageManager.checkPermission(permission,
				context.getPackageName()) == PackageManager.PERMISSION_GRANTED;
	}
	
	

	/**
	 * 获取系统版本号
	 * 
	 * @param context
	 * @return
	 */
	public static String getOsVersion(Context context) {
		if (checkPhoneState(context)) {
			return android.os.Build.VERSION.RELEASE;
		}
		return null;
	}

	/**
	 * 是否具有read_phone_state
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkPhoneState(Context context) {
		PackageManager packageManager = context.getPackageManager();
		if (packageManager
				.checkPermission("android.permission.READ_PHONE_STATE",
						context.getPackageName()) != 0) {
			return false;
		}
		return true;
	}

	/**
	 * 是否有重力感应装置
	 * 
	 * @return
	 */
	public static boolean isHaveGravity(Context context) {
		SensorManager manager = (SensorManager) context
				.getSystemService(Context.SENSOR_SERVICE);
		if (manager == null) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * 是否有nfc装置
	 */
	public static boolean isHaveNfc(Context context){
		NfcManager nfcManager = (NfcManager)context.getSystemService("nfc");
		return nfcManager!=null;
	}


}
