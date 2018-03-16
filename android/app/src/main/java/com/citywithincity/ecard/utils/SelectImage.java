package com.citywithincity.ecard.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.citywithincity.utils.Alert;
import com.citywithincity.utils.ExternalUtil;
import com.citywithincity.utils.SDCardUtil;
import com.citywithincity.widget.ActionSheet;
import com.citywithincity.widget.ActionSheet.OnActionSheetListener;
import com.damai.error.UnexpectedException;
import com.damai.helper.ActivityResult;
import com.damai.helper.OnSelectDataListener;
import com.damai.util.AlbumUtil;
import com.damai.util.CameraUtil;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class SelectImage implements OnActionSheetListener,ActivityResult {
	
	private Activity context;

	SelectImageListener listener;
	
	public interface SelectImageListener extends OnSelectDataListener<File>{
	}
	
	public SelectImage(Activity activity,SelectImageListener listener){
		this.context = activity;
		this.listener = listener;
	}
	
	
	public void selectImage(){
		
		new ActionSheet(context, new String[]{"选择图片","拍照"}).setTitle("图片").setOnActionSheetListener(this)
		.show();
	}

	 File temp;
	@Override
	public void onActionSheet(int index) {
		if(index==0){
			Acp.getInstance(context).request(new AcpOptions.Builder()
					.setPermissions("android.permission.CAMERA").build(), new AcpListener() {
				@Override
				public void onGranted() {
					AlbumUtil.start(context,SelectImage.this);
				}

				@Override
				public void onDenied(List<String> permissions) {
					Alert.alert(context,"请打开SD卡权限");
				}
			});

		}else{
			try {
				temp= SDCardUtil.getTempImageFile(context);
				CameraUtil.start(context,temp, this);
			} catch (IOException e) {
				Alert.alert(context,"请打开SD卡权限");
			}


		}
	}


	@Override
	public void onActivityResult(Intent intent, int resultCode, int requestCode) {
		if(resultCode == Activity.RESULT_OK){
			if(requestCode == ExternalUtil.REQUEST_CAPTURE_IMAGE){
				File file = temp;
				if(file==null || !file.exists()){
					Alert.alert(context, "保存图片失败，请确认是否应打开权限");
					return;
				}
				listener.onSelectData(file);
			}else{
				try{
                    String path = AlbumUtil.getPath(context, intent);
                    if(path==null){
                        Alert.alert(context,"获取文件失败,请选择其他图片");
                        return;
                    }
					listener.onSelectData(new File(path));
				}catch (UnexpectedException e) {
					Alert.alert(context, e.getMessage());
				}
			}
				
		}
	}
	
}
