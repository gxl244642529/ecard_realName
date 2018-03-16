package com.citywithincity.ecard.pingan;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.citywithincity.activities.PinganWebActivity;
import com.citywithincity.ecard.ECardConfig;
import com.citywithincity.ecard.R;
import com.damai.auto.DMWebActivity;
import com.damai.core.DMAccount;
import com.facebook.common.logging.FLog;
import com.facebook.react.common.ReactConstants;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by renxueliang on 16/12/8.
 */

public class PinganActivity extends DMWebActivity {

    private Set<String> whiteList = new HashSet<String>();

    private Set<String> whiteUrl = new HashSet<String>();

    private Set<String> titleBlackList = new HashSet<String>();


    private String index = "";


    public PinganActivity(){
        whiteList.add("home.pingan.com.cn");

        if(!ECardConfig.PINGAN_RELEASE){
            //测试环境
            whiteList.add("egis-cssp-dmzstg1.pingan.com.cn");
            whiteList.add("egis-cssp-dmzstg2.pingan.com.cn");
            whiteList.add("egis-cssp-dmzstg3.pingan.com.cn");
        }


   //     whiteUrl.add("https://rsb-stg2.pingan.com.cn:7443/ibd/page/web_for_h5/mainAcct_h5/index.html?environment=rsb#ylx/index/input");

        if(ECardConfig.PINGAN_RELEASE){
            //生产环境
            whiteUrl.add("https://cz.pingan.com.cn/ibd/page/web_for_h5/mainAcct_h5/index.html#ylx/index/finish");
        }else{
            //测试环境
            whiteUrl.add("https://rsb-stg2.pingan.com.cn:7443/ibd/page/web_for_h5/mainAcct_h5/index.html?environment=rsb#ylx/index/finish");
        }

//title黑名单

        /***
         *
         *
         *
         *
         * 测试环境：

         https://rsb-stg2.pingan.com.cn:7443/ibd/page/web_for_h5/mainAcct_h5/index.html?environment=rsb#ylx/index/input    //开户首页

         https://rsb-stg2.pingan.com.cn:7443/ibd/page/web_for_h5/mainAcct_h5/index.html?environment=rsb#ylx/index/passSet   //开户设密页面

         https://rsb-stg2.pingan.com.cn:7443/ibd/page/web_for_h5/mainAcct_h5/index.html?environment=rsb#ylx/index/finish   //开户完成页面


         * 生产环境：

         https://cz.pingan.com.cn/ibd/page/web_for_h5/mainAcct_h5/index.html#ylx/index/input    //开户首页

         https://cz.pingan.com.cn/ibd/page/web_for_h5/mainAcct_h5/index.html#ylx/index/passSet   //开户设密页面

         https://cz.pingan.com.cn/ibd/page/web_for_h5/mainAcct_h5/index.html#ylx/index/finish   //开户完成页面


         *
         *
         */

        if(ECardConfig.PINGAN_RELEASE){
            //生产环境
            titleBlackList.add("https://cz.pingan.com.cn/ibd/page/web_for_h5/mainAcct_h5/index.html#ylx/index/input");
            titleBlackList.add("https://cz.pingan.com.cn/ibd/page/web_for_h5/mainAcct_h5/index.html#ylx/index/passSet");
            titleBlackList.add("https://cz.pingan.com.cn/ibd/page/web_for_h5/mainAcct_h5/index.html#ylx/index/finish");

        }else{


            //测试环境

            titleBlackList.add("https://rsb-stg2.pingan.com.cn:7443/ibd/page/web_for_h5/mainAcct_h5/index.html?environment=rsb#ylx/index/input");
            titleBlackList.add("https://rsb-stg2.pingan.com.cn:7443/ibd/page/web_for_h5/mainAcct_h5/index.html?environment=rsb#ylx/index/passSet");
            titleBlackList.add("https://rsb-stg2.pingan.com.cn:7443/ibd/page/web_for_h5/mainAcct_h5/index.html?environment=rsb#ylx/index/finish");


        }


        getTitleWithJs = true;
    }


    @Override
    protected boolean parseUrl(String url) {
        if(url.startsWith("weixin")){
            return false;
        }
        if(!super.parseUrl(url)){
            Map<String,String> ref= new HashMap<String,String>();
            ref.put("Referer","https://home.pingan.com.cn");
            getWebView().loadUrl(url,ref);

            return true;

        }
        return true;
    }

    @Override
    protected void onSetContent(Bundle savedInstanceState) {
        super.onSetContent(savedInstanceState);
        findViewById(R.id._id_refresh).setVisibility(View.GONE);

    }


    protected void orgParseInt(Intent intent){
        super.parseIntent(intent);
    }

    @Override
    protected void parseIntent(Intent intent) {
        load();
    }



    protected  void load(){
        DMAccount account = DMAccount.get();
        if(ECardConfig.PINGAN_RELEASE){
            load("http://www.cczcc.net/index.php/pingan260/index/"+account.getId());
        }else{
            load("http://www.cczcc.net/index.php/pingan_test/index/"+account.getId());
        }
        setTitle("平安之家");
    }

    @Override
    protected void overrideWebSettings(WebSettings webSettings) {
        super.overrideWebSettings(webSettings);
        WebView webView = getWebView();
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSavePassword(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setSupportMultipleWindows(true);
        webView.getSettings().setAppCacheMaxSize(1024*1024*16);
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        webView.getSettings().setAppCachePath(appCachePath);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        String databasePath = "/data/data/" + getApplicationContext().getPackageName() + "/databases/";
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //  webView.getSettings().setPluginState();
        webView.getSettings().setDatabasePath(databasePath);
        //webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setSaveFormData(true);
        //  webView.getSettings().setPluginsEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);

    }

    private String getHost(){
        try {
            String url = getUrl();
            URI uri = new URI(url);
            return uri.getHost();
        } catch (URISyntaxException e) {
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // WebView 缓存文件
        CookieSyncManager.createInstance(this);
        CookieManager.getInstance().removeAllCookie();
    }

    @Override
    protected boolean shouldOverrideUrlLoading(WebView view, String url) {

        if(url.startsWith("weixin")){
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                view.getContext().startActivity(intent);
            } catch (ActivityNotFoundException e) {
                FLog.w(ReactConstants.TAG, "activity not found to handle uri scheme for: " + url, e);
            }
            return true;
        }
        if(url.contains("{userid}")){
            DMAccount account = DMAccount.get();
            url = url.replace("{userid}",account.getId());
        }

        if(url.contains("%7Buserid%7D")){
            DMAccount account = DMAccount.get();
            url = url.replace("%7Buserid%7D",account.getId());
        }
        if(url.startsWith("https://home.pingan.com.cn/m/insurance_unlogin/index.html?shareId=")){

            PinganWebActivity.openUrl(this,url,"平安之家");
            return true;
        }


        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    protected void load(String url) {
        if(url.endsWith("pdf")){
            String name = url.substring(url.lastIndexOf('/'));
            url = "http://www.cczcc.net/pingan/" + name + ".htm";
        }
        Map<String,String> ref= new HashMap<String,String>();
        ref.put("Referer","https://home.pingan.com.cn");
        this.getWebView().loadUrl(url, ref);

    }

    @Override
    protected void backToPrevious() {
        if( whiteUrl.contains(getUrl())){
            finish();
            return;
        }
        if(whiteList.contains(getHost())){
            getWebView().loadUrl("javascript:window.back();");
        }else{
            super.backToPrevious();
        }
    }
}
