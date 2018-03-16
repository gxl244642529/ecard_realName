package com.damai.react.views;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

/**
 * Created by renxueliang on 16/12/5.
 */

public class RCTQrViewManager extends SimpleViewManager<RCTQrView> {
    @Override
    public String getName() {
        return "RCTQrView";
    }

    @Override
    protected RCTQrView createViewInstance(ThemedReactContext reactContext) {
        return new RCTQrView(reactContext);
    }


    @ReactProp(name="content")
    public void setContent(RCTQrView view,String content){
        view.setContent(content);
    }

}
