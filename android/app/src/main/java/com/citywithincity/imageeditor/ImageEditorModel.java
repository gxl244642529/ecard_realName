package com.citywithincity.imageeditor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;

import com.citywithincity.interfaces.IDestroyable;
import com.citywithincity.pattern.memo.Memo;
import com.citywithincity.pattern.memo.Memo.IMemo;
import com.citywithincity.utils.FileUtil;
import com.citywithincity.utils.ImageUtil;
import com.damai.util.ViewUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

class ImageEditorModel implements IMemo<File>,IDestroyable {
	
	public interface IImageEditorListener{
		void onEditImageComplete(int index,File file);
	}
	
	private File currentPath;
	/**
	 * 正在编辑的图片文件
	 */
	File editFile;

	/**
	 * 计算出来的图片压缩配置
	 */
	Options opts;

	/**
	 * 工作的bimtp
	 */
	Bitmap workingBitmap;


	private Memo<File> memoModel;
	/**
	 * 图片实际大小（实际上是能处理的最小大小,小于等于实际图片大小,大小为原图片大小 / (n*n) ) n为大于等于1的整数
	 */
	Point imageSize;

	
	private IImageEditorListener listener;
	
	public ImageEditorModel(Context context,IImageEditorListener listener) {
		// 当前工作目录
		try{
			currentPath = EditorUtil.getEditorWorkPath(context);
		}catch (IOException e){
			currentPath = new File("/");
		}


		memoModel = new Memo<File>(this);
		this.listener = listener;
	}

	/**
	 * 获取图片大小
	 * 
	 * @return
	 */
	public Point getImageSize() {
		return imageSize;
	}

	/**
	 * 设置要编辑的文件
	 * 
	 * @param editFile
	 * @param addToRedoList 是否要添加到重做列表中
	 */
	public void setEditFile(File editFile,boolean addToRedoList) {
		this.editFile = editFile;
		/**
		 * 所有参数全部变化
		 */
		String path = editFile.getAbsolutePath();
		calcOpts(path);
		/**
		 * 回收现有图片
		 */
		/*
		if (workingBitmap != null) {
			if (useCount <= 0) {
				try{
					workingBitmap.recycle();
					System.gc();
				}catch(Throwable ex){
					
				}
			}
		}*/
		workingBitmap = ImageUtil.decodeBitmap(path, ViewUtil.screenWidth,
				ViewUtil.screenHeight);
		useCount = 0;
		
		if(addToRedoList){
			memoModel.add(editFile);
		}
	}
	
	private int useCount;

	/**
	 * 获取工作bitmap
	 * 
	 * @return
	 */
	public Bitmap getWorkingBitmap() {
		useCount++;
		return workingBitmap;
	}

	private void calcOpts(String path) {
		// 取出比卡大的最小大小
		BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
		bmpFactoryOptions.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, bmpFactoryOptions);
		int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight
				/ (float) EditorUtil.MIN_CARD_HEIGHT);
		int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth
				/ (float) EditorUtil.MIN_CARD_WIDTH);
		int ratio = 1;
		if (widthRatio > 1 || heightRatio > 1) {
			ratio = Math.max(widthRatio, heightRatio);
			while (bmpFactoryOptions.outWidth / ratio < EditorUtil.MIN_CARD_WIDTH) {
				if (ratio > 1) {
					ratio--;
				} else {
					break;
				}
			}
			while (bmpFactoryOptions.outHeight / ratio < EditorUtil.MIN_CARD_HEIGHT) {
				if (ratio > 1) {
					ratio--;
				} else {
					break;
				}
			}
		}
		ratio = Math.max(ratio, 1);
		bmpFactoryOptions.inSampleSize = ratio;
		/**
		 * 计算能处理的图片大小,实际图片decode出来以后就这么大
		 */
		imageSize = new Point(bmpFactoryOptions.outWidth / ratio,
				bmpFactoryOptions.outHeight / ratio);
		opts = bmpFactoryOptions;
		opts.inJustDecodeBounds = false;
	}


	/**
	 * //解析出原图片
	 * 
	 * @return
	 */
	public Bitmap decodeOrgImage() {

		return BitmapFactory.decodeFile(editFile.getAbsolutePath(), opts);
	}



	/**
	 * 图片处理完毕
	 * 
	 * @param bitmap
	 */
	public void onImageComplete(int index,File file) {
		listener.onEditImageComplete(index,file);
	}

	@Override
	public void releaseMemoData(File data) {
		if (data.exists())
			data.delete();
	}
	
	/**
	 * 
	 * @param bitmap
	 */
	public File saveImage(Bitmap bitmap) throws FileNotFoundException {
		File file = new File(currentPath, String.valueOf(System.currentTimeMillis()) + ".jpg");
		ImageUtil.saveAs(bitmap, file);
		bitmap.recycle();
		setEditFile(file, true);
		return file;
	}
	
	
	
	public boolean onIntent(Intent intent){
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			String path = bundle.getString("data");
			if (!TextUtils.isEmpty(path)) {
				File file = new File(path);
				if (file.exists()) {
					// 拷贝文件
					File currentFile = new File(currentPath,String.valueOf(System.currentTimeMillis()) + ".png");
					FileUtil.copyFile(file, currentFile);
					setEditFile(currentFile,false);
					//如果不需要剪裁，设置已经剪裁过了
					if(!bundle.getBoolean(EditorUtil.NEEDS_CUT_FIRST)){
						setCutted();
					}
					return true;
				}
			}
		}
		return false;
	}

	public boolean canUndo() {
		return memoModel.canUndo();
	}

	public boolean canRedo() {
		return memoModel.canRedo();
	}

	public boolean undo() {
		if (memoModel.canUndo()) {
			setEditFile(memoModel.undo(),false);
			return true;
		}
		return false;
	}
	
	public boolean redo() {
		if (memoModel.canRedo()) {
			setEditFile(memoModel.redo(),false);
			return true;
		}
		return false;
	}

	public File getPrevious() {
		return memoModel.getPrevious();
	}

	@Override
	public void destroy() {
		
	}

	public File getEditFile() {
		return editFile;
	}
	
	private boolean isCutted;

	public void setCutted() {
		isCutted = true;
	}
	
	public boolean isCutted(){
		return isCutted;
	}
}
