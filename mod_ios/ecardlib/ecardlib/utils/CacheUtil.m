//
//  CacheUtil.m
//  ecard
//
//  Created by randy ren on 14-9-27.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "CacheUtil.h"

@implementation CacheUtil
+(BOOL)isExpireSince:(NSDate*)begin hours:(int)hours{
    if(!begin)return TRUE;
    NSTimeInterval timeBetween = [[NSDate date] timeIntervalSinceDate:begin];
    return timeBetween > hours * 3600;
}
+(BOOL)isExpire:(double)begin hours:(int)hours{
    
    return [[NSDate alloc]init].timeIntervalSince1970 - begin > hours * 3600;
}
+(NSString*)getPath:(NSString*)name{
    
    NSString* cacheDir = [NSHomeDirectory() stringByAppendingPathComponent:@"Documents"];
    cacheDir=[cacheDir stringByAppendingPathComponent:name];
    // 创建文件管理器
    NSFileManager *fileMgr = [NSFileManager defaultManager];
    BOOL isDir;
    if (![fileMgr fileExistsAtPath:cacheDir isDirectory:&isDir] || !isDir)
    {
        NSError* error;
        [fileMgr createDirectoryAtPath:cacheDir withIntermediateDirectories:NO attributes:NULL error:&error]         ;
        if(error)
        {
            NSLog(@"Create directory failed");
        }
    }
    
    return cacheDir;
}
@end
