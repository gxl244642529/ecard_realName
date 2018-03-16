package com.damai.react.camera.interfaces;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;

import com.damai.react.camera.view.ViewfinderView;
import com.google.zxing.Result;


public interface IScanActivity {
	
	public static final int SCAN_ACTION = 888;
	
	Handler runningHandler();

	void handleDecode(Result obj, Bitmap barcode);

	void setResult(int resultOk, Intent obj);


	void startActivity(Intent intent);

	void drawViewfinder();

	ViewfinderView getViewfinderView();
}
