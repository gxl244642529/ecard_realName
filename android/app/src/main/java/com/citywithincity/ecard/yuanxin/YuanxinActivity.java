package com.citywithincity.ecard.yuanxin;

import android.os.Bundle;
import android.webkit.WebSettings;

import com.damai.auto.DMWebActivity;
import com.damai.core.DMAccount;

/**
 * Created by renxueliang on 16/12/14.
 */

public class YuanxinActivity extends DMWebActivity {

    @Override
    protected void overrideWebSettings(WebSettings webSettings) {
        super.overrideWebSettings(webSettings);
        getWebView().getSettings().setDefaultTextEncodingName("utf-8");

    }

    @Override
    protected void onSetContent(Bundle savedInstanceState) {
        super.onSetContent(savedInstanceState);
        DMAccount account = DMAccount.get();
        this.load("http://www.cczcc.net/index.php/yuanxin/index/"+account.getId());
        this.setTitle("圆信永丰");
    }

    @Override
    protected void backToPrevious() {
        String url = getWebView().getUrl();
        if(url.startsWith("http://test.m.gtsfund.com.cn/app-fund/pages/account-openAcco-tl-validate.html")){
            //进行跳转
            DMAccount account = DMAccount.get();
            String id = account.getId();
            load("http://www.cczcc.net/index.php/yuanxin/index/"+id+"/update");

        }else if(url.startsWith("http://www.cczcc.net/index.php/yuanxin/index/")){

            finish();

        }else{
            super.backToPrevious();
        }
    }
}
