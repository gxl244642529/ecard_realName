package com.citywithincity.ecard.licai;

import android.os.Bundle;
import android.webkit.WebView;

import com.damai.auto.DMWebActivity;

/**
 * Created by renxueliang on 17/3/23.
 */

public class LicaiActivity extends DMWebActivity {

    @Override
    protected void onSetContent(Bundle savedInstanceState) {
        super.onSetContent(savedInstanceState);

        this.setTitle("惠民理财");
        this.load("http://218.5.80.17:8092/c_licai");
    }

    @Override
    protected void onReceivedTitle(WebView view, String title) {
        if(title!=null && !title.isEmpty() && !title.startsWith("http")  && !title.startsWith("218")) {
            setTitle(title);
        }
    }
}
