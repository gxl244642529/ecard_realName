//
//  DMMapUtil.m
//  amap
//
//  Created by 任雪亮 on 17/1/18.
//  Copyright © 2017年 任雪亮. All rights reserved.
//

#import "DMMapUtil.h"
#import <MAMapKit/MAMapKit.h>
#import <AMapLocationKit/AMapLocationKit.h>
#import <AMapFoundationKit/AMapFoundationKit.h>

@implementation DMLocationInfo



@end


AMapLocationManager* _locationManager;
@implementation DMMapUtil

+(void)setAppKey:(NSString*)key{
    [AMapServices sharedServices].apiKey = key;
}
+(void)getPosition: (void (^)(DMLocationInfo*)) callback{
    AMapLocatingCompletionBlock completionBlock = ^(CLLocation *location, AMapLocationReGeocode *regeocode, NSError *error)
    {
        if (error)
        {
            //kCLErrorLocationUnknown
            //连接异常 8
            NSLog(@"locError:{%ld - %@};", (long)error.code, error.localizedDescription);
            
            //如果为定位失败的error，则不进行后续操作
            if (error.code == AMapLocationErrorLocateFailed)
            {
                
                
                callback(nil);
                
              
                return;
            }
        }
        
        //得到定位信息
        if (location)
        {
            DMLocationInfo* info = [[DMLocationInfo alloc]init];
            info.lat = location.coordinate.latitude;
            info.lng = location.coordinate.longitude;
            if (regeocode)
            {
                info.address = regeocode.AOIName;
                info.detailAddress = regeocode.formattedAddress;
            }
            callback(info);
            
        }
    };
    if(!_locationManager){
        _locationManager = [[AMapLocationManager alloc] init];
        
        [_locationManager setDelegate:self];
        
        //设置期望定位精度
        [_locationManager setDesiredAccuracy:kCLLocationAccuracyHundredMeters];
        
        //设置不允许系统暂停定位
        [_locationManager setPausesLocationUpdatesAutomatically:NO];
        
        //设置允许在后台定位
        [_locationManager setAllowsBackgroundLocationUpdates:NO];
        
        //设置定位超时时间
        [_locationManager setLocationTimeout:DefaultLocationTimeout];
        
        //设置逆地理超时时间
        [_locationManager setReGeocodeTimeout:DefaultReGeocodeTimeout];
        
    }
    //进行单次带逆地理定位请求
    [_locationManager requestLocationWithReGeocode:YES completionBlock:completionBlock];
}

+(double)getDistance:(double)lat lng:(double)lng targetLat:(double)targetLat targetLng:(double)targetLng{
  /*  NSNumber* lat = opts[@"lat"];
    NSNumber* lng = opts[@"lng"] ;
    NSNumber* targetLat = opts[@"targetLat"] ;
    NSNumber* targetLng = opts[@"targetLng"] ;
    */
    
    MAMapPoint point1 = MAMapPointForCoordinate(CLLocationCoordinate2DMake(lat,lng));
    MAMapPoint point2 = MAMapPointForCoordinate(CLLocationCoordinate2DMake(targetLat,targetLng));
    //2.计算距离
    CLLocationDistance distance = MAMetersBetweenMapPoints(point1,point2);
    
    return distance;
//    callback(@[ [NSNumber numberWithDouble:distance] ]);

}

@end
