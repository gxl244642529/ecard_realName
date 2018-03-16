package com.facebook.react.views.webview;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.events.ContentSizeChangeEvent;
import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;

/**
 * Created by renxueliang on 17/5/26.
 */

public class OverrideLoadEvent extends Event<OverrideLoadEvent> {
    public static final String EVENT_NAME = "overrideLoad";

    private final String url;

    public OverrideLoadEvent(String url){
        this.url = url;
    }

    @Override
    public String getEventName() {
        return EVENT_NAME;
    }

    @Override
    public void dispatch(RCTEventEmitter rctEventEmitter) {
        WritableMap data = Arguments.createMap();
        data.putString("url", url);
        rctEventEmitter.receiveEvent(getViewTag(), EVENT_NAME, data);
    }
}
