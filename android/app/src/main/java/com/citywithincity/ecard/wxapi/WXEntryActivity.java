package com.citywithincity.ecard.wxapi;

import android.os.Bundle;
import android.util.Log;

import com.citywithincity.ecard.ECardConfig;
import com.citywithincity.ecard.react.ECardShareModule;
import com.damai.auto.DMActivity;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by renxueliang on 2017/7/8.
 */

public class WXEntryActivity extends DMActivity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    protected void onSetContent(Bundle bundle) {
        this.setContentView(com.damai.lib.R.layout._dialog_wait);

        //注册API
        api = WXAPIFactory.createWXAPI(this, ECardConfig.WEIXIN_APPID);
        api.handleIntent(getIntent(), this);
        Log.i("savedInstanceState"," sacvsa"+api.handleIntent(getIntent(), this));
    }

    @Override
    public void onReq(BaseReq baseReq) {

        System.out.print(baseReq);

    }

    @Override
    public void onResp(BaseResp resp) {

        if(resp instanceof SendAuth.Resp){
            SendAuth.Resp newResp = (SendAuth.Resp) resp;
            //获取微信传回的code
            String code = newResp.code;
            Log.i("newRespnewResp","   code    :"+code);

        }

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                Log.i("savedInstanceState","发送成功ERR_OKERR_OK");

                ECardShareModule.onSuccess();

                //发送成功
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                Log.i("savedInstanceState","发送取消ERR_USER_CANCEL");
                ECardShareModule.onCancel();
                //发送取消
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                Log.i("savedInstanceState","发送取消ERR_AUTH_DENIEDERR_AUTH_DENIEDERR_AUTH_DENIED");
                //发送被拒绝
                break;
            default:
                Log.i("savedInstanceState","发送返回breakbreakbreak");
                //发送返回
                break;
        }
        finish();


    }
}
