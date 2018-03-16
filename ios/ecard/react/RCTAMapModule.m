//
//  RCTPositionModule.m
//  ebusiness
//
//  Created by 任雪亮 on 16/12/3.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import "RCTAMapModule.h"

#import <amap/amap.h>

/*
#import <MAMapKit/MAMapKit.h>
#import <AMapSearchKit/AMapSearchAPI.h>
#import <AMapFoundationKit/AMapFoundationKit.h>
#import <AMapSearchKit/AMapCommonObj.h>
#import <AMapLocationKit/AMapLocationManager.h>
*/
@interface RCTAMapModule()//<AMapLocationManagerDelegate>
{
  //AMapLocationManager* locationManager;
 
}

@property (nonatomic,copy) RCTResponseSenderBlock success;
@property (nonatomic,copy) RCTResponseSenderBlock error;

@end

@implementation RCTAMapModule

-(id)init{
  if(self = [super init]){
    /* locationManager = [[AMapLocationManager alloc] init];
      locationManager.delegate  = self;
    [locationManager setPausesLocationUpdatesAutomatically:NO];
     */
  }
  return self;
}

RCT_EXPORT_MODULE();


RCT_EXPORT_METHOD(getPosition:(RCTResponseSenderBlock)success){
    _success = success;
   //[locationManager startUpdatingLocation];
  
  [DMMapUtil getPosition:^(DMLocationInfo * info) {
    success(
      @[
        
        @{
          @"flag":@0,
          @"lat": [NSNumber numberWithDouble: info.lat ],
          @"lng":[NSNumber numberWithDouble: info.lng]
          }

        
        ]
    
    );
  }];
  
}

RCT_EXPORT_METHOD(getDistance:(NSDictionary*)opts
                  callback:(RCTResponseSenderBlock)callback){
  
  
  NSNumber* lat = opts[@"lat"];
  NSNumber* lng = opts[@"lng"] ;
  NSNumber* targetLat = opts[@"targetLat"] ;
  NSNumber* targetLng = opts[@"targetLng"] ;

  
 double distance= [DMMapUtil getDistance:[lat doubleValue] lng:[lng doubleValue] targetLat:[targetLat doubleValue] targetLng:[targetLng doubleValue]];
  
  callback(@[ [NSNumber numberWithDouble:distance] ]);

}
@end
