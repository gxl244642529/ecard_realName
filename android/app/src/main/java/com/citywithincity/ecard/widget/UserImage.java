package com.citywithincity.ecard.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alan.roundimageview.RoundedImageView;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.UserModel;
import com.citywithincity.ecard.models.vos.ECardUserInfo;
import com.citywithincity.ecard.utils.SelectImage;
import com.citywithincity.ecard.utils.SelectImage.SelectImageListener;
import com.citywithincity.imageeditor.EditorUtil;
import com.citywithincity.interfaces.ILocalData;
import com.citywithincity.interfaces.IViewContainer;
import com.citywithincity.models.LocalData;
import com.damai.core.ApiJob;
import com.damai.core.DMAccount;
import com.damai.core.DMLib;
import com.damai.core.ILife;
import com.damai.core.OnApiSuccessListener;
import com.damai.helper.ActivityResult;
import com.damai.util.StrKit;

import java.io.File;

public class UserImage extends RelativeLayout implements OnClickListener,
		SelectImageListener, ActivityResult, ILife, OnApiSuccessListener {

	public static final int SELECT_PICTURE = 20;
	public static final int CAPTURE_PICTURE = 21;
	public static final int CROP_IMAGE = 22;

	public static final String HEAD_IMAGE = "head_image";
	public static final String BACK_IAMGE = "back_image";

	private RoundedImageView headImage;
	private ImageView backImage;
	private Context mContext;

	private SelectImage selectImage;

	private UserModel userModel;

	public UserImage(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (isInEditMode()) {
			return;
		}
		mContext = context;
		selectImage = new SelectImage((Activity) context, this);
		userModel = new UserModel();
		((IViewContainer) context).addLife(this);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (isInEditMode()) {
			return;
		}
		headImage = (RoundedImageView) findViewById(R.id.head_view);
		backImage = (ImageView) findViewById(R.id.head_img);
		backImage.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		if (backImage == v) {
			selectImage.selectImage();
		}
	}

	@Override
	public void onSelectData(File file) {
		EditorUtil.editImage((Activity) mContext, this, file.getAbsolutePath(),
				EditorUtil.REQUEST_EDIT_IMAGE, backImage.getMeasuredWidth(),
				backImage.getMeasuredHeight(), null, true);
	}

	@Override
	public void onActivityResult(Intent data, int resultCode, int requestCode) {
		if (resultCode == Activity.RESULT_OK) {

			if (requestCode == EditorUtil.REQUEST_EDIT_IMAGE) {
				String path = data.getExtras().getString("data");
				Bitmap bitmap = BitmapFactory.decodeFile(path);
				backImage.setImageBitmap(bitmap);
				if(DMAccount.isLogin()){
					userModel.setBg(new File(path),this);
				}else {
					LocalData.write().putString(UserModel.bg, path).save();
				}
			}

		}
	}

	


	@Override
	public void onResume(IViewContainer container) {
		// 加载背景
		ILocalData localData = LocalData.read();
		if(DMAccount.isLogin()){
			ECardUserInfo account = DMAccount.get();
			
			if( !StrKit.isBlank( account.getHead() )){
				DMLib.getJobManager().loadImage(account.getHead(), headImage);
			}else{
				String head = localData.getString(UserModel.head, null);
				if(head!=null){
					DMLib.getJobManager().loadImage(head, headImage);
				}else{
					headImage.setImageResource(R.drawable.shouy_bg);
				}
			}
			if(!StrKit.isBlank( account.getBg() ) ){
				DMLib.getJobManager().loadImage(account.getBg(), backImage);
			}else{
				
				String bg = localData.getString(UserModel.bg, null);
				if(bg!=null && !new File(bg).exists()){
					DMLib.getJobManager().loadImage(bg, backImage);
				}else{
					backImage.setImageResource(R.drawable.shouy_bg);
				}
			}
		}else{
			
			String head = localData.getString(UserModel.head, null);
			if(head!=null && !new File(head).exists()){
				DMLib.getJobManager().loadImage(head, headImage);
			}else{
				headImage.setImageResource(R.drawable.shouy_bg);
			}
			
			String bg = localData.getString(UserModel.bg, null);
			if(bg!=null && !new File(bg).exists()){
				DMLib.getJobManager().loadImage(bg, backImage);
			}else{
				backImage.setImageResource(R.drawable.shouy_bg);
			}
		}
	}

	@Override
	public void onJobSuccess(ApiJob job) {
		String path = job.getData();
		DMLib.getJobManager().loadImage(path, backImage);
		
	}

	@Override
	public void onPause(IViewContainer container) {
		
	}

	@Override
	public void onNewIntent(Intent intent, IViewContainer container) {
		
	}

	@Override
	public void onDestroy(IViewContainer container) {
		// TODO Auto-generated method stub
		
	}

	

	

}
