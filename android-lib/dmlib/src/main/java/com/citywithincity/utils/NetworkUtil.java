package com.citywithincity.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class NetworkUtil {
	public static final int NETWORK_NONE=0;
	
	public static final int NETWORK_3G=1;
	
	public static final int NETWORK_WIFI=2;
	
	
	/**
	 * 获取网络类型
	 * @param context
	 * @return
	 */
	public static int getNetworkType(Context context){
		if (PermissionUtil.checkPermissions(context, "android.permission.INTERNET")) {
			ConnectivityManager connectMgr = (ConnectivityManager) context
			        .getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = connectMgr.getActiveNetworkInfo();
			
			if(info==null){
				return NETWORK_NONE;
			}
			
			if(info.getType()==ConnectivityManager.TYPE_WIFI)
			{
				return NETWORK_WIFI;
			}
			return NETWORK_3G;
		}
		return NETWORK_NONE;
	}
	
	/**
	 * Get the current networking
	 * 
	 * @param context
	 * @return WIFI or MOBILE
	 */
	public static String getNetworking(Context context) {
		TelephonyManager manager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		int type = manager.getNetworkType();
		String typeString = "UNKNOWN";
		if (type == TelephonyManager.NETWORK_TYPE_CDMA) {
			typeString = "CDMA";
		}
		if (type == TelephonyManager.NETWORK_TYPE_EDGE) {
			typeString = "EDGE";
		}
		if (type == TelephonyManager.NETWORK_TYPE_EVDO_0) {
			typeString = "EVDO_0";
		}
		if (type == TelephonyManager.NETWORK_TYPE_EVDO_A) {
			typeString = "EVDO_A";
		}
		if (type == TelephonyManager.NETWORK_TYPE_GPRS) {
			typeString = "GPRS";
		}
		if (type == TelephonyManager.NETWORK_TYPE_HSDPA) {
			typeString = "HSDPA";
		}
		if (type == TelephonyManager.NETWORK_TYPE_HSPA) {
			typeString = "HSPA";
		}
		if (type == TelephonyManager.NETWORK_TYPE_HSUPA) {
			typeString = "HSUPA";
		}
		if (type == TelephonyManager.NETWORK_TYPE_UMTS) {
			typeString = "UMTS";
		}
		if (type == TelephonyManager.NETWORK_TYPE_UNKNOWN) {
			typeString = "UNKNOWN";
		}
		if (type == TelephonyManager.NETWORK_TYPE_1xRTT) {
			typeString = "1xRTT";
		}
		if (type == 11) {
			typeString = "iDen";
		}
		if (type == 12) {
			typeString = "EVDO_B";
		}
		if (type == 13) {
			typeString = "LTE";
		}
		if (type == 14) {
			typeString = "eHRPD";
		}
		if (type == 15) {
			typeString = "HSPA+";
		}

		return typeString;
	}
	
	/**
	 * 是否有网络
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvaliable(Context context){
		if (PermissionUtil.checkPermissions(context, "android.permission.INTERNET")) {
			ConnectivityManager connectMgr = (ConnectivityManager) context
			        .getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = connectMgr.getActiveNetworkInfo();
			
			return info!=null;
		}
		return false;
	}
}
