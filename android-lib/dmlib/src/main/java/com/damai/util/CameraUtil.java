package com.damai.util;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.citywithincity.utils.ExternalUtil;
import com.damai.error.UnexpectedException;
import com.damai.helper.ActivityResult;
import com.damai.helper.ActivityResultContainer;

public class CameraUtil {
	

	public static void start(Activity context,File file,ActivityResult listener){
		((ActivityResultContainer)context).startActivityForResult(listener, getCaptureIntent(context,file), ExternalUtil.REQUEST_CAPTURE_IMAGE);
	}
	
	public static Intent getCaptureIntent(Context context, File imageFile){
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);


		Uri imageUri;
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
			imageUri = FileProvider.getUriForFile(context,
                    context.getPackageName() + ".fileprovider", imageFile);
		} else {
			imageUri = Uri.fromFile(imageFile);
		}
		intent.putExtra("autofocus", true); // 自动对焦
		intent.putExtra("fullScreen", false); // 全屏
		intent.putExtra("showActionIcons", false);
		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageUri);
		return intent;
	}
	
	/**
	 * 检测文件
	 * @param file
	 * @throws UnexpectedException 
	 */
	public static void checkCaptureFile(File file) throws UnexpectedException{
		if(file==null || !file.exists()){
			throw new UnexpectedException("摄像头捕获图像不存在");
		}
		if(file.length()==0){
			throw new UnexpectedException("摄像头拍摄照片为空，请确认是否打开了摄像头权限");
		}
	}
}
