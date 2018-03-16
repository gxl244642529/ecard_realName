package com.citywithincity.activities;

import android.app.Activity;
import android.content.Intent;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.citywithincity.ecard.pingan.PinganActivity;
import com.damai.auto.DMWebActivity;

/**
 * Created by renxueliang on 2017/10/11.
 */

public class PinganWebActivity extends PinganActivity {

    public static void openUrl(Activity context, String url, String title) {
        openUrl(context, url, title, true);
    }

    public static void openUrl(Activity context, String url, String title, boolean parseTitle) {
        Intent intent = new Intent(context, PinganWebActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        intent.putExtra("parseTitle", parseTitle);
        context.startActivity(intent);
    }


    @Override
    protected void parseIntent(Intent intent) {
        orgParseInt(intent);
    }

    @Override
    protected void backToPrevious() {
        if(this.getWebView().canGoBack()) {
            this.getWebView().goBack();
        } else {
            finish();
        }
    }
}
