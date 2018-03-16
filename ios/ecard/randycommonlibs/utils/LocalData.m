//
//  LocalData.m
//  ecard
//
//  Created by randy ren on 15/8/31.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "LocalData.h"

@implementation LocalData
+(void)setStringValue:(NSString*)value forKey:(id)key{
    NSUserDefaults* def = [NSUserDefaults standardUserDefaults];
    [def setValue:value forKey:key];
}
+(void)setIntegerValue:(NSInteger)value forKey:(id)key{
    NSUserDefaults* def = [NSUserDefaults standardUserDefaults];
    [def setValue:[NSNumber numberWithInteger:value] forKey:key];
}
+(NSString*)stringValue:(NSString*)key defaultValue:(NSString*)defaultValue{
    NSUserDefaults* def = [NSUserDefaults standardUserDefaults];
    id value = [def valueForKey:key];
    if(value){
        return value;
    }
    return defaultValue;
}


+(NSInteger)integerValue:(NSString*)key defaultValue:(NSInteger)defaultValue{
    NSUserDefaults* def = [NSUserDefaults standardUserDefaults];
    id value = [def valueForKey:key];
    if(value){
        return [value integerValue];
    }
    return defaultValue;
}
@end
