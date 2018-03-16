package com.damai.react;

import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.utils.Alert;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

/**
 * Created by renxueliang on 16/8/28.
 */
public class AlertModule extends ReactContextBaseJavaModule {
    public AlertModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "AlertModule";
    }

    @ReactMethod
    public void alert(String message, final Callback callback){
        Alert.alert(getCurrentActivity(), "温馨提示", message, new DialogListener() {
            @Override
            public void onDialogButton(int id) {
                if(id== R.id._id_ok){
                    callback.invoke(0);
                }else{
                    callback.invoke(1);
                }
            }
        });
    }

    @ReactMethod
    public void confirm(String message, final Callback callback){
        Alert.confirm(getCurrentActivity(), "温馨提示", message, new DialogListener() {
            @Override
            public void onDialogButton(int id) {
                if(id== R.id._id_ok){
                    callback.invoke(0);
                }else{
                    callback.invoke(1);
                }
            }
        });
    }


    @ReactMethod
    public void toast(String message){
       Alert.showShortToast(message);
    }

    @ReactMethod
    public void wait(String message){
        Alert.wait(getCurrentActivity(),message);
    }

    @ReactMethod
    public void cancelWait(){
        Alert.cancelWait();
    }
}
