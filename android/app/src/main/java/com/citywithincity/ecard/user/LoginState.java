package com.citywithincity.ecard.user;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.ImageView;

import com.citywithincity.auto.Observer;
import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.ECardJsonManager;
import com.citywithincity.ecard.models.vos.ECardUserInfo;

@Observer
public class LoginState implements IState{
	
	private ImageView headImg;
	private ImageView bgView;
	private ECardUserInfo userInfo;
	private Context mContext;
	
	public LoginState(Context context) {
		mContext = context;
	}

	@Override
	public void handle(ImageView imageView, ImageView bg) {
		userInfo = ECardJsonManager.getInstance().getUserInfo();
		headImg = imageView;
		bgView = bg;
		
		setHeadView(userInfo, headImg, mContext);
		setBgView(userInfo, bgView, mContext);
	}
	
	public static void setHeadView(ECardUserInfo userInfo, ImageView headView, Context context) {
		if (!TextUtils.isEmpty(userInfo.getHead())) {
			ECardJsonManager.getInstance().setImageSrc(userInfo.getHead(), headView);
		} else {
			Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.shouy_bg);
			headView.setImageBitmap(bitmap);
		}
	}
	
	public static void setBgView(ECardUserInfo userInfo,ImageView bgView, Context context) {
		if (!TextUtils.isEmpty(userInfo.getBg())) {
			ECardJsonManager.getInstance().setImageSrc(userInfo.getBg(), bgView);
		} else {
			Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.shouy_bg);
			bgView.setImageBitmap(bitmap);
		}
	}
	
//	//头像
//	@NotificationMethod(UserModel.HEAD_IMG)
//	public void onLoadHeadImageSuccess(String result) {
//		userInfo.head = JsonUtil.getImageUrl(result);
//		saveUserInfo(userInfo);
//		JsonTaskManager.getInstance().setImageSrc(userInfo.head, headImg);
//	}
//	
//	//壁纸
//	@NotificationMethod(UserModel.HEAD_BG)
//	public void onLoadHeadBgSuccess(String result) {
//		userInfo.bg = JsonUtil.getImageUrl(result);
//		saveUserInfo(userInfo);
//		JsonTaskManager.getInstance().setImageSrc(userInfo.head, bgView);
//	}
//	
//	/**
//	 * 保存到缓存
//	 */
//	private void saveUserInfo(UserInfo userInfo) {
//		LocalData.createDefault(LocalDataMode.MODE_WRITE)
//				.putObject(JsonTaskManager.SUB_USER_INFO, userInfo).destroy();
//	}

}
