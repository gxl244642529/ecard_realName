//
//  DiskCache.m
//  ecard
//
//  Created by randy ren on 14-4-27.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "DiskCache.h"
#import <ecardlib/ecardlib.h>
@interface DiskCache()
{
    //是否过期
    BOOL _isTimeOut;
    //缓存目录
     NSString* _cacheDir;
    //文件目录
    NSString* _filePath;
}
@end

@implementation DiskCache

-(void)dealloc{
    _cacheDir = NULL;
    _filePath = NULL;
}
-(void)remove:(NSString*)key{
     [[NSFileManager defaultManager] removeItemAtPath:[_cacheDir stringByAppendingPathComponent:key] error:nil];
}
-(id)init{
    if(self=[super init]){
        _cacheDir = [CacheUtil getPath:@"cache"];
    }
    return self;
}

//直接放入,
-(BOOL)put:(NSString*)key value:(NSData*)value
{
    return [value writeToFile:[_cacheDir stringByAppendingPathComponent:key] atomically:YES];
}
//更新缓存时间
-(void)updateExpire:(NSString*)key{
    
    NSString* filePath=[_cacheDir stringByAppendingPathComponent:key];
     NSError* error;
    NSDictionary* attrs = [[NSDictionary alloc]initWithObjectsAndKeys:[NSDate date],NSFileModificationDate, nil];
    [[NSFileManager defaultManager]setAttributes:attrs ofItemAtPath:filePath error:&error];
    if(error){
        NSLog(@"%@",error);
    }
}
//如果过期，则返回空
-(NSData *)get:(NSString*)key{
    NSString* path =[_cacheDir stringByAppendingPathComponent:key];
    
    if(![self exists:path]){
        return NULL;
    }
   
    //是否过期
    _isTimeOut = [self isTimeout:path];
    if(_isTimeOut){
        return NULL;
    }
    return [[NSFileManager defaultManager]contentsAtPath:path];
    //return [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];;
}
-(NSData *)getIgnoreTimeout:(NSString*)key{
    NSString* path =[_cacheDir stringByAppendingPathComponent:key];
    
    if(![self exists:path]){
        return NULL;
    }
    NSData *data = [[NSFileManager defaultManager]contentsAtPath:path];
    return data;
    
}
-(BOOL)contains:(NSString*)key{
    return [self exists:[_cacheDir stringByAppendingPathComponent:key]];
}
//是否过期,此方法只能在get之后调用，判断本次取出的内容是否过期
-(BOOL)isTimeout{
    return _isTimeOut;
}

-(void)clear{
    //清空目录
    
    [[NSFileManager defaultManager] removeItemAtPath:_cacheDir error:nil];
    [[NSFileManager defaultManager] createDirectoryAtPath:_cacheDir
                              withIntermediateDirectories:YES
                                               attributes:nil
                                                    error:NULL];
    
}


#pragma Private
-(BOOL)exists:(NSString*)filePath{
    BOOL isDir;
    if ([[NSFileManager defaultManager] fileExistsAtPath:filePath isDirectory:&isDir] && !isDir){
        return TRUE;
    }
    return FALSE;
}


-(BOOL)isTimeout:(NSString*)file{
    NSDictionary* dic = [[NSFileManager defaultManager]attributesOfItemAtPath:file error:NULL];
    NSDate* date = [dic valueForKey:NSFileModificationDate];
    NSTimeInterval timeBetween = [[NSDate date] timeIntervalSinceDate:date];
    return timeBetween > 3 * 3600;
}






@end
