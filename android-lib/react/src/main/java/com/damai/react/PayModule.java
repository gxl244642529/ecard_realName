package com.damai.react;

import com.citywithincity.paylib.PayType;
import com.damai.auto.DMPayEntryActivity;
import com.damai.auto.LifeManager;
import com.damai.core.DMLib;
import com.damai.pay.BasePayAction;
import com.damai.pay.DMPayListener;
import com.damai.pay.DMPayModel;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ObjectAlreadyConsumedException;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by renxueliang on 17/3/27.
 */

public class PayModule extends ReactContextBaseJavaModule implements DMPayListener {

    enum PayResult{

        GetOrderInfoSuccess,      //拉取订单成功
        PayCancel,                //支付取消
        GetOrderInfoError,        //支付错误
        PrePayError,              //预支付错误
        PrePaySuccess,            //预支付成功
        ClinetPaySuccess,         //客户端支付成功
        ClientPayError            //客户端支付失败
    };


    private DMPayModel model;
    private BasePayAction action;

    public PayModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }


    @Override
    public String getName() {
        return "PayModule";
    }

    private Callback payCallback;


    @ReactMethod
    public void create(String moduleId, ReadableArray supportPayTypes){
        int[] payTypes = new int[supportPayTypes.size()];
        for(int i=0, c = supportPayTypes.size() ; i < c ;++i){
            payTypes[i] = supportPayTypes.getInt(i);
        }
        action = new BasePayAction(moduleId,null);
        model = DMLib.getJobManager().createPayModel(moduleId, payTypes );
        model.setPayAction(action);
        model.setListener(this);
    }


    @ReactMethod
    public void call(String api, ReadableMap map,Callback callback){
        model.prePay(null,api,ReactUtils.toMap(map));
        payCallback = callback;
    }

    @ReactMethod
    public void setIndex(int index){
        model.setIndex(index);
    }

    @ReactMethod
    public void destroy(){
        if(model!=null){
            model.destroy();
            model = null;
            action.destroy();
            action= null;
        }
    }

    @Override
    public void onPaySuccess(DMPayModel model, Object data) {
        if(payCallback!=null){
            try {
                payCallback.invoke(0, ReactUtils.toWriteMap((JSONObject)data));
            } catch (JSONException e) {
               throw new RuntimeException(e);
            }
            payCallback = null;
        }
    }

    /**
     * 这要不要去拉取订单，不一定
     * @param model
     * @param type
     * @return
     */
    @Override
    public boolean onClientPaySuccess(DMPayModel model, int type,Object data) {

        if(type == PayType.PAY_WEIXIN.ordinal()){

           if(LifeManager.getActivity() instanceof DMPayEntryActivity){
               LifeManager.getActivity().finish();
           }
        }

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("orderId",model.getOrderId());
        map.put("fee",model.getFee());
        map.put("info",data);
        //需要通知到上层
        payCallback.invoke(PayResult.ClinetPaySuccess.ordinal(),
                ReactUtils.toWriteMap(map)
                );

        return true;
    }


    @Override
    public boolean onPrePayError(String reason, boolean isNetworkError) {
        if(payCallback!=null){
            WritableMap map = Arguments.createMap();
            map.putString("error",reason);
            map.putBoolean("isNetworkError",isNetworkError);
            payCallback.invoke(3,map);
            payCallback = null;
        }
        return true;
    }

    /**
     * 支付失败
     * @param type
     * @param error
     * @param isNetworkError
     */
    @Override
    public void onPayError(int type, String error, boolean isNetworkError) {

    }

    @Override
    public void onPayCancel(DMPayModel model) {
        if(payCallback!=null){
            payCallback.invoke(1);
            payCallback = null;
        }
    }

    @Override
    public boolean onGetPayInfoError(DMPayModel model, String error, boolean isNetworkError) {
        if(payCallback!=null){
            WritableMap map = Arguments.createMap();
            map.putString("error",error);
            map.putBoolean("isNetworkError",isNetworkError);
            payCallback.invoke(2,map);
            payCallback = null;
        }
        return true;
    }


}
