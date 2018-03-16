package com.citywithincity.camera.interfaces;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;

import com.citywithincity.camera.view.ViewfinderView;
import com.google.zxing.Result;

public interface IScanActivity {
	
	public static final int SCAN_ACTION = 888;
	
	Handler runningHandler();

	void handleDecode(Result obj, Bitmap barcode);

	void setResult(int resultOk, Intent obj);

	void finish();

	void startActivity(Intent intent);

	void drawViewfinder();

	ViewfinderView getViewfinderView();
}
