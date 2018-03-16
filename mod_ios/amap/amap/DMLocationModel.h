//
//  LocationModel.h
//  test
//
//  Created by 任雪亮 on 16/9/26.
//  Copyright © 2016年 任雪亮. All rights reserved.
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


@interface DMLocationModel : NSObject
-(void)startLocation;
@property (nonatomic,weak) id<DMLocationDelegate> delegate;
@end
