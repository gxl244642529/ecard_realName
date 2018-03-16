package com.citywithincity.ecard.react;

import android.content.ContentResolver;
import android.provider.Settings;
import android.view.WindowManager;

import com.citywithincity.utils.MessageUtil;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

/**
 * Created by renxueliang on 17/5/12.
 */

public class ScreenModule extends ReactContextBaseJavaModule {
    public ScreenModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "ScreenModule";
    }

    @ReactMethod
    public void setValue(final int value){
        MessageUtil.sendMessage(new MessageUtil.IMessageListener() {
            @Override
            public void onMessage(int i) {
                setScrennManualMode();
                WindowManager.LayoutParams lp = getCurrentActivity().getWindow().getAttributes();
                lp.screenBrightness = Float.valueOf(value) * (1f / 100f);
                getCurrentActivity().getWindow().setAttributes(lp);
            }
        });

    }

    @ReactMethod
    public void getValue(final Callback callback){
        MessageUtil.sendMessage(new MessageUtil.IMessageListener() {
            @Override
            public void onMessage(int i) {
                int value = 0;
                ContentResolver cr = getCurrentActivity().getContentResolver();
                try {
                    value = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }
                //计算
                value = (int) ((float)value / 255 * 100);
                callback.invoke(value);
            }
        });


    }

    public void setScrennManualMode() {
        ContentResolver contentResolver = getCurrentActivity().getContentResolver();
        try {
            int mode = Settings.System.getInt(contentResolver,
                    Settings.System.SCREEN_BRIGHTNESS_MODE);
            if (mode == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                Settings.System.putInt(contentResolver, Settings.System.SCREEN_BRIGHTNESS_MODE,
                        Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }
}
