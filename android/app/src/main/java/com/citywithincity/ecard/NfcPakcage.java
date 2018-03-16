package com.citywithincity.ecard;

/*
 * *
 *  * Copyright (c) $year-present, JZoom, Inc.
 *  * All rights reserved.
 *  *
 *  * This source code is licensed under the Apache-2.0 license found in the
 *  * LICENSE page
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *
 *
 *
 */

import com.citywithincity.ecard.react.NfcModule;
import com.facebook.react.LazyReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.ModuleSpec;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.module.model.ReactModuleInfoProvider;
import com.facebook.react.modules.appstate.AppStateModule;
import com.jzoom.nfc.NfcModel;

import java.util.Arrays;
import java.util.List;

import javax.inject.Provider;

/**
 * Created by renxueliang on 2017/11/24.
 */

public class NfcPakcage extends LazyReactPackage {
    @Override
    public List<ModuleSpec> getNativeModules(final ReactApplicationContext reactContext) {
        return Arrays.asList( new ModuleSpec(NfcModule.class, new Provider<NativeModule>() {
            @Override
            public NativeModule get() {
                return new NfcModule(reactContext);
            }
        }));
    }

    @Override
    public ReactModuleInfoProvider getReactModuleInfoProvider() {
        return LazyReactPackage.getReactModuleInfoProviderViaReflection(this);
    }

    @Override
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Arrays.asList();
    }
}
