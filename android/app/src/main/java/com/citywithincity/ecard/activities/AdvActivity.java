package com.citywithincity.ecard.activities;

import android.app.Activity;
import android.content.Intent;

import com.citywithincity.ecard.pingan.PinganActivity;
import com.damai.auto.DMWebActivity;

/**
 * Created by jzoom on 2018/2/5.
 */

public class AdvActivity extends PinganActivity {

    @Override
    protected void parseIntent(Intent intent) {
        orgParseInt(intent);
    }


    public static void openUrl(Activity context, String url, String title) {
        Intent intent = new Intent(context, AdvActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        intent.putExtra("parseTitle", true);
        context.startActivity(intent);
    }
}
