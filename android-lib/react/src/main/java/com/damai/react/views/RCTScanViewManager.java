package com.damai.react.views;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;

/**
 * Created by renxueliang on 16/11/12.
 */

public class RCTScanViewManager extends SimpleViewManager<RCTScanView> {


    @Override
    public String getName() {
        return "RCTScanView";
    }

    @Override
    protected RCTScanView createViewInstance(ThemedReactContext reactContext) {
        RCTScanView scanView = new RCTScanView(reactContext);
        scanView.startSession();
        return scanView;
    }
}
