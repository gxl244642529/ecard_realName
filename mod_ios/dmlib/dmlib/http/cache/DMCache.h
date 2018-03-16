//
//  Cache.h
//  libs
//
//  Created by randy ren on 16/1/8.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol DMCacheExpire <NSObject>

-(BOOL)isExpire:(NSTimeInterval)time;

@end

typedef enum {
    DMCacheResult_Empty,
    DMCacheResult_Expire,
    DMCacheResult_Found
}DMCacheResult;

@protocol DMCache <NSObject>

-(void)put:(NSString*)key value:(id)value;

-(id)get:(NSString*)key;

-(DMCacheResult)check:(NSString*)key expire:(NSObject<DMCacheExpire>*)expire;

-(void)clear;

-(void)remove:(NSString*)key;

-(void)clearCache:(NSString*)prefix;

@end
