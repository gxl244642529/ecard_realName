package com.citywithincity.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.media.ExifInterface;
import android.util.DisplayMetrics;
import android.view.View;

public class ImageUtil {
	// 从文件创建
	/*
	 * private StateListDrawable addStateDrawable(Context context, int idNormal,
	 * int idPressed, int idFocused) { StateListDrawable sd = new
	 * StateListDrawable(); Drawable normal = idNormal == -1 ? null :
	 * context.getResources().getDrawable(idNormal); Drawable pressed =
	 * idPressed == -1 ? null : context.getResources().getDrawable(idPressed);
	 * Drawable focus = idFocused == -1 ? null :
	 * context.getResources().getDrawable(idFocused);
	 * //注意该处的顺序，只要有一个状态与之相配，背景就会被换掉
	 * //所以不要把大范围放在前面了，如果sd.addState(new[]{},normal)放在第一个的话，就没有什么效果了
	 * sd.addState(new int[]{android.R.attr.state_enabled,
	 * android.R.attr.state_focused}, focus); sd.addState(new
	 * int[]{android.R.attr.state_pressed, android.R.attr.state_enabled},
	 * pressed); sd.addState(new int[]{android.R.attr.state_focused}, focus);
	 * sd.addState(new int[]{android.R.attr.state_pressed}, pressed);
	 * sd.addState(new int[]{android.R.attr.state_enabled}, normal);
	 * sd.addState(new int[]{}, normal); return sd; }
	 */

	public StateListDrawable addStateDrawable(Context context, int idNormal,
			int idPressed, int idFocused) {
		StateListDrawable sd = new StateListDrawable();
		// new BitmapDrawable(res, filepath);

		Drawable normal = idNormal == -1 ? null : context.getResources()
				.getDrawable(idNormal);
		Drawable pressed = idPressed == -1 ? null : context.getResources()
				.getDrawable(idPressed);
		Drawable focus = idFocused == -1 ? null : context.getResources()
				.getDrawable(idFocused);
		// 注意该处的顺序，只要有一个状态与之相配，背景就会被换掉
		// 所以不要把大范围放在前面了，如果sd.addState(new[]{},normal)放在第一个的话，就没有什么效果了
		sd.addState(new int[] { android.R.attr.state_enabled,
				android.R.attr.state_focused }, focus);
		sd.addState(new int[] { android.R.attr.state_pressed,
				android.R.attr.state_enabled }, pressed);
		sd.addState(new int[] { android.R.attr.state_focused }, focus);
		sd.addState(new int[] { android.R.attr.state_pressed }, pressed);
		sd.addState(new int[] { android.R.attr.state_enabled }, normal);
		sd.addState(new int[] {}, normal);

		new BitmapDrawable(context.getResources(), "");
		return sd;
	}

	public static boolean saveAs(Bitmap bitmap, File file, int quality) throws FileNotFoundException {
		FileOutputStream outputStream = null;
        try{
            outputStream = new FileOutputStream(file);
            return bitmap
                    .compress(CompressFormat.JPEG, quality, outputStream);
        }finally {
            IoUtil.close(outputStream);
        }

	}

	public static boolean saveAs(Bitmap bitmap, File file) throws FileNotFoundException {
		return saveAs(bitmap, file, 100);
	}

	/**
	 * 截图
	 * 
	 * @param view
	 * @param file
	 * @return
	 */
	public static void takeSnapshot(View view, File file) throws FileNotFoundException {
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap src = view.getDrawingCache();

		FileOutputStream outputStream = null;
		try{
			outputStream = new FileOutputStream(file);
			src.compress(CompressFormat.JPEG, 100, outputStream);
			view.destroyDrawingCache();
		}finally {
			IoUtil.close(outputStream);
		}

	}

	/**
	 * 当前屏幕截图
	 * 
	 * @param context
	 * @return
	 */
	public static void takeSnapshot(Activity context, File file) throws FileNotFoundException {
		// 获取屏幕
		View decorview = context.getWindow().getDecorView();
		takeSnapshot(decorview, file);
	}

	public static boolean takeSnapshot(View view, File file, int width,
			int height) {
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap src = view.getDrawingCache();
		src = resize(src, width, height);
		FileOutputStream outputStream;
		try {
			outputStream = new FileOutputStream(file);
			return src.compress(CompressFormat.JPEG, 100, outputStream);
		} catch (FileNotFoundException e) {
			return false;
		} finally {
			view.destroyDrawingCache();
		}
	}

	/**
	 * 当前屏幕截图
	 * 
	 * @param context
	 * @return
	 */
	public static File takeSnapshot(Activity context) throws IOException {
		File file = new File(
				SDCardUtil.getCacheDirectory(context, "screenshot"),
				System.currentTimeMillis() + ".jpg");
		// 获取屏幕
		View decorview = context.getWindow().getDecorView();
        takeSnapshot(decorview, file);
        return file;
	}

