package com.citywithincity.ecard;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.damai.core.DMAccount;
import com.damai.react.PushModule;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by renxueliang on 16/10/25.
 */

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    public MyReceiver(){
        System.out.print("MyReceiver ");

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
      //  Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            String extraString = bundle.getString(JPushInterface.EXTRA_EXTRA);
            String title= bundle.getString(JPushInterface.EXTRA_TITLE);
            String description = bundle.getString(JPushInterface.EXTRA_ALERT);
            if(!PushHandler.onClickCustomContent(context, title, description, extraString)){
                processOnClick(context,bundle);
            }

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
            if(connected){
                if(DMAccount.isLogin()){
                    JPushInterface.setAlias(context,
                            DMAccount.get().getAccount(), new TagAliasCallback() {

                                @Override
                                public void gotResult(int arg0, String arg1, Set<String> arg2) {

                                    System.out.print("yes");
                                }
                            });
                }
            }
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    private void processOnClick(Context context, Bundle bundle) {
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        try {
            PushModule.onNotifycation(new JSONObject(extras));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //send msg to ReactEnterActivity
    private void processCustomMessage(Context context, Bundle bundle) {
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        if(message!=null){
            try {
                JSONObject jsonObject = new JSONObject(message);
                int type = jsonObject.getInt("type");
                if(type == 1){
                    //单点登录
                    PushHandler.handleSingleLogin(context);
                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return;
        }


        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        if(extras!=null){
            try {
                PushModule.onNotifycation(new JSONObject(extras));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
