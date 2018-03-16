package com.citywithincity.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.damai.util.AlbumUtil;


public class ExternalUtil {

	public static final int REQUEST_CAPTURE_IMAGE = 200;
	public static final int REQUEST_SELECT_IMAGE = 201;
	public static final int REQUEST_CROP_IMAGE = 202;

	/**
	 * 将bitmap对象写入文件
	 * 
	 * @param bitmap
	 * @param file
	 * @return
	 */
	public static boolean saveAs(Bitmap bitmap, File file) {
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(file);
			bitmap.compress(CompressFormat.JPEG, 100, outputStream);
			return true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		return false;

	}

	/**
	 * 调用摄像头拍照
	 * 
	 * @param parent
	 *            :MainActivity
	 * @param imageFile
	 * @param requestCode
	 */
	public static void captureImage(Activity parent, File imageFile,
			int requestCode) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		Uri imageFileUri = Uri.fromFile(imageFile);
		intent.putExtra("autofocus", true); // 自动对焦
		intent.putExtra("fullScreen", false); // 全屏
		intent.putExtra("showActionIcons", false);
		intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);

		parent.startActivityForResult(intent, requestCode);

	}

	/**
	 * 从相册选择图片
	 * 
	 * @param parent
	 *            ：要显示的activity，只能是同一个activity（）
	 * @param requestCode
	 */
	public static void selectImage(Activity parent, int requestCode) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");
		parent.startActivityForResult(Intent.createChooser(intent,"选择图片"),requestCode);
	}

	public static boolean onSelectImageSaveAs(Context context, Intent data,
			File file) {
		Uri uri = data.getData();// 抽取数据
		ContentResolver cr = context.getContentResolver();
		InputStream is;
		try {
			is = cr.openInputStream(uri);
			return FileUtil.copyInputStream(is, file);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return false;
	}

	/**
	 * 选择图片后另存为一张新的图片
	 * 
	 * @param context
	 * @param data
	 * @param file
	 * @return
	 */
	public static Bitmap onSelectImageSaveAs(Context context, Intent data,
			File file, int dw, int dh) {
		Uri uri = data.getData();// 抽取数据
		ContentResolver cr = context.getContentResolver();
		try {
			// 数据写入BitMap中
			BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
			bmpFactoryOptions.inJustDecodeBounds = true;
			InputStream is = cr.openInputStream(uri);
			Bitmap bitmap = BitmapFactory.decodeStream(is, null,
					bmpFactoryOptions);
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
					/ (float) dw);
			int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
					/ (float) dh);

			if (heightRatio > 1 && widthRatio > 1) {
				if (heightRatio > widthRatio) {
					bmpFactoryOptions.inSampleSize = heightRatio;
				} else {
					bmpFactoryOptions.inSampleSize = widthRatio;
				}

			}

			bmpFactoryOptions.inJustDecodeBounds = false;
			is = cr.openInputStream(uri);
			bitmap = BitmapFactory.decodeStream(is, null, bmpFactoryOptions);
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			saveAs(bitmap, file);
			return bitmap;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 图片剪裁
	 * 
	 * @param parent
	 * @param requestCode
	 * @param photoUri
	 *            原始、目标图片uri Uri.fromFile
	 * @param outputX
	 *            目标大小
	 * @param outputY
	 */
	public static void cropImage(Activity parent, int requestCode,
			Uri photoUri, int outputX, int outputY) {
		Intent intent = AlbumUtil.getCropIntent(photoUri, outputX, outputY);
		parent.startActivityForResult(intent, requestCode);
	}
}
