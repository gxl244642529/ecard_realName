package com.citywithincity.ecard.react;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.tech.TagTechnology;
import android.support.annotation.Nullable;

import com.citywithincity.ecard.models.TianYu;
import com.citywithincity.ecard.nfc.NfcDialog;
import com.citywithincity.ecard.nfc.NfcResult;
import com.citywithincity.ecard.nfc.ECardNfcModel;
import com.citywithincity.ecard.utils.NfcUtil;
import com.citywithincity.interfaces.DialogListener;
import com.citywithincity.utils.Alert;
import com.citywithincity.utils.MessageUtil;
import com.damai.auto.LifeManager;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.jzoom.nfc.NfcException;
import com.jzoom.nfc.NfcListener;

import java.io.IOException;


/**
 * Created by renxueliang on 17/3/21.
 */

public class NfcModule extends ReactContextBaseJavaModule implements NfcListener {

    public static final String CPU = "cpu";

    private static NfcModule module;

    public NfcModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.module = this;
        if(ECardNfcModel.getRunningInstance()!=null){
            ECardNfcModel.getRunningInstance().setListener(this);
        }
    }

    public static NfcModule getRunningInstance() {
        return module;
    }

    @Override
    public String getName() {
        return "NfcModule";
    }


    @ReactMethod
    public void close(){
        if(ECardNfcModel.getRunningInstance()!=null){
            ECardNfcModel.getRunningInstance().close();
        }
    }


    @ReactMethod
    public void isAvailable(Callback callback){
        callback.invoke(NfcUtil.isAvailable(getCurrentActivity()));
    }


    @Override
    public void onNfcEvent(TagTechnology tag) {

        notifyEvent("nfcTag", Arguments.createMap());

    }

    protected void notifyEvent(String eventName,@Nullable Object data){
        try{
            getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName,data);
        }catch (Throwable t){
            t.printStackTrace();
            MessageUtil.sendMessage(200,new MessageUtil.IMessageListener() {
                @Override
                public void onMessage(int i) {
                    System.err.print("========================nfc read again==============================================");
                    if(getCurrentActivity()!=null && !getCurrentActivity().isFinishing())
                        notifyEvent("nfcTag", Arguments.createMap());
                }
            });
        }

    }

    private NfcDialog nfcDialog;



    @ReactMethod
    public void handleSelf(){
        if(nfcDialog!=null){
            return;
        }
        if(ECardNfcModel.getRunningInstance()==null){
            return;
        }
        try {
            NfcResult result = ECardNfcModel.getRunningInstance().getCardInfo();
            nfcDialog = new NfcDialog(getCurrentActivity(), new DialogListener() {
                @Override
                public void onDialogButton(int i) {
                    nfcDialog = null;
                }
            });
            nfcDialog.onNecReaded(result);
        } catch (IOException e) {
            Alert.showShortToast("请重新贴卡");
        } catch (NfcException e) {
            Alert.showShortToast("请重新贴卡");
        } finally {
            ECardNfcModel.getRunningInstance().close();
        }

    }



    @ReactMethod
    public void readCard(Callback callback){
        if(ECardNfcModel.getRunningInstance()!=null){
            try {
                callback.invoke(ECardNfcModel.getRunningInstance().getCardId());
            }  catch (IOException e) {
                Alert.showShortToast("请重新贴卡");
            } catch (NfcException e) {
                Alert.showShortToast("请重新贴卡");
            }
        }else{
            callback.invoke("");
        }
    }


    @ReactMethod
    public void recharge(String tyId,String cardId,int fee,String acc){
        Activity ac= getCurrentActivity();
        if (NfcUtil.isAvailable(ac)) {
            TianYu.startRecharge(ac,
                    acc,
                    cardId,
                    tyId, fee,false);
        } else {
            Alert.showShortToast("本手机不支持nfc设备");
        }
    }

}
