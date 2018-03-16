package com.damai.amap;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.citywithincity.models.LocalData;
import com.damai.map.LocationInfo;
import com.damai.map.LocationListener;
import com.damai.map.LocationService;

/**
 * Created by renxueliang on 17/4/4.
 */

public class AMapLocationService implements LocationService, AMapLocationListener {
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    public AMapLocationClient mlocationClient = null;

    public AMapLocationService(Context context){

        mlocationClient = new AMapLocationClient(context);
//初始化定位参数
        mLocationOption = new AMapLocationClientOption();
//设置定位监听
        mlocationClient.setLocationListener(this);

//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
//设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
// 在定位结束后，在合适的生命周期调用onDestroy()方法
// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//启动定位



    }


    private LocationInfo info;

    private LocationListener listener;

    @Override
    public void getLocation(LocationListener listener) {
        this.listener = listener;
        mlocationClient.startLocation();
    }

    public static final String SUBKEY = "map_config";

    @Override
    public LocationInfo getCachedLocation() {
        if(info==null){
            //从历史记录中加载
            LocationInfo data = LocalData.read().getObject(SUBKEY,LocationInfo.class);
            info = data;
        }
        return info;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        mlocationClient.stopLocation();

        if(listener!=null){
            LocationInfo info = new LocationInfo();
            this.info = info;
            LocalData.write().putObject(SUBKEY,info).save();
            info.setAddress(aMapLocation.getAddress());
            info.setLat(aMapLocation.getLatitude());
            info.setLng(aMapLocation.getLongitude());
            listener.onGetLocation(info);
        }
    }
}
