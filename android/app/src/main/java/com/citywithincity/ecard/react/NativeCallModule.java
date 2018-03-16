package com.citywithincity.ecard.react;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by renxueliang on 17/3/19.
 */

public class NativeCallModule extends ReactContextBaseJavaModule {


    private Map<String,Class<?>> map;

    public NativeCallModule(ReactApplicationContext reactContext) {
        super(reactContext);
        map = new HashMap<String,Class<?>>();
        //map.put("selling",);
    }

    @Override
    public String getName() {
        return "NativeCallModule";
    }

    @ReactMethod
    public void open(String id){



    }
}
