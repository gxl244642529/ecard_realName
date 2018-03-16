package com.damai.react.views;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

/**
 * Created by renxueliang on 16/11/9.
 */

public class RCTAMapManager extends SimpleViewManager<RCTAMap> {
    @Override
    public String getName() {
        return "RCTAMap";
    }

    @Override
    protected RCTAMap createViewInstance(ThemedReactContext reactContext) {
        return new RCTAMap(reactContext);
    }

    @ReactProp(name="userTrackingMode")
    public void setUserTrackingMode(RCTAMap view,String userTrackingMode){

    }

}
