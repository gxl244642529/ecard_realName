package com.citywithincity.ecard.ui.activity;

import android.app.Activity;
import android.content.Intent;

import com.citywithincity.zxinglib.activity.CaptureActivity;





/**
 * Initial the camera
 * @author Ryan.Tang
 */

public class MyScanActivity extends CaptureActivity  {
	public static final int SCAN_ACTION = 1000;
	
	
	public static void callScan(Activity context){
		context.startActivityForResult(new Intent(context,MyScanActivity.class), SCAN_ACTION);
	}
}