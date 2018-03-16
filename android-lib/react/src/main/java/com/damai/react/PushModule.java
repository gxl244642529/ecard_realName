package com.damai.react;

import com.damai.push.Push;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by renxueliang on 16/10/25.
 */

public class PushModule extends ReactContextBaseJavaModule {

    private static PushModule _instance;

    public static PushModule runningInstance(){
        return _instance;
    }


    public PushModule(ReactApplicationContext reactContext) {
        super(reactContext);
        _instance = this;
    }

    @Override
    public String getName() {
        return "PushModule";
    }


    /**
     * 收到推送
     */
    public static void onNotifycation(JSONObject object) throws JSONException {

        PushModule instance = runningInstance();
        if(instance!=null){
            WritableMap map= ReactUtils.toWriteMap(object);
            instance.getReactApplicationContext()
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit("onPush", map);//对应的javascript层的事件名为logInConsole，注册该事件即可进行回调
        }


    }


    @ReactMethod
    public void getPushId(Callback callback){
        String pushId = Push.getPushId();
        callback.invoke(pushId);
    }

}
