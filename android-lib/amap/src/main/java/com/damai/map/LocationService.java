package com.damai.map;

/**
 * Created by renxueliang on 17/4/4.
 */

public interface LocationService {
    void getLocation(LocationListener listener);
    LocationInfo getCachedLocation();
}
