package com.citywithincity.ecard;

import android.content.Context;
import android.content.Intent;

import com.citywithincity.ecard.activities.AdvActivity;
import com.citywithincity.ecard.react.AccountModule;
import com.citywithincity.models.http.JsonTaskManager;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.PackageUtil;
import com.damai.auto.DMWebActivity;
import com.damai.auto.LifeManager;
import com.damai.core.DMAccount;
import com.damai.push.Push;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class PushHandler {
	public static final int PUSH_RECHARGE_CONSUM = 2; // 消费
	public static final int PUSH_SEND_GIFT = 3; // 礼物
	public static final int PUSH_USE_COUPON = 4; // 优惠券
	public static final int PUSH_USE_GIFT = 5; // 礼物
	public static final int PUSH_USE_LOTTERY = 6; //
	public static final int PUSH_ADV = 7; // 广告
	public static final int PUSH_LOW_VALUE = 8; // 低余额提醒
	
	


	public static boolean onClickCustomContent(Context context,String title,String description,String customContentString){
		System.out.println("custome message:"+title + ":" + description + ":" + customContentString);
		JSONObject customJson = null;
		try {
			customJson = new JSONObject(customContentString);
			int type = customJson.getInt("type");
			switch (type) {
			case 10:
			{
				String url = null;
				if (customJson.has("url")) {
					url = customJson.getString("url");
				}
				
				if(PackageUtil.isRunning(context,ReactEnterActivity.class)){

					AdvActivity.openUrl(LifeManager.getActivity(), url, title);
				}else{
					
					Intent intent = new Intent(context,ReactEnterActivity.class);
					intent.putExtra("url", url);
					intent.putExtra("web",true);
					intent.putExtra("title", customJson.getString("title"));
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
				}
				
			}
				
				
			
				break;
			case 11: {
                String content = customJson.getString("content");
                if (PackageUtil.isRunning(context, ReactEnterActivity.class)) {
                    Alert.alert(LifeManager.getActivity(), customJson.getString("title"), content);
                } else {

                    Intent intent = new Intent(context, ReactEnterActivity.class);
                    intent.putExtra("alert", content);
                    intent.putExtra("web", false);
                    intent.putExtra("title", customJson.getString("title"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
                break;
            }
                case 1:{
                    handleSingleLogin(context);
                    break;
                }
			default:
				return false;
			}
			
			//记录下来

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
    public static void handleSingleLogin(Context context){
        //单点登录
        DMAccount.logout();
        JPushInterface.setAlias(LifeManager.getActivity(), "", new TagAliasCallback() {
            @Override
            public void gotResult(int arg0, String arg1, Set<String> arg2) {
                // TODO Auto-generated method stub

            }
        });
        if(JsonTaskManager.getInstance()!=null)JsonTaskManager.getInstance().logout();
        AccountModule.onLogout();
        if (PackageUtil.isRunning(context, ReactEnterActivity.class)) {
            Alert.alert(LifeManager.getActivity(), "温馨提示","您的账号在其他设备登录,如非本人操作请及时修改密码");
        } else {

            Intent intent = new Intent(context, ReactEnterActivity.class);
            intent.putExtra("alert", "您的账号在其他设备登录,如非本人操作请及时修改密码");
            intent.putExtra("web", false);
            intent.putExtra("title", "温馨提示");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
	
	
}
