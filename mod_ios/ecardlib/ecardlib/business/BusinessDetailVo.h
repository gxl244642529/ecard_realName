//
//  BusinessDetailVo.h
//  ecard
//
//  Created by randy ren on 16/2/23.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>

#import <DMLib/DMLib.h>

@interface BusinessDetailVo : NSObject<IJsonValueObject>

@property (nonatomic,copy) NSString* title;
@property (nonatomic,copy) NSString* phone;
@property (nonatomic,copy) NSString* addr;
@property (nonatomic,copy) NSString* detail;
@property (nonatomic,copy) NSString* img1;
@property (nonatomic,copy) NSString* img2;
@property (nonatomic,copy) NSString* img3;

@property (nonatomic,copy) NSString* count;
@property (nonatomic,assign) double lat;
@property (nonatomic,assign) double lng;


@end
