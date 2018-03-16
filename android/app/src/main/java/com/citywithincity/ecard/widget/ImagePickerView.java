package com.citywithincity.ecard.widget;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.util.AttributeSet;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.citywithincity.ecard.R;
import com.citywithincity.imageeditor.EditorUtil;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.ExternalUtil;
import com.citywithincity.utils.ImageUtil;
import com.citywithincity.utils.IoUtil;
import com.citywithincity.utils.SDCardUtil;
import com.citywithincity.widget.ActionSheet;
import com.citywithincity.widget.ActionSheet.OnActionSheetListener;
import com.damai.core.IdManager;
import com.damai.error.UnexpectedException;
import com.damai.helper.ActivityResult;
import com.damai.helper.ActivityResultContainer;
import com.damai.helper.IValue;
import com.damai.util.AlbumUtil;
import com.damai.util.CameraUtil;
import com.damai.widget.FormElement;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ImagePickerView extends LinearLayout implements OnActionSheetListener, ActivityResult,FormElement,IValue{
	
	private ImageView imageView;
	
	/**
	 * 是否支持从相册选择
	 */
	private boolean enableAlbum;
	
	private String hint;
	
	/*
	 * 质量
	 */
	private int quality;
	private int maxWidth;
	private int maxHeight;
	private boolean edit;

	public ImagePickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnClickListener(listener);
		
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable._image_picker);
		
		hint=a.getString(R.styleable._image_picker_image_picker_hint);
		
		quality = a.getInt(R.styleable._image_picker_image_picker_quality, 100);
		maxWidth = a.getInt(R.styleable._image_picker_image_picker_width, 900);
		maxHeight = a.getInt(R.styleable._image_picker_image_picker_height, 900);
		edit = a.getBoolean(R.styleable._image_picker_image_picker_edit,false);
		a.recycle();
	}
	
	@Override
	protected void onFinishInflate() {
		if(isInEditMode())return;
		imageView = (ImageView) findViewById(R.id._image_view);
		if(imageView==null){
			throw new RuntimeException("Cannot find imageView with id R.id._image_view!");
		}
		
	}
	
	
	private static final String[] ALBUM ={"选择图片","拍照"};
	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(enableAlbum){
				new ActionSheet((Activity) getContext(), ALBUM).setTitle("选择图片").setOnActionSheetListener(ImagePickerView.this)
				.show();
			}else{
				//直接选择
				
				captureImage();
			}
		}
	};
	
	private ActivityResultContainer getActivity(){
		return (ActivityResultContainer)getContext();
	}
	
	

	private File temp;
	
	private void captureImage(){
        try {
            temp= SDCardUtil.getTempImageFile(getContext());
        } catch (IOException e) {
            Alert.alert(getContext(),"请确认sd卡权限是否打开");
            return;
        }
        Acp.getInstance(getContext()).request(new AcpOptions.Builder()
                .setPermissions("android.permission.CAMERA").build(), new AcpListener() {
            @Override
            public void onGranted() {
                getActivity().startActivityForResult(ImagePickerView.this,
                        CameraUtil.getCaptureIntent(getContext(),temp),
						ExternalUtil.REQUEST_CAPTURE_IMAGE);
            }

            @Override
            public void onDenied(List<String> permissions) {
                Alert.alert(getContext(),"请打开摄像头权限");
            }
        });
	}

	
	private void selectImage(){
		getActivity().startActivityForResult(this,
				AlbumUtil.getSelectIntent(),
				ExternalUtil.REQUEST_SELECT_IMAGE);
	}
	
	@Override
	public void onActionSheet(int index) {
		if(index==0){
			selectImage();
		}else{
			captureImage();
		}
	}

	//这里bitmap
	private Bitmap cachedBitmap;
	
	private void setImage(File file){
		if(edit){
			EditorUtil.editImage((Activity)getContext(),
					this, file.getAbsolutePath(),
					EditorUtil.REQUEST_EDIT_IMAGE,
					maxWidth,
					maxHeight,
					null, true);
		}else{
			showFile(file.getAbsolutePath());
		}
	}

    private void showFile(String file){
        Bitmap bitmap = ImageUtil.decodeBitmap(file,  maxWidth, maxHeight);
        imageView.setImageBitmap(bitmap);
        cachedBitmap = bitmap;
    }
	
	@Override
	public void onActivityResult(Intent intent, int resultCode, int requestCode) {
		if(resultCode==Activity.RESULT_OK){
			if(requestCode==ExternalUtil.REQUEST_CAPTURE_IMAGE){
                File file = temp;
                if(file==null || !file.exists()){
                    Alert.alert(getContext(),"请确认是否打开sd卡权限");
                    return;
                }
                setImage(file);

			}else if(requestCode==ExternalUtil.REQUEST_SELECT_IMAGE){
				try {
					String path = AlbumUtil.getPath(getContext(), intent);
					setImage(new File(path));
				} catch (UnexpectedException e) {
					Alert.alert(getContext(), e.getMessage());
				}
			}else if(requestCode == EditorUtil.REQUEST_EDIT_IMAGE){
                String path = EditorUtil.getPath(intent);
                showFile(path);
            }
		}
	}


	@Override
	public String validate(Map<String, Object> data) {
		
		if(  cachedBitmap==null && base64==null){
			return hint==null ? "请选择图片" : hint;
		}
		
		data.put(IdManager.idToString(getContext(), getId()), getValue());
		return null;
		
	}
	
	
	
	
	private String base64;

	@Override
	public void setValue(Object value) {
		if(value==null){
			return;
		}
		base64 = (String)value;
		byte[] bytes = Base64.decode(base64, 0);
		// 加载
		Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		imageView.setImageBitmap(bitmap);
	}

	
	public Object getValue() {
		
		//这里,如果有bitmap，则使用这个bitmap，否则使用base64
		if(cachedBitmap!=null){
			ByteArrayOutputStream outputStream = null;
			try{
				outputStream= new ByteArrayOutputStream();
                cachedBitmap.compress(CompressFormat.JPEG, quality, outputStream);
				return outputStream.toByteArray();	
			}finally{
				IoUtil.close(outputStream);
			}
			
		}
		return base64;
	}
	

}
