package com.citywithincity.ecard.user.activities;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.activities.AddressActivity;
import com.citywithincity.ecard.models.UserModel;
import com.citywithincity.ecard.models.vos.ECardUserInfo;
import com.citywithincity.ecard.react.AccountModule;
import com.citywithincity.ecard.utils.SelectImage;
import com.citywithincity.ecard.utils.SelectImage.SelectImageListener;
import com.citywithincity.imageeditor.EditorUtil;
import com.citywithincity.interfaces.ILocalData;
import com.citywithincity.models.LocalData;
import com.citywithincity.models.http.JsonTaskManager;
import com.citywithincity.utils.ImageUtil;
import com.damai.auto.DMActivity;
import com.damai.core.ApiJob;
import com.damai.core.DMAccount;
import com.damai.core.DMLib;
import com.damai.core.OnApiSuccessListener;
import com.damai.helper.a.Event;
import com.damai.helper.a.Model;
import com.damai.helper.a.Res;
import com.damai.push.Push;

import java.io.File;


public class UserInfoActivity extends DMActivity implements SelectImageListener, OnApiSuccessListener {

	private SelectImage selectImage;
	
	@Model
	private UserModel userModel;
	
	@Res
	private ImageView head_view;

	@Override
	protected void onSetContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_user_info);
		setTitle("个人信息");
		selectImage = new SelectImage(this, this);
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		
		updateImage();
	}
	
	private void updateImage(){
		ECardUserInfo account = DMAccount.get();
		if(account!=null && account.getHead()!=null){
			DMLib.getJobManager().loadImage(account.getHead(), head_view);
		}else{
			ILocalData localData = LocalData.read();
			String head = localData.getString(UserModel.head, null);
			if(head!=null){
				DMLib.getJobManager().loadImage(head, head_view);
			}
		}
	}
	
	
	
	@Event
	public void head_view(){
		//查询头像
		
		
	}
	
	@Event
	public void user_head(){
		
		selectImage.selectImage();
	}
	
	@Event
	public void onAddress(){
		startActivity(new Intent(this,AddressActivity.class));
	}
	
	@Event
	public void update_password(){
		startActivity(new Intent(this,UpdatePasswordActivity.class));
	}
	
	@Event(confirm="确定要退出吗?")
	public void onLogout(){
		DMAccount.logout();
		JsonTaskManager.getInstance().logout();
		Push.onLogout();

		AccountModule.onLogout();

		finish();
	}

	@Override
	public void onSelectData(File data) {
		//选择了头像,去剪裁一下
		EditorUtil.editImage(this,  data.getAbsolutePath(), 1000, 640, 640, null, true);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			if(requestCode == 1000){
				//剪裁完毕
				String image = EditorUtil.getPath(data);
				LocalData.write().putString(image, UserModel.head);
				//展示出来
				Bitmap bitmap = ImageUtil.decodeBitmap(image, head_view.getMeasuredWidth(), head_view.getMeasuredHeight());
				head_view.setImageBitmap(bitmap);
				//保存
				userModel.setHeadImage(new File(image),this);
			}
		}
	}


	@Override
	public void onJobSuccess(ApiJob job) {
		String path = job.getData();
		AccountModule.onChangeHead(path);
		DMLib.getJobManager().loadImage(path, head_view);
	}



}
