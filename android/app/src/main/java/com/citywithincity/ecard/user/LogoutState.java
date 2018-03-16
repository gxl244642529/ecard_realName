package com.citywithincity.ecard.user;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.ImageView;

import com.citywithincity.ecard.R;
import com.citywithincity.ecard.models.ECardJsonManager;
import com.citywithincity.ecard.models.vos.EcardUserConfig;
import com.citywithincity.ecard.utils.SystemUtil;
import com.citywithincity.utils.FileDatabase;

import java.io.IOException;

public class LogoutState implements IState{

	private ImageView headImg;
	private ImageView bgView;
	private EcardUserConfig config;
	private Context mContext;
	
	public LogoutState(Context context) {
		mContext = context;
	}
	
	@Override
	public void handle(ImageView imageView, ImageView bg) {
		try {
			config = FileDatabase.loadFromFile(SystemUtil.getTravelConfigFile(mContext), EcardUserConfig.class);

		} catch (IOException e) {



		}


        if(config == null) {
            config = new EcardUserConfig();
        }
        headImg = imageView;
        bgView = bg;

        setBgView(config, bgView, mContext);
        setHeadView(config, headImg,mContext);

	}
	
	public static void setHeadView(EcardUserConfig config, ImageView headView,Context context) {
		if (!TextUtils.isEmpty(config.headImg)) {
			ECardJsonManager.getInstance().setImageSrc(config.headImg, headView);
		} else {
			Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.shouy_bg);
			headView.setImageBitmap(bitmap);
		}
	}
	
	public static void setBgView(EcardUserConfig config, ImageView bgView,Context context) {
		if (!TextUtils.isEmpty(config.bg)) {
			ECardJsonManager.getInstance().setImageSrc(config.bg, bgView);
		} else {
			Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.shouy_bg);
			bgView.setImageBitmap(bitmap);
		}
	}

}
