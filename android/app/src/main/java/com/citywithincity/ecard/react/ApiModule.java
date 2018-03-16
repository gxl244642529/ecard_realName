package com.citywithincity.ecard.react;

import android.text.TextUtils;

import com.citywithincity.models.cache.CachePolicy;
import com.citywithincity.utils.Alert;
import com.damai.auto.LifeManager;
import com.damai.core.ApiJob;
import com.damai.core.ApiListener;
import com.damai.core.DMAccount;
import com.damai.core.DMError;
import com.damai.core.DMLib;
import com.damai.core.DMMessage;
import com.damai.core.DMPage;
import com.damai.react.ReactUtils;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by renxueliang on 16/8/28.
 */
public class ApiModule extends ReactContextBaseJavaModule implements ApiListener {

    public static final int ERROR_NETWORK = 0;
    public static final int ERROR_SERVER = 1;
    public static final int ERROR_ALERT = 2;
    public static final int ERROR_TOAST = 3;


    private Map<ApiJob,ApiInfo> taskMap;

    private ApiInfo getAndRemove(ApiJob api){
        ApiInfo info = taskMap.get(api);
        taskMap.remove(api);
        return info;
    }

    @Override
    public boolean onApiMessage(ApiJob job) {
        ApiInfo info = getAndRemove(job);
        if(info!=null && info.getErrorCallback()!=null){
            DMMessage message = job.getMessage();
            int errorType = message.getType() == DMMessage.ALERT ? ERROR_ALERT : ERROR_TOAST;
            info.getErrorCallback().invoke(message.getMessage(),errorType);
            return true;
        }
        return false;
    }

    @Override
    public boolean onJobError(ApiJob job) {
        ApiInfo info = getAndRemove(job);
        if(info!=null && info.getErrorCallback()!=null){
            DMError error = job.getError();
            int errorType = error.isNetworkError() ? ERROR_NETWORK : ERROR_SERVER;
            info.getErrorCallback().invoke(error.getReason(),errorType);
            return true;
        }
        return false;
    }


    private WritableArray parseList(List<JSONObject> arr) throws JSONException {
        WritableArray result = Arguments.createArray();
        for(int i=0 , c = arr.size() ; i < c; ++i){

            JSONObject jsonObject = arr.get(i);
            WritableMap map= ReactUtils.toWriteMap(jsonObject);
            result.pushMap(map);
        }
        return result;
    }



    private WritableMap parseData(DMPage<JSONObject> page) throws JSONException {
        WritableMap map = Arguments.createMap();
        map.putInt("page",page.getPage());
        map.putInt("total",page.getTotal());
        map.putArray("result",parseList(page.getList()));
        return map;
    }

    @Override
    public void onJobSuccess(ApiJob job) {
        ApiInfo info = getAndRemove(job);
        if(info!=null && info.getSuccessCallback()!=null){
            Object data = job.getData();
            try{
                if(data instanceof DMPage){
                    info.getSuccessCallback().invoke(parseData((DMPage<JSONObject>)data));
                }else if(data instanceof  JSONArray){
                    info.getSuccessCallback().invoke(ReactUtils.toWriteArray((JSONArray)data));
                }else if(data instanceof  JSONObject){
                    info.getSuccessCallback().invoke(ReactUtils.toWriteMap((JSONObject)data));
                }else{
                    info.getSuccessCallback().invoke(data);
                }
            }catch (JSONException e){
                e.printStackTrace();
                //解析错误
                Alert.showShortToast("Json解析失败");
            }

        }
    }

    private static class ApiInfo{
        private WeakReference<ApiJob> job;
        private Callback successCallback;
        private Callback errorCallback;

        private ApiInfo(ApiJob job, Callback successCallback, Callback errorCallback) {
            this.job = new WeakReference<ApiJob>(job);
            this.successCallback = successCallback;
            this.errorCallback = errorCallback;
        }

        public ApiJob getJob() {
            return job.get();
        }



        public Callback getSuccessCallback() {
            return successCallback;
        }

        public Callback getErrorCallback() {
            return errorCallback;
        }

    }


    public ApiModule(ReactApplicationContext reactContext) {
        super(reactContext);
        taskMap = new HashMap<ApiJob,ApiInfo>();
    }

    @Override
    public String getName() {
        return "ApiModule";
    }


    @ReactMethod
    public void exit(){
        LifeManager.closeAll();
        System.exit(0);
    }

    @ReactMethod
    public void logout(){
        DMAccount.logout();
    }


    @ReactMethod
    public void finish(){
        LifeManager.getActivity().finish();
    }


    @ReactMethod
    public void api(ReadableMap data,Callback successCallback,Callback errorCallback){
        String api = data.getString("api");
        ReadableMap files =data.hasKey("files") ?  data.getMap("files") : null;
        ReadableMap postData =data.hasKey("data") ?  data.getMap("data") : null;

        ApiJob job = null;
        int type = data.hasKey("type") ? data.getInt("type") : 0;
        if(type==0){
            job = DMLib.getJobManager().createObjectApi(api);
        }else{
            job = DMLib.getJobManager().createPageApi(api);
        }
        job.setServer(1);

        if(data.hasKey("timeoutMs")){
            job.setTimeoutMS( data.getInt("timeoutMs") );
        }


        //cache
        int cachePolicy = data.getInt("cachePolicy");
        job.setCachePolicy(CachePolicy.get(cachePolicy));
        job.setRelated(getCurrentActivity());
        job.putAll(ReactUtils.toMap(postData));

        if(files!=null){
            ReadableMapKeySetIterator it = files.keySetIterator();
            while(it.hasNextKey()) {
                String key = it.nextKey();
                ReadableType t = files.getType(key);
                switch (t){
                    case Array:
                    {
                        ReadableArray arr = files.getArray(key);
                        List<File> list = new ArrayList<File>(arr.size());
                        for(int i=0 ,c = arr.size(); i < c; ++i){
                            String str = arr.getString(i);
                            if(!TextUtils.isEmpty(str)){
                                list.add(new File(str.replace("file://","")));
                            }
                        }
                        job.putList(key,list);
                    }
                    break;
                    case String:
                        String path = files.getString(key);
                        if(path.startsWith("/") || path.startsWith("file://")){
                            job.putObject(key,new File(path.replace("file://","")));
                        }

                        break;
                }

            }
        }


        if(data.hasKey("waitingMessage")){
            job.setWaitingMessage(data.getString("waitingMessage"));
        }
        int crypt = 0;
        if(data.hasKey("crypt")){
            crypt = data.getInt("crypt");
        }
        job.setCrypt(crypt);


        ApiInfo info = new ApiInfo(job,successCallback,errorCallback);
        taskMap.put(job,info);

        job.setApiListener(this);

        job.execute();

    }

}
