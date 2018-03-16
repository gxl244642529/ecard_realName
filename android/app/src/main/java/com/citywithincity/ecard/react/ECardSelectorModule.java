package com.citywithincity.ecard.react;

import android.app.Activity;
import android.content.Intent;

import com.citywithincity.ecard.utils.MyECardUtil;
import com.citywithincity.ecard.widget.ECardSelectView;
import com.damai.helper.ActivityResult;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

/**
 * Created by renxueliang on 17/3/30.
 */

public class ECardSelectorModule  extends ReactContextBaseJavaModule implements ActivityResult {
    public ECardSelectorModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }


    private Callback callback;


    @Override
    public String getName() {
        return "ECardSelectorModule";
    }

    @ReactMethod
    public void selectECard(Callback callback){
        this.callback = callback;
        MyECardUtil.selectECard(ECardSelectorModule.this,getCurrentActivity());

    }


    @Override
    public void onActivityResult(Intent intent, int resultCode, int requestCode) {
        if(resultCode== Activity.RESULT_OK ){
            String cardId = MyECardUtil.getCardId(intent);
            if(callback!=null){
                callback.invoke(cardId);
                callback = null;
            }

        }
    }
}
