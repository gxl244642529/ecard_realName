package com.citywithincity.imageeditor.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;

public class BitmapUtils {
	/**
	 * 滤镜
	 * @param src
	 * @return
	 */
	public static Bitmap createFilterBitmap(Bitmap src,ColorMatrix cMatrix){
		Bitmap bmp = Bitmap.createBitmap(src.getWidth(),
				src.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bmp);
		Paint paint = new Paint();
		paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));
		canvas.drawBitmap(src, 0, 0, paint);
		return bmp;
	}
	

	/**
	 * 饱和度
	 * @param src
	 * @param sat
	 * @return
	 */
	public static Bitmap createSat(Bitmap src,float sat){
		ColorMatrix cMatrix = new ColorMatrix();
		cMatrix.setSaturation(sat);
		return createFilterBitmap(src, cMatrix);
	}
	
	/**
	 * 明亮度
	 */
	public static Bitmap createBright(Bitmap src,int brightness){
		ColorMatrix cMatrix = new ColorMatrix();
		cMatrix.set(new float[] { 1, 0, 0, 0, brightness, 0, 1, 0, 0,
				brightness,// 改变亮度
				0, 0, 1, 0, brightness, 0, 0, 0, 1, 0 });
		return createFilterBitmap(src, cMatrix);
	}
	
	/**
	 * 对比度
	 */
	public static Bitmap createContrast(Bitmap src,float contrast) {
		ColorMatrix cMatrix = new ColorMatrix();
		cMatrix.set(new float[] { contrast, 0, 0, 0, 0, 0, contrast, 0,
				0, 0,// 改变对比度
				0, 0, contrast, 0, 0, 0, 0, 0, 1, 0 });
		return createFilterBitmap(src, cMatrix);
	}
	
	/**
	 * 创建小图
	 * @param src
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap smallImage(Bitmap src,int width,int height){
		Matrix matrix = new Matrix();
		float sx = (float)width / src.getWidth();
		float sy = (float)height / src.getHeight();
		matrix.postScale(sx, sy);
		return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, false);
	}
}
