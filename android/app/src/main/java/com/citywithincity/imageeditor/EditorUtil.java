package com.citywithincity.imageeditor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.citywithincity.utils.SDCardUtil;
import com.damai.helper.ActivityResult;
import com.damai.helper.ActivityResultContainer;

import java.io.File;
import java.io.IOException;

public class EditorUtil {

	public static final int REQUEST_EDIT_IMAGE = 101;

	/**
	 * 
	 * @param activity
	 * @param requestCode
	 * @param imageFile
	 * @param minWidth
	 * @param minHeight
	 * @param errorLtMinSize
	 */
	public static void editImage(Activity activity, String imageFile,
			int requestCode, int minWidth, int minHeight,
			String errorLtMinSize, boolean needsCutFirst) {

		MIN_CARD_WIDTH = minWidth;
		MIN_CARD_HEIGHT = minHeight;
		ErrorMinSize = errorLtMinSize;

		rate = (float) MIN_CARD_HEIGHT / MIN_CARD_WIDTH;
		ImageEditorActivity.edit(activity, imageFile, needsCutFirst,
				requestCode);
	}
	
	public static void editImage(Activity activity, ActivityResult listener,String imageFile,
			int requestCode, int minWidth, int minHeight,
			String errorLtMinSize, boolean needsCutFirst) {

		MIN_CARD_WIDTH = minWidth;
		MIN_CARD_HEIGHT = minHeight;
		ErrorMinSize = errorLtMinSize;

		rate = (float) MIN_CARD_HEIGHT / MIN_CARD_WIDTH;
		ImageEditorActivity.edit((ActivityResultContainer) activity, listener,
				imageFile, true, EditorUtil.REQUEST_EDIT_IMAGE);
	}
	
	public static String getPath(Intent data){
		return data.getExtras().getString("data");
	}

	public static void editDiy(Activity activity, ActivityResult listener,
			String imageFile) {

		MIN_CARD_WIDTH = DIY_MIN_CARD_WIDTH;
		MIN_CARD_HEIGHT = DIY_MIN_CARD_HEIGHT;
		ErrorMinSize = "为保证打印质量，图片宽度必须至少为" + DIY_MIN_CARD_WIDTH + "像素";
		rate = (float) MIN_CARD_HEIGHT / MIN_CARD_WIDTH;
		ImageEditorActivity.edit((ActivityResultContainer) activity, listener,
				imageFile, true, EditorUtil.REQUEST_EDIT_IMAGE);
	}

	public static void editImage(Activity activity, String imageFile,
			int requestCode, int minWidth, int minHeight) {

		MIN_CARD_WIDTH = minWidth;
		MIN_CARD_HEIGHT = minHeight;
		ErrorMinSize = null;
		rate = (float) MIN_CARD_HEIGHT / MIN_CARD_WIDTH;
		ImageEditorActivity.edit(activity, imageFile, true, requestCode);
	}

	/**
	 * 工作目录
	 */
	static final String WORKING_PATH = "editor";

	/*
	 * 需要开始就剪裁
	 */
	public static final String NEEDS_CUT_FIRST = "needs_cut";

	static final int DIY_MIN_CARD_WIDTH = 1016;
	static final int DIY_MIN_CARD_HEIGHT = 640;

	static int MIN_CARD_WIDTH = DIY_MIN_CARD_WIDTH;
	static int MIN_CARD_HEIGHT = DIY_MIN_CARD_HEIGHT;

	static float rate = (float) MIN_CARD_HEIGHT / MIN_CARD_WIDTH;

	/**
	 * 如果图片小于最小尺寸，提示文字
	 */
	static String ErrorMinSize = "";

	/**
	 * 工作目录
	 * 
	 * @return
	 */
	static File getEditorWorkPath(Context context) throws IOException {
		return SDCardUtil.getSDCardDir(context, WORKING_PATH);
	}

	/**
	 * 
	 * @param context
	 * @return
	 */
	static File getResultFile(Context context) throws IOException {
		return new File(SDCardUtil.getSDCardDir(context, "images"),
				String.valueOf(System.currentTimeMillis()));
	}

	/**
	 * 当前图片目录
	 * 
	 * @return
	 */
	static File getEditorCurrentPath(Context context, String name) throws IOException {
		File file = new File(getEditorWorkPath(context), name);
		if (!file.isDirectory()) {
			file.mkdirs();
		}
		return file;
	}
}
