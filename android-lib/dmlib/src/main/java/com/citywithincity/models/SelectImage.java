package com.citywithincity.models;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.preference.PreferenceManager.OnActivityResultListener;

import com.citywithincity.interfaces.IDestroyable;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.ExternalUtil;
import com.citywithincity.utils.ThreadUtil;
import com.citywithincity.widget.ActionSheet;
import com.citywithincity.widget.ActionSheet.OnActionSheetListener;

public class SelectImage implements IDestroyable, OnActivityResultListener, OnActionSheetListener {
	public static interface ISelectImageListener{
		void onSelectImage(File file);
	}
	
	Activity context;
	File imageFile;
	ISelectImageListener listener;
	
	public SelectImage(Activity context){
		this.context = context;
	}
	
	public void setListenr(ISelectImageListener listener){
		this.listener = listener;
	}
	
	public  void selectImage(File file){
		imageFile = file;
		
		new ActionSheet(context, new String[]{"选择图片","拍照"}).setTitle("图片").setOnActionSheetListener(this)
		.show();
	}
	
	
	
	
	@Override
	public boolean onActivityResult(int requestCode, int resultCode, final Intent data) {
		if(resultCode != Activity.RESULT_OK)return false;
		if(requestCode == ExternalUtil.REQUEST_CAPTURE_IMAGE){
			listener.onSelectImage(imageFile);
			return true;
		}else if(requestCode == ExternalUtil.REQUEST_SELECT_IMAGE){
			Alert.wait(context, "");
			ThreadUtil.run(new Runnable() {
				@Override
				public void run() {
					ExternalUtil.onSelectImageSaveAs(context, data, imageFile);
					ThreadUtil.post(new Runnable() {
						@Override
						public void run() {
							Alert.cancelWait();
							listener.onSelectImage(imageFile);
						}
					});
				}
			});
			
			return true;
		}
		return false;
	}

	@Override
	public void destroy() {
		Alert.cancelWait();
		context = null;
		listener = null;
		imageFile = null;
	}

	@Override
	public void onActionSheet(int index) {
		if(index==0){
			ExternalUtil.selectImage(context, ExternalUtil.REQUEST_SELECT_IMAGE);
		}else{
			ExternalUtil.captureImage(context, imageFile, ExternalUtil.REQUEST_CAPTURE_IMAGE);
		}
	}

}
