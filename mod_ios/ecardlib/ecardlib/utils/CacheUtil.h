//
//  CacheUtil.h
//  ecard
//
//  Created by randy ren on 14-9-27.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface CacheUtil : NSObject
+(BOOL)isExpireSince:(NSDate*)begin hours:(int)hours;

+(BOOL)isExpire:(double)begin hours:(int)hours;

+(NSString*)getPath:(NSString*)name;
@end
