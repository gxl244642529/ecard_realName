package com.citywithincity.ecard.bus;

import android.os.Bundle;

import com.damai.auto.DMWebActivity;

/**
 * Created by renxueliang on 17/3/23.
 */

public class BusActivity extends DMWebActivity {

    @Override
    protected void onSetContent(Bundle savedInstanceState) {
        super.onSetContent(savedInstanceState);

        load("http://m.doudou360.com/bus/Index.aspx?area=xiamen&partner=etongka.com");
        setTitle("公交路线");
    }
}
