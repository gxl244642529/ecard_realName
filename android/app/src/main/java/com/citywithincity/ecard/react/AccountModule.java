package com.citywithincity.ecard.react;

import android.content.Intent;

import com.citywithincity.ecard.access.LoginActivity;
import com.citywithincity.ecard.models.vos.ECardUserInfo;
import com.damai.core.DMAccount;
import com.damai.core.LoginListener;
import com.damai.core.LoginListenerWrap;
import com.damai.react.ReactUtils;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.modules.core.RCTNativeAppEventEmitter;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by renxueliang on 16/8/28.
 */
public class AccountModule extends ReactContextBaseJavaModule {


    private static WeakReference<AccountModule> runningInstance;


    private Callback callback;
    private LoginListener listener;

    public AccountModule(ReactApplicationContext reactContext) {
        super(reactContext);
        runningInstance = new WeakReference<AccountModule>(this);

    }

    @Override
    public String getName() {
        return "AccountModule";
    }

    /**
     * 保存数据
     * @param data
     */
    @ReactMethod
    public void save(ReadableMap data) throws JSONException {
        ECardUserInfo info = DMAccount.get();
      //  JSONObject jsonObject = ReactUtils.toJSONObject(data);
        info.setData( ReactUtils.toMap(data));
        info.save();
    }



    @ReactMethod
    public void login(final Callback callback){

        LoginListenerWrap.getInstance().setListener(new LoginListener() {
            @Override
            public void onLoginSuccess() {
                callback.invoke(1);
            }

            @Override
            public void onLoginCancel() {
                callback.invoke(0);
            }
        });


        getCurrentActivity().startActivity(new Intent(getCurrentActivity(),LoginActivity.class));
    }


    @ReactMethod
    public void logout(){
        DMAccount.logout();
    }

    public static AccountModule getRunningInstance(){
        if(runningInstance==null)return null;
        return runningInstance.get();
    }


    public static void onLoginSuccess(ECardUserInfo account) {
        AccountModule instance = getRunningInstance();
        if(instance!=null){
            instance.getReactApplicationContext()
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit("loginSuccess", account.toWriteMap());//对应的javascript层的事件名为logInConsole，注册该事件即可进行回调
        }
    }

    public static void onChangeHead(String url){
        AccountModule instance = getRunningInstance();
        if(instance!=null){
            WritableMap writeMap = Arguments.createMap();
            writeMap.putString("url",url);
            instance.getReactApplicationContext()
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit("headChanged", writeMap);//对应的javascript层的事件名为logInConsole，注册该事件即可进行回调
        }
    }

    public static void onLogout() {
        AccountModule instance = getRunningInstance();
        if(instance!=null){
            instance.getReactApplicationContext()
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit("logoutSuccess", null);//对应的javascript层的事件名为logInConsole，注册该事件即可进行回调
        }
    }
}
