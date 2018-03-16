//
//  LocalData.h
//  ecard
//
//  Created by randy ren on 15/8/31.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>



@interface LocalData : NSObject

/**
 *  只保存一个value
 *
 *  @param key   <#key description#>
 *  @param value <#value description#>
 */
+(void)setStringValue:(NSString*)value forKey:(id)key;

+(void)setIntegerValue:(NSInteger)value forKey:(id)key;

+(NSString*)stringValue:(NSString*)key defaultValue:(NSString*)defaultValue;


+(NSInteger)integerValue:(NSString*)key defaultValue:(NSInteger)defaultValue;

@end
