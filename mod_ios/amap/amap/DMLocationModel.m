//
//  LocationModel.m
//  test
//
//  Created by 任雪亮 on 16/9/26.
//  Copyright © 2016年 任雪亮. All rights reserved.
//

#import "DMLocationModel.h"
#import <AMapLocationKit/AMapLocationKit.h>
#import <AMapFoundationKit/AMapFoundationKit.h>


@implementation DMLocationInfo



@end


@interface DMLocationModel() <AMapLocationManagerDelegate>
{
    AMapLocationManager* _locationManager;
}

@property (nonatomic, copy) AMapLocatingCompletionBlock completionBlock;

@end


@implementation DMLocationModel




-(void)startLocation{
    
    __weak id<DMLocationDelegate> __locationDelegate = _delegate;
    self.completionBlock = ^(CLLocation *location, AMapLocationReGeocode *regeocode, NSError *error)
    {
        if (error)
        {
            //kCLErrorLocationUnknown
            //连接异常 8
            NSLog(@"locError:{%ld - %@};", (long)error.code, error.localizedDescription);
            
            //如果为定位失败的error，则不进行后续操作
            if (error.code == AMapLocationErrorLocateFailed)
            {
                [__locationDelegate onLocationFailWithCode:error.code description:@"定位错误，请检查是否已经打开定位权限"];
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
            [__locationDelegate onLocationSuccess:info];
            
            
            
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
        [_locationManager setAllowsBackgroundLocationUpdates:YES];
        
        //设置定位超时时间
        [_locationManager setLocationTimeout:DefaultLocationTimeout];
        
        //设置逆地理超时时间
        [_locationManager setReGeocodeTimeout:DefaultReGeocodeTimeout];

    }
    //进行单次带逆地理定位请求
    [_locationManager requestLocationWithReGeocode:YES completionBlock:self.completionBlock];

}
/**
 *  当定位发生错误时，会调用代理的此方法。
 *
 *  @param manager 定位 AMapLocationManager 类。
 *  @param error 返回的错误，参考 CLError 。
 */
- (void)amapLocationManager:(AMapLocationManager *)manager didFailWithError:(NSError *)error{
    NSLog(@"%@",error);
}

/**
 *  连续定位回调函数
 *
 *  @param manager 定位 AMapLocationManager 类。
 *  @param location 定位结果。
 */
- (void)amapLocationManager:(AMapLocationManager *)manager didUpdateLocation:(CLLocation *)location{
    
}

/**
 *  定位权限状态改变时回调函数
 *
 *  @param manager 定位 AMapLocationManager 类。
 *  @param status 定位权限状态。
 */
- (void)amapLocationManager:(AMapLocationManager *)manager didChangeAuthorizationStatus:(CLAuthorizationStatus)status{
    
}

@end
