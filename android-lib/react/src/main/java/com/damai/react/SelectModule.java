package com.damai.react;

import com.citywithincity.utils.Alert;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;

/**
 * Created by renxueliang on 16/10/14.
 */

public class SelectModule extends ReactContextBaseJavaModule {
    public SelectModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "SelectModule";
    }

    @ReactMethod
    public void select(ReadableArray labels, int selectedIndex, String title, final Callback callback){
        int c = labels.size();
        String[] ls = new String[c];
        for(int i=0; i < c; ++i){
            ls[i] = labels.getString(i);
        }
        Alert.select(getCurrentActivity(), title, ls, selectedIndex, new Alert.IOnSelect<String>() {
            @Override
            public void onSelectData(int position, String data) {
                callback.invoke(position);
            }
        });
    }

}
