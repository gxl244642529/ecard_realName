package com.damai.react;


import com.amap.api.location.AMapLocation;
import com.amap.api.maps2d.model.LatLng;
import com.damai.map.LocationInfo;
import com.damai.map.LocationListener;
import com.damai.map.LocationUtil;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;

/**
 * Created by renxueliang on 16/12/3.
 */

/**
 *
 */
public class AMapModule extends ReactContextBaseJavaModule implements LocationListener {



    public AMapModule(ReactApplicationContext reactContext) {
        super(reactContext);



    }

    @Override
    public String getName() {
        return "AMapModule";
    }

    private Callback success;

    @ReactMethod
    public void getPosition(Callback success){

        this.success = success;
        LocationUtil.getLocation(this);
    }



    @ReactMethod
    public void getDistance(ReadableMap data, Callback callback){
        double lat = data.getDouble("lat");
        double lng = data.getDouble("lng");
        double targetLat = data.getDouble("targetLat");
        double targetLng = data.getDouble("targetLng");
        callback.invoke( LocationUtil.getDistance(lat,lng,targetLat,targetLng));
    }

    @Override
    public void onGetLocation(LocationInfo info) {
        WritableMap writableMap = Arguments.createMap();
        writableMap.putDouble("lat",info.getLat());
        writableMap.putDouble("lng",info.getLng());
        writableMap.putInt("flag",0);
        writableMap.putString("address",info.getAddress());
        success.invoke(writableMap);
    }
}
