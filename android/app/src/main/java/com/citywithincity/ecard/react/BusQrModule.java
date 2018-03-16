package com.citywithincity.ecard.react;

import android.os.AsyncTask;

//import com.allcitygo.qrlib.QRSdk;
import com.citywithincity.ecard.NewJavaServerHandler;
import com.citywithincity.ecard.models.vos.ECardUserInfo;
import com.damai.core.DMAccount;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;

import java.util.Map;

/**
 * Created by renxueliang on 17/3/23.
 */

public class BusQrModule extends ReactContextBaseJavaModule {


    public BusQrModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }


    @ReactMethod
    public void activate(final String account, final Callback callback){
//        AsyncTask task = new AsyncTask<Object, Object, Integer>() {
//            @Override
//            protected Integer doInBackground(Object... params) {
//                int ret = QRSdk.activeDevice(account);
//                callback.invoke(ret);
//                return ret;
//            }
//        };
//        task.execute();
    }

    @ReactMethod
    public void inactiveDevice(){
//        AsyncTask task = new AsyncTask<Object, Object, Integer>() {
//            @Override
//            protected Integer doInBackground(Object... params) {
//                int ret = QRSdk.inactiveDevice();
//                return ret;
//            }
//        };
//        task.execute();
    }

    @ReactMethod
    public void getQr(final String account, final Callback callback){
//        final ECardUserInfo userInfo = DMAccount.get();
//        AsyncTask task = new AsyncTask<Object, Object, Map<String,Object>>() {
//
//            @Override
//            protected Map<String, Object> doInBackground(Object... params) {
//                Map<String, Object> data= QRSdk.getQRCode(account);
//                callback.invoke(toWriteMap(data));
//                return data;
//            }
//
//        };
//        task.execute();

    }


    @ReactMethod
    public void disableToken(){
     //   QRSdk.disableToken();
    }


    public static WritableMap toWriteMap(Map<String, Object> data){

        WritableMap map = Arguments.createMap();
        for(Map.Entry<String,Object> entry : data.entrySet()){
            String key = entry.getKey();
            Object obj = entry.getValue();
            if(obj instanceof String) {
                map.putString(key, (String)obj);
            } else if(obj instanceof Integer) {
                map.putInt(key, ((Integer)obj).intValue());
            } else if(obj instanceof Double) {
                map.putDouble(key, ((Double)obj).doubleValue());
            } else if(obj instanceof Integer) {
                map.putInt(key, ((Integer)obj).intValue());
            } else if(obj instanceof Boolean) {
                map.putBoolean(key, ((Boolean)obj).booleanValue());
            }
        }
        return map;
    }

    @Override
    public String getName() {
        return "BusQrModule";
    }
}
