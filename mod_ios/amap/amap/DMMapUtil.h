//
//  DMMapUtil.h
//  amap
//
//  Created by 任雪亮 on 17/1/18.
//  Copyright © 2017年 任雪亮. All rights reserved.
//

#import <Foundation/Foundation.h>


#define DefaultLocationTimeout 5
#define DefaultReGeocodeTimeout 5
@interface DMLocationInfo : NSObject

@property (nonatomic,assign) double lat;
@property (nonatomic,assign) double lng;
//地址
@property (nonatomic,copy) NSString* address;
//详细地址
@property (nonatomic,copy) NSString* detailAddress;

@end
@protocol DMLocationDelegate <NSObject>

-(void)onLocationSuccess:(DMLocationInfo*)info;

-(void)onLocationFailWithCode:(NSInteger)code description:(NSString*)description;

@end


@interface DMMapUtil : NSObject

+(void)setAppKey:(NSString*)key;

+(void)getAddress:(double)lat lng:(double)lng ;

+(double)getDistance:(double)lat lng:(double)lng targetLat:(double)targetLat targetLng:(double)targetLng;


+(void)getPosition: (void (^)(DMLocationInfo*)) callback;

@end
