package com.citywithincity.ecard.react;

import android.os.Bundle;

import com.citywithincity.ecard.models.vos.ECardVo;
import com.citywithincity.utils.MemoryUtil;
import com.damai.react.ReactUtils;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.Map;

/**
 * Created by renxueliang on 17/4/5.
 */

public class ECardReactUtils {
    public static ReactApplicationContext context;
    public static void notifyGotoReal(ECardVo eCardVo,boolean isReal){
        WritableMap map = Arguments.createMap();
        map.putString("cardId",eCardVo.getCardid());
        map.putString("createDate",eCardVo.getCreateDate());
        map.putBoolean("real",isReal);
        context.getJSModule(
                DeviceEventManagerModule.RCTDeviceEventEmitter.class
        ).emit("gotoRealCard",map);

    }

    /**
     * 发送消息
     * @param name
     * @param data
     */
    public static void notifyObservers(String name, Map<String,Object> data){
        context.getJSModule(
                DeviceEventManagerModule.RCTDeviceEventEmitter.class
        ).emit(name, ReactUtils.toWriteMap(data));
    }

    public static void notifyObservers(String name, Bundle data){
        notifyObservers(name,MemoryUtil.bundleToMap(data));
    }
}
