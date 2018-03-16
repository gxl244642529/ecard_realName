//
//  JsonUtil.m
//  ecard
//
//  Created by randy ren on 15/5/13.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "JsonUtil.h"

@implementation JsonUtil

+(double)getDouble:(NSDictionary*)dic key:(NSString*)key{
    id value = [dic objectForKey:key];
    if(!value)return 0;
    if([value isKindOfClass:[NSNull class]])return 0;
    return [value doubleValue];
}
+(NSInteger)getInteger:(NSDictionary*)dic key:(NSString*)key{
    id value = [dic objectForKey:key];
    if(!value)return 0;
    if([value isKindOfClass:[NSNull class]])return 0;
    return [value integerValue];
}
+(NSString*)getString:(NSDictionary*)dic key:(NSString*)key defaultValue:(NSString*)defaultValue{
    id value = [dic objectForKey:key];
    if(!value)return defaultValue;
    if([value isKindOfClass:[NSNull class]])return defaultValue;
    if([value isKindOfClass:[NSString class]])return value;
    return [NSString stringWithFormat:@"%@",value];
}
+(NSString*)getString:(NSDictionary*)dic key:(NSString*)key{
    id value = [dic objectForKey:key];
    if(!value)return nil;
    if([value isKindOfClass:[NSNull class]])return nil;
    if([value isKindOfClass:[NSString class]])return value;
    return [NSString stringWithFormat:@"%@",value];
}
@end
