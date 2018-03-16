package com.damai.react;

import com.damai.auto.DMWebActivity;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

/**
 * Created by renxueliang on 16/10/29.
 */

public class BrowserModule extends ReactContextBaseJavaModule {
    public BrowserModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "WebModule";
    }

    @ReactMethod
    public void open(String url,String title){
        DMWebActivity.openUrl(getCurrentActivity(),url,title);
    }
}
