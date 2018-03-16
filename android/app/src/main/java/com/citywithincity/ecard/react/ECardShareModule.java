package com.citywithincity.ecard.react;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.citywithincity.ecard.ECardConfig;
import com.citywithincity.ecard.R;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;


/**
 * Created by renxueliang on 17/6/19.
 */

public class ECardShareModule extends ReactContextBaseJavaModule {

    private static ECardShareModule ECardShareModule;

    private IWXAPI mWXApi;

    public ECardShareModule(ReactApplicationContext reactContext) {
        super(reactContext);
        if (mWXApi == null) {
            mWXApi = WXAPIFactory.createWXAPI(reactContext,
                    ECardConfig.WEIXIN_APPID, true);
        }
        mWXApi.registerApp(ECardConfig.WEIXIN_APPID);
        ECardShareModule = this;
    }


    @Override
    public String getName() {
        return "ECardShareModule";
    }


    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }


    Callback callback;
    @ReactMethod
    public void wxShare(ReadableMap map,Callback callback){
        this.callback = callback;
        String title = map.getString("title");
        String content = map.getString("content");
        String url = map.getString("url");
        int type = map.getInt("type");

        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = title;
        msg.description = content;

        Context context = getCurrentActivity();

        Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        thumb.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] datas = baos.toByteArray();
        msg.thumbData = datas;//Util.bmpToByteArray(thumb, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = type;
        mWXApi.sendReq(req);

    }


    private void onShareSuccess(int result){

        if(callback != null){

            callback.invoke(result);

            callback = null;

        }


    }

    public static void onSuccess() {

        if(ECardShareModule!=null){
            ECardShareModule.onShareSuccess(0);
        }


    }

    public static void onCancel() {
        if(ECardShareModule!=null){
            ECardShareModule.onShareSuccess(-1);
        }
    }
}
