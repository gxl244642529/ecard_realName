//
//  ICache.h
//  ecard
//
//  Created by randy ren on 14-4-27.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol ICache <NSObject>

//直接放入,
-(BOOL)put:(NSString*)key value:(NSObject*)value;
//更新缓存时间
-(void)updateExpire:(NSString*)key;
//如果过期，则返回空
-(NSObject*)get:(NSString*)key;
-(NSObject*)getIgnoreTimeout:(NSString*)key;
-(BOOL)contains:(NSString*)key;
//是否过期,此方法只能在get之后调用，判断本次取出的内容是否过期
-(BOOL)isTimeout;
-(void)clear;
-(void)remove:(NSString*)key;
@end
