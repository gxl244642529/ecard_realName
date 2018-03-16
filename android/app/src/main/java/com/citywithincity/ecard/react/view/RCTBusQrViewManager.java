package com.citywithincity.ecard.react.view;

import com.damai.react.views.RCTQrView;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;

/**
 * Created by renxueliang on 17/4/13.
 */

public class RCTBusQrViewManager extends SimpleViewManager<RCTBusQrView> {
    @Override
    public String getName() {
        return "RCTBusQrView";
    }

    @Override
    protected RCTBusQrView createViewInstance(ThemedReactContext reactContext) {
        return new RCTBusQrView(reactContext);
    }


    @ReactProp(name="content")
    public void setContent(RCTBusQrView view,String content){
        view.setContent(content);
    }
}
