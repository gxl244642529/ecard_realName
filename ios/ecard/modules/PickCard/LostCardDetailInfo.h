//
//  LostCardDetailInfo.h
//  ecard
//
//  Created by randy ren on 14-5-18.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface LostCardDetailInfo : NSObject

@property (nonatomic) int sex;
@property (nonatomic) int vry_time;
@property (nonatomic) int vry_id;
@property (nonatomic) int status;

@property (nonatomic,retain) NSString* info;
@property (nonatomic,retain) NSString* phone;

@property (nonatomic,retain) NSString* vry_error;
@property (nonatomic,retain) NSString* vry_result;

@property (nonatomic,retain) NSString* img;
@property (nonatomic,retain) NSString* cardID;
@property (nonatomic,retain) NSString* time;


@end
