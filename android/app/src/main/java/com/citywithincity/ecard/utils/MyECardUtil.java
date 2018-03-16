package com.citywithincity.ecard.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.citywithincity.interfaces.IViewContainer;
import com.damai.auto.LifeManager;
import com.damai.helper.ActivityResult;
import com.damai.helper.ActivityResultContainer;
import com.facebook.quicklog.identifiers.ReactNativeBridge;

public class MyECardUtil {
	
	public static final int SELECT_ECARD = 10;


	public static void selectECard(ActivityResult listener){
		selectECard(listener,null);
	}
	/**
	 * 选择e通卡
	 */
	public static void selectECard(ActivityResult listener,Context context){

		if(context==null){
			context = LifeManager.getCurrent().getActivity();
		}

		if(listener instanceof IViewContainer){
			context = ((IViewContainer)listener).getActivity();
		}else if(listener instanceof  View){
			context = ((View)listener).getContext();
		}
		
		String action = context.getPackageName() + ".action.SELECT_ECARD";
		Intent intent = new Intent();
		intent.setAction(action);
		ActivityResultContainer container = (ActivityResultContainer)context;
		container.startActivityForResult(listener,intent, SELECT_ECARD);
	}
	
	
	public static void setResult(Activity context, String cardId){
		Intent intent = new Intent();
		intent.putExtra("cardId", cardId);
		context.setResult(Activity.RESULT_OK, intent);
		context.finish();
	}
	
	public static String getCardId(Intent intent){
		return intent.getStringExtra("cardId");
	}
	
	
	/**
	 * 解析时间
	 * @param strTime
	 * @return
	 */
	public static String parseTime(String strTime) {
		StringBuilder sb = new StringBuilder();
		sb.append(strTime.substring(0, 4));
		sb.append("年");
		sb.append(strTime.substring(4, 6));
		sb.append("月");
		sb.append(strTime.substring(6, 8));
		sb.append("日 ");
		sb.append(strTime.substring(8, 10));
		sb.append("点");
		sb.append(strTime.substring(10, 12));
		sb.append("分");
		return sb.toString();
	}
}
