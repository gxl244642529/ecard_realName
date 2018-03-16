package com.citywithincity.ecard.ui.activity.exam;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.utils.SystemUtil;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.ExternalUtil;
import com.citywithincity.utils.FileUtil;
import com.citywithincity.utils.ImageUtil;
import com.citywithincity.widget.ActionSheet;
import com.citywithincity.widget.ActionSheet.OnActionSheetListener;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ExamUploadImageView extends LinearLayout {

	public static final int CAPTURE_IMAGE = 1;
	public static final int SELECT_IMAGE = 2;
	public static final int BOTH = 3;

	/**
	 * 图片选择方式
	 */
	private int method = BOTH;

	private File imageFile;
	private ImageView imageView;
	private Bitmap bitmap;
	private static int _currentId = 0;
	private int defaultPicSaveQuality = 10;
	private boolean isAddWaterMark = false;
	private String waterMark;
	private Context context;
	private int waterMarkColorId;

	public ExamUploadImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setOnClickListener(listener);
		this.context = context;

	}

	@Override
	protected void onFinishInflate() {
		if (isInEditMode()) {
			return;
		}
		imageView = new ImageView(getContext());
		ViewGroup viewGroup = (ViewGroup) findViewById(R.id._container);
		viewGroup.addView(imageView, new FrameLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
		imageView.setImageResource(R.drawable.ic_insurance_page_bg);
		super.onFinishInflate();
	}
	
	public void setImageResource(int bgResId) {
		imageView.setImageResource(bgResId);
	}

	public void setPicSaveQuality(int quality) {
		defaultPicSaveQuality = quality;
	}

	public void setBase64(String pic) {
		byte[] bytes = Base64.decode(pic, 0);
		// 加载
		bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		if (bitmap != null) {
			setBitmap(bitmap);
		}
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try{

				createFile();
			}catch (IOException e){
				Alert.alert(getContext(),"文件创建失败");
				return;
			}

			final OnActionSheetListener listener = new OnActionSheetListener() {

				@Override
				public void onActionSheet(int index) {

					switch (method) {
					case CAPTURE_IMAGE:


						ExternalUtil.captureImage((Activity) getContext(),
								imageFile, ExternalUtil.REQUEST_CAPTURE_IMAGE);
						break;
					case SELECT_IMAGE:
						Acp.getInstance(getContext()).request(new AcpOptions.Builder()
								.setPermissions("android.permission.CAMERA").build(), new AcpListener() {
							@Override
							public void onGranted() {
								ExternalUtil.selectImage((Activity) getContext(),
										ExternalUtil.REQUEST_SELECT_IMAGE);
							}

							@Override
							public void onDenied(List<String> permissions) {
								Alert.alert(getContext(),"请打开摄像头权限");
							}
						});

						break;
					case BOTH:
						if (index == 0) {
							ExternalUtil.selectImage((Activity) getContext(),
									ExternalUtil.REQUEST_SELECT_IMAGE);
						} else {
							ExternalUtil.captureImage((Activity) getContext(),
									imageFile,
									ExternalUtil.REQUEST_CAPTURE_IMAGE);
						}
						break;

					default:
						break;
					}
					// 掉政委
					_currentId = getId();
				}
			};

			switch (method) {
			case CAPTURE_IMAGE:
				new ActionSheet((Activity) getContext(), new String[] { "拍照" })
						.setTitle("选择图片").setOnActionSheetListener(listener)
						.show();
				break;
			case SELECT_IMAGE:
				new ActionSheet((Activity) getContext(),
						new String[] { "图库选择照片" }).setTitle("选择图片")
						.setOnActionSheetListener(listener).show();
				break;
			case BOTH:
				new ActionSheet((Activity) getContext(), new String[] {
						"图库选择照片", "拍照" }).setTitle("选择图片")
						.setOnActionSheetListener(listener).show();
				break;

			default:
				break;
			}

		}
	};

	private void createFile() throws IOException {
		if (imageFile == null)
			imageFile = new File(SystemUtil.getFileDir(getContext(), "exam"),
					String.valueOf(System.currentTimeMillis()));
	}

	private void setBitmap(Bitmap bitmap) {
		if (imageView == null) {
			imageView = new ImageView(getContext());
			ViewGroup viewGroup = (ViewGroup) findViewById(R.id._container);
			viewGroup.addView(imageView, new FrameLayout.LayoutParams(
					android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
			imageView.setImageResource(R.drawable.diy_pages);
		}
		imageView.setImageBitmap(bitmap);
	}

	public void parseResult(int requestCode, Intent data) {
		if (_currentId != getId())
			return;
		if (requestCode == ExternalUtil.REQUEST_CAPTURE_IMAGE) {
			bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
			bitmap = ImageUtil.rotateBitmapByDegree(bitmap,
					ImageUtil.getBitmapDegree(imageFile.getAbsolutePath()));
		} else {
			Uri uri = data.getData();// 抽取数据
			ContentResolver cr = getContext().getContentResolver();
			InputStream is;
			try {
				is = cr.openInputStream(uri);
				bitmap = BitmapFactory.decodeStream(is);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}
		if (bitmap != null) {
			if (isAddWaterMark) {
				bitmap = ImageUtil.waterMark(bitmap, waterMark,context,waterMarkColorId);
			}
			setBitmap(bitmap);
		} else {
			Alert.showShortToast("加载图片错误，请重新选择图片");
		}
	}
	
	public File getFile(int dw,int dh) throws IOException {
		createFile();
		if (ImageUtil.saveAs(bitmap, imageFile, defaultPicSaveQuality)) {
			bitmap = ImageUtil.decodeBitmap(imageFile.getAbsolutePath(), dw, dh);
			if (ImageUtil.saveAs(bitmap, imageFile, defaultPicSaveQuality)) {
				return imageFile;
			}
		}
		return null;
	}

	/**
	 * base64
	 * @return
	 */
	public String getBase64() throws IOException {
		createFile();
		if (ImageUtil.saveAs(bitmap, imageFile, defaultPicSaveQuality)) {
			return FileUtil.toBase64(imageFile);
		}
		return null;
	}

	/**
	 * 指定宽高的base64
	 * @param dw
	 * @param dh
	 * @return
	 */
	public String getBase64(int dw, int dh) throws IOException {
		createFile();
		if (ImageUtil.saveAs(bitmap, imageFile, defaultPicSaveQuality)) {
			bitmap = ImageUtil
					.decodeBitmap(imageFile.getAbsolutePath(), dw, dh);
			if (ImageUtil.saveAs(bitmap, imageFile, defaultPicSaveQuality)) {
				return FileUtil.toBase64(imageFile);
			}
		}
		return null;
	}
	
	/**
	 * 压缩图片，大小为length以内，单位kb
	 * @param context
	 * @param length
	 * @return
	 */
	public File compressToFile(Activity context,int length) throws IOException {
		createFile();
		if (ImageUtil.saveAs(bitmap, imageFile, defaultPicSaveQuality)) {
			
			ImageUtil.compress(context, imageFile.getAbsolutePath(),length);
			
				return imageFile;
		}
		return null;
	}
	
	public boolean hasImage() {
		return (imageFile != null && imageFile.length() != 0) || bitmap != null;
	}

	/**
	 * 图片
	 * 
	 * @param method
	 */
	public void setMethod(int method) {
		this.method = method;
	}
	
	public void setImageScaleType(ScaleType scaleType) {
		imageView.setScaleType(scaleType);
	}
	
	public void addWaterMark(String waterMark, int waterMarkColorId) {
		isAddWaterMark = true;
		this.waterMark = waterMark;
		this.waterMarkColorId = waterMarkColorId;
	}

}
