package com.damai.webview.urlparser;

import java.io.Serializable;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;

import com.citywithincity.utils.MemoryUtil;
import com.damai.core.EntityUtil;
import com.damai.dmlib.LibBuildConfig;
import com.damai.helper.DataHolder;

public class OpenActivityUrlParser implements WebViewUrlParser {

	@SuppressWarnings("unchecked")
	@Override
	public void parseUrl(Context context,String command, String[] args) {
		String activityName = args[2];
		
		try{
			String packageName = context.getPackageName();
			String activityClassName = packageName+ ".activities.adv." + activityName + "Activity";
			Class<? extends Activity> clazz = (Class<? extends Activity>) Class.forName( activityClassName);
			//参数
			Intent intent = new Intent(context,clazz);
			if(args.length>3){
				String argString = args[3];
				JSONObject jsonObject = new JSONObject(new String(Base64.decode(argString,Base64.NO_WRAP),"UTF-8"));
				if(jsonObject.has("className")){
					Class<?> voClass = Class.forName(packageName + ".adv.vos." + jsonObject.getString("className"));
					Serializable value = (Serializable) EntityUtil.jsonToObject(jsonObject, voClass);
					intent.putExtra("data", value);
					DataHolder.set(clazz,value);
				}else{
					Bundle bundle = MemoryUtil.jsonToBundle(jsonObject);
					intent.putExtras(bundle);
				}
			}
			
			context.startActivity(intent);
			((Activity)context).finish();
		}catch(Exception e){
			if(LibBuildConfig.DEBUG){
				System.err.println("打开Activity失败:" + activityName + " 参数:" + (args.length>3?args[3]:"null"));
			}
		}
	}

}
