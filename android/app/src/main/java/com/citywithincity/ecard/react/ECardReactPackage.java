package com.citywithincity.ecard.react;


import com.citywithincity.ecard.react.view.RCTBusQrViewManager;
import com.damai.react.AMapModule;
import com.damai.react.AlertModule;
import com.damai.react.BrowserModule;
import com.damai.react.DatePickerModule;
import com.damai.react.ImagePicker;
import com.damai.react.PushModule;
import com.damai.react.SelectModule;
import com.damai.react.views.RCTAMapManager;
import com.damai.react.views.RCTQrViewManager;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.facebook.react.views.webview.MyReactWebViewManager;

import java.util.Arrays;
import java.util.List;

/**
 * Created by renxueliang on 16/11/11.
 */

public class ECardReactPackage implements ReactPackage {
    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        ECardReactUtils.context = reactContext;
        return Arrays.<NativeModule>asList(
                new AccountModule(reactContext),
                new ApiModule(reactContext),
                new AlertModule(reactContext),
                new SelectModule(reactContext),
                new PushModule(reactContext),
                new DatePickerModule(reactContext),
                new AMapModule(reactContext),
                new ImagePicker(reactContext),
                new BrowserModule(reactContext),
                new NativeCallModule(reactContext),
                new SysModule(reactContext),
                new BusQrModule(reactContext),
                new PayModule(reactContext),
                new NfcModule(reactContext),
                new ECardSelectorModule(reactContext),
                new ScreenModule(reactContext),
                new ECardShareModule(reactContext)

        );
    }

    @Override
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Arrays.asList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Arrays.<ViewManager>asList(
                new RCTAMapManager(),
                new RCTQrViewManager(),
                new RCTBusQrViewManager(),
                new MyReactWebViewManager()
        );
    }
}
