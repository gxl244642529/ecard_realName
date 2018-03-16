package com.citywithincity.imageeditor;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.citywithincity.ecard.R;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.ThreadUtil;
import com.damai.auto.LifeManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

abstract class AbsEditorFragment extends Fragment {
	
	public interface IFragmentListener{
		void onFragmentDestroy(Fragment fragment);
	}
	
	protected ImageView thisImageView;
	protected Point imageSize;
	protected Bitmap workingBitmap;
	protected ImageEditorModel model;
	private int index;
	private IFragmentListener onDestroyListener;
	
	

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		imageSize = model.getImageSize();
		workingBitmap = model.getWorkingBitmap();
		thisImageView = (ImageView) view.findViewById(R.id._image_view);
		thisImageView.setImageBitmap(workingBitmap);
		view.findViewById(R.id._id_ok).setOnClickListener(onOkClickListener);
		view.findViewById(R.id._id_cancel).setOnClickListener(onCancelListener);
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setModel(ImageEditorModel model) {
		this.model = model;
	}

	@Override
	public void onDestroy() {
		model = null;
		Alert.cancelWait();
		super.onDestroy();
	}

	public void setTitle(String title) {
		((TextView) getView().findViewById(R.id._title_text)).setText(title);
	}

	OnClickListener onOkClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			processImage();
		}
	};

	OnClickListener onCancelListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			onCancelEditImage();
		}
	};

	protected abstract Bitmap executeImage(Bitmap bm) throws OutOfMemoryError;

	/**
	 * 处理图片
	 * 
	 * @param bitmap
	 */
	protected File saveImage(Bitmap bitmap) throws FileNotFoundException {
		return model.saveImage(bitmap);
	}

	protected void onImageComplete(File file) {
		Alert.cancelWait();
		// 图片处理完毕
		onDestroyListener.onFragmentDestroy(this);
		// 通知上一级
		model.onImageComplete(index, file);
	}

	protected void onCancelEditImage() {
		
		onDestroyListener.onFragmentDestroy(this);
	}

	protected void onError(String errorMessage) {
		Alert.cancelWait();
		Alert.alert(getActivity(), "温馨提示", errorMessage);
	}

	protected void processImage() {
		Alert.wait(getActivity(), "正在处理图片...");
		ThreadUtil.run(new Runnable() {
			@Override
			public void run() {
				try {
					// 解析出原理啊图片
					Bitmap bm = model.decodeOrgImage();
					final Bitmap bitmap = executeImage(bm);
					if (bitmap != null) {
						try{
							final File file = saveImage(bitmap);
							ThreadUtil.post(new Runnable() {
								@Override
								public void run() {
									onImageComplete(file);
								}
							});
						}catch (IOException e){
							ThreadUtil.post(new Runnable() {
								@Override
								public void run() {
									Alert.alert(LifeManager.getActivity(),"文件保存失败");
								}
							});
						}

					} else {
						// 没有变化,直接退出
						ThreadUtil.post(new Runnable() {

							@Override
							public void run() {
								onCancelEditImage();
							}
						});

					}
				} catch (OutOfMemoryError exception) {
					ThreadUtil.post(new Runnable() {

						@Override
						public void run() {
							onError("内存不足");
						}
					});

				} finally {
					Alert.cancelWait();
				}
			}
		});

	}


	public IFragmentListener getOnDestroyListener() {
		return onDestroyListener;
	}

	public void setOnDestroyListener(IFragmentListener onDestroyListener) {
		this.onDestroyListener = onDestroyListener;
	}
}
