package com.citywithincity.imageeditor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.citywithincity.ecard.R;
import com.citywithincity.imageeditor.AbsEditorFragment.IFragmentListener;
import com.citywithincity.imageeditor.ImageEditorModel.IImageEditorListener;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.FragmentUtil;
import com.citywithincity.utils.ImageUtil;
import com.damai.helper.ActivityResult;
import com.damai.helper.ActivityResultContainer;
import com.damai.util.ViewUtil;

import java.io.File;

public class ImageEditorActivity extends FragmentActivity implements
		OnClickListener, OnTouchListener, IImageEditorListener, IFragmentListener {

	private ImageView imageView;

	private View btnUndo;// 撤销
	private View btnRedo;// 重做
	private View contrast;// 对比

	private ImageEditorModel model;

	/**
	 * 将文件传入进来
	 * 
	 * @param context
	 * @param imageFile
	 */
	public static final void edit(Activity context, String imageFile, boolean needsCutFirst, int requestCode) {
		Bundle bundle = new Bundle();
		bundle.putBoolean(EditorUtil.NEEDS_CUT_FIRST, needsCutFirst);
		bundle.putString("data", imageFile);
		Intent intent = new Intent(context,
				ImageEditorActivity.class);
		intent.putExtras(bundle);
		context.startActivityForResult(intent, requestCode);
	}
	
	public static final void edit(ActivityResultContainer context,ActivityResult listener, String imageFile, boolean needsCutFirst, int requestCode) {
		Bundle bundle = new Bundle();
		bundle.putBoolean(EditorUtil.NEEDS_CUT_FIRST, needsCutFirst);
		bundle.putString("data", imageFile);
		Intent intent = new Intent((Context)context,
				ImageEditorActivity.class);
		intent.putExtras(bundle);
		context.startActivityForResult(listener,intent, requestCode);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editor);

		ViewUtil.initParam(this);
		findViewById(R.id._title_left).setOnClickListener(this);
		model = new ImageEditorModel(this,this);
		if (!model.onIntent(getIntent())) {
			Alert.alert(this, "温馨提示", "您必须选择一张图片用于编辑", new DialogListener() {

				@Override
				public void onDialogButton(int id) {
					finish();
				}
			});
			return;
		}

		imageView = (ImageView) findViewById(R.id._image_view);
		btnUndo = findViewById(R.id.undo);
		btnRedo = findViewById(R.id.redo);
		contrast = findViewById(R.id.contrast);

		btnUndo.setEnabled(false);
		btnRedo.setEnabled(false);
		contrast.setEnabled(false);

		btnUndo.setOnClickListener(this);
		btnRedo.setOnClickListener(this);
		contrast.setOnTouchListener(this);
		findViewById(R.id._id_ok).setOnClickListener(this);

		ViewGroup viewGroup = (ViewGroup) findViewById(R.id.editor_tools);
		for (int i = 0; i < viewGroup.getChildCount(); ++i) {
			View child = viewGroup.getChildAt(i);
			child.setTag(i);
			child.setOnClickListener(tooListener);
		}

		enableButtons();
		showImage();
		if(!model.isCutted()){
			onUseTool(0, false);
		}
		
	}
	
	@Override
	public void onBackPressed() {
		if(fragment == null){
			goback();
		}else{
		
			fragment.onCancelEditImage();
		}
		
	}
	
	private void goback(){
		Alert.confirm(this, "温馨提示", "您还没有保存图片，真的要退出吗?", new DialogListener() {
			
			@Override
			public void onDialogButton(int id) {
				if(id== R.id._id_ok){
					finish();
				}
			}
		});
	
	}

	/**
	 * 将图片显示出来
	 */
	protected void tempShowImage(File file) {
		String path = file.getAbsolutePath();
		Bitmap bitmap = ImageUtil.decodeBitmap(path, ViewUtil.screenWidth,
				ViewUtil.screenHeight);
		imageView.setImageBitmap(bitmap);
	}

	private void showCurrentImage() {
		imageView.setImageBitmap(model.getWorkingBitmap());
	}

	@Override
	public void onClick(View v) {
		if (v == btnUndo) {
			if(model.undo()){
				enableButtons();
				showImage();
			}
		} else if (v == btnRedo) {
			if (model.redo()) {
				enableButtons();
				showImage();
			}
		}else if(v.getId() == R.id._id_ok){
			if(EditorUtil.MIN_CARD_WIDTH > 0  && ! model.isCutted()){
				Alert.alert(this, "温馨提示", "您还没有对图片进行剪裁，请使用编辑工具对图片进行剪裁");
				return;
			}
			//返回值
			Intent intent = new Intent();
			intent.putExtra("data", model.getEditFile().getAbsolutePath());
			setResult(RESULT_OK, intent);
			finish();
		}else if(v.getId() == R.id._title_left){
			goback();
		}
	}
	AbsEditorFragment fragment;
	private void onUseTool(int index,boolean animation){
	
		fragment = null;
		switch (index) {
		case 0:
			fragment = new EditorEditFragment();
			break;
		case 1:
			fragment = new EditorFilterFragment();
			break;
		case 2:
			fragment = new EditorAdjustFragment();
			break;
		case 3:
			fragment = new EditorTextFragment();
			break;
		default:
			return;
		}
		fragment.setModel(model);
		fragment.setIndex(index);
		fragment.setOnDestroyListener(this);
		FragmentUtil.addFragment(ImageEditorActivity.this, R.id._container,
				fragment, animation, false);
	}

	private OnClickListener tooListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int index = (Integer) v.getTag();
			onUseTool(index,true);
		}
	};

	/**
	 * 将图片显示出来
	 */
	protected void showImage() {
		imageView.setImageBitmap(model.getWorkingBitmap());
	}


	private void enableButtons() {
		boolean able = model.canUndo();
		contrast.setEnabled(able);
		btnUndo.setEnabled(able);
		btnRedo.setEnabled(model.canRedo());
	}

	

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (model.canUndo()) {
				showCurrentImage();
			}
		} else if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (model.canUndo()) {
				tempShowImage(model.getPrevious());
			}
		}
		return false;
	}
	
	@Override
	protected void onDestroy() {
		model.destroy();
		fragment = null;
		super.onDestroy();
	}

	@Override
	public void onEditImageComplete(int index,File file) {
		enableButtons();
		showImage();
		if(index == 0){
			model.setCutted();
		}
	}

	@Override
	public void onFragmentDestroy(Fragment fragment) {
		FragmentUtil.removeFragment(this, fragment, true);
		this.fragment = null;
	}


}
