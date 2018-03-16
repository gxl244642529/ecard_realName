package com.citywithincity.ecard.pingan;

import com.damai.core.DMServers;

/**
 * Created by renxueliang on 2017/9/14.
 */

public class FundActivity extends PinganActivity {

    @Override
    protected void load() {
        load(DMServers.getImageUrl(0,"/index.php/fund"));
        setTitle("惠民理财");
    }
}