	public static Bitmap decodeBitmap(Resources res, int resID, int dw, int dh) {
		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		bmpFactoryOptions.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory
				.decodeResource(res, resID, bmpFactoryOptions);
		int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
				/ (float) dh);
		int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
				/ (float) dw);

		if (heightRatio > 1 && widthRatio > 1) {
			if (heightRatio > widthRatio) {
				bmpFactoryOptions.inSampleSize = heightRatio;
			} else {
				bmpFactoryOptions.inSampleSize = widthRatio;
			}

		}

		bmpFactoryOptions.inJustDecodeBounds = false;
		bmp = BitmapFactory.decodeResource(res, resID, bmpFactoryOptions);
		return bmp;
	}

	/**
	 * 从文件解码图片
	 * 
	 * @param path
	 * @param dw
	 * @param dh
	 * @return
	 */
	public static Bitmap decodeBitmap(String path, int dw, int dh) {
		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		bmpFactoryOptions.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(path, bmpFactoryOptions);
		int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
				/ (float) dh);
		int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
				/ (float) dw);

		if (heightRatio > 1 && widthRatio > 1) {
			if (heightRatio > widthRatio) {
				bmpFactoryOptions.inSampleSize = heightRatio;
			} else {
				bmpFactoryOptions.inSampleSize = widthRatio;
			}

		}

		bmpFactoryOptions.inJustDecodeBounds = false;
		bmp = BitmapFactory.decodeFile(path, bmpFactoryOptions);
		return bmp;
	}

	/**
	 * 获取图片大小
	 * 
	 * @param path
	 *            图片路径
	 * @return
	 */
	public static Point getImageSize(String path) {
		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		bmpFactoryOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, bmpFactoryOptions);
		return new Point(bmpFactoryOptions.outWidth,
				bmpFactoryOptions.outHeight);
	}

	public static Bitmap copyBitmap(Bitmap src) {
		Bitmap bmp = Bitmap.createBitmap(src.getWidth(), src.getHeight(),
				Config.RGB_565);
		Canvas canvas = new Canvas(bmp);
		canvas.drawBitmap(src, 0, 0, null);
		return bmp;
	}

	/**
	 * 图片大小
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap resize(Bitmap bitmap, float newWidth, float newHeight) {

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = false;

		int bmpWidth = bitmap.getWidth();
		int bmpHeight = bitmap.getHeight();

		float scaleWidth = newWidth / bmpHeight;// 按固定大小缩放 sWidth 写多大就多大
		float scaleHeight = newHeight / bmpHeight;// 按固定大小缩放 sWidth 写多大就多大

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);// 产生缩放后的Bitmap对象
		Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bmpWidth,
				bmpHeight, matrix, false);

		return resizeBitmap;
	}

	/**
	 * 读取图片的旋转的角度
	 * 
	 * @param path
	 *            图片绝对路径
	 * @return 图片的旋转角度
	 */
	public static int getBitmapDegree(String path) {
		int degree = 0;
		try {
			// 从指定路径下读取图片，并获取其EXIF信息
			ExifInterface exifInterface = new ExifInterface(path);
			// 获取图片的旋转信息
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 将图片按照某个角度进行旋转
	 * 
	 * @param bm
	 *            需要旋转的图片
	 * @param degree
	 *            旋转角度
	 * @return 旋转后的图片
	 */
	public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
		Bitmap returnBm = null;

		// 根据旋转角度，生成旋转矩阵
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);
		try {
			// 将原始图片按照旋转矩阵进行旋转，并得到新的图片
			returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
					bm.getHeight(), matrix, true);
		} catch (OutOfMemoryError e) {
		}
		if (returnBm == null) {
			returnBm = bm;
		}
		if (bm != returnBm) {
			bm.recycle();
		}
		return returnBm;
	}

	/**
	 * 压缩图片，大小为length以内，单位kb
	 * @param context
	 * @param srcPath
	 * @param length
	 */
	public static void compress(Activity context,String srcPath,int length) {
		
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);

		float hh = dm.heightPixels;
		float ww = dm.widthPixels;
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, opts);
		opts.inJustDecodeBounds = false;
		int w = opts.outWidth;
		int h = opts.outHeight;
		int size = 0;
		if (w <= ww && h <= hh) {
			size = 1;
		} else {
			double scale = w >= h ? w / ww : h / hh;
			double log = Math.log(scale) / Math.log(2);
			double logCeil = Math.ceil(log);
			size = (int) Math.pow(2, logCeil);
		}
		opts.inSampleSize = size;
		bitmap = BitmapFactory.decodeFile(srcPath, opts);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int quality = 100;
		bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
//		System.out.println(baos.toByteArray().length/1024);
		while (baos.toByteArray().length > length * 1024) {
			baos.reset();
			bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
			quality -= 1;
//			System.out.println("quality  :" + quality);
//			System.out.println(baos.toByteArray().length/1024);
		}
		try {
			baos.writeTo(new FileOutputStream(
					srcPath));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				baos.flush();
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 给图片添加水印  
	 * @param src
	 * @param str
	 * @return
	 */
    public static Bitmap waterMark(Bitmap src, String water, Context context,int colorId) {  
    	Bitmap tarBitmap = src.copy(Config.ARGB_8888, true);
    	int w = tarBitmap.getWidth();
    	int h = tarBitmap.getHeight();
    	Canvas canvas = new Canvas(tarBitmap);
    	//启用抗锯齿和使用设备的文本字距          
    	Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);//字体的相关设置          
    	textPaint.setTextSize(100.0f);//字体大小           
    	textPaint.setTypeface(Typeface.DEFAULT_BOLD);          
    	textPaint.setColor(Color.YELLOW);          
    	textPaint.setShadowLayer(3f, 1, 1,context.getResources().getColor(colorId));
    	//图片上添加水印的位置，这里设置的是中下部3/4处          
//    	canvas.drawText(water, w/2-50, (float) (h*0.75), textPaint);
    	canvas.drawText(water, 100, h - 100, textPaint);
    	canvas.save(Canvas.ALL_SAVE_FLAG);           
    	canvas.restore();            
    	return tarBitmap;      
    } 

}
