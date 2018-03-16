package com.damai.map;

import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.model.LatLng;

/**
 * Created by renxueliang on 17/4/4.
 */

public class LocationUtil {

    private static LocationService _locationService;

    public static void setLocationService(LocationService service){
        _locationService = service;
    }

    public static void getLocation(LocationListener listener){
        _locationService.getLocation(listener);
    }
    public static LocationInfo getCachedLocation(){
        return _locationService.getCachedLocation();
    }

    public static double getDistance(double lat, double lng, double targetLat, double targetLng) {
        LatLng start = new LatLng(lat, lng);
        LatLng end = new LatLng(targetLat,targetLng);
        return AMapUtils.calculateLineDistance(start, end);
    }
}
