package com.damai.react.views;

import android.content.Context;
import android.util.AttributeSet;

import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.MapView;

/**
 * Created by renxueliang on 16/10/29.
 */
public class RCTAMap extends MapView {


    public RCTAMap(Context context) {
        super(context);
    }

    public RCTAMap(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public RCTAMap(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public RCTAMap(Context context, AMapOptions aMapOptions) {
        super(context, aMapOptions);
    }
}
