//
//  DiskCache.m
//  libs
//
//  Created by randy ren on 16/1/8.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMDiskCache.h"
#import "DMCacheUtil.h"
@interface DMDiskCache()
{
    //缓存目录
    NSString* _cacheDir;
}

@end

@implementation DMDiskCache

-(void)dealloc{
    _cacheDir = nil;
}

-(id)init{
    if(self = [super init]){
        _cacheDir = [DMCacheUtil getPath:@"cache"];
    }
    return  self;
}

-(void)put:(NSString*)key value:(NSData*)value{
    [value writeToFile:[_cacheDir stringByAppendingPathComponent:key] atomically:YES];
}
-(void)remove:(NSString*)key{
    [[NSFileManager defaultManager] removeItemAtPath:[_cacheDir stringByAppendingPathComponent:key] error:nil];
    
}
-(id)get:(NSString*)key{
    NSString* path =[_cacheDir stringByAppendingPathComponent:key];
    
    return [[NSFileManager defaultManager]contentsAtPath:path];
    
}

-(void)clearCache:(NSString*)prefix{
    prefix = [prefix stringByReplacingOccurrencesOfString:@"/" withString:@"_"];
    //列出文件
    
    NSFileManager* manager = [NSFileManager defaultManager];
    NSDirectoryEnumerator* dirEnum = [manager enumeratorAtPath:_cacheDir];
   
    NSString* path;
    while ((path = [dirEnum nextObject]) != nil)
    {
        if([path hasPrefix:prefix]){
            [manager removeItemAtPath:[_cacheDir stringByAppendingPathComponent:path] error:nil];
        }
        
    }
    
}

#pragma Private
-(BOOL)exists:(NSString*)filePath{
    BOOL isDir;
    if ([[NSFileManager defaultManager] fileExistsAtPath:filePath isDirectory:&isDir] && !isDir){
        return TRUE;
    }
    return FALSE;
}



-(DMCacheResult)check:(NSString*)key expire:(NSObject<DMCacheExpire>*)expire{
    NSString* path =[_cacheDir stringByAppendingPathComponent:key];
    
    if(![self exists:path]){
        return DMCacheResult_Empty;
    }
    
    
    NSDictionary* dic = [[NSFileManager defaultManager]attributesOfItemAtPath:path error:NULL];
    NSDate* date = [dic valueForKey:NSFileModificationDate];
    
    if([expire isExpire:[date timeIntervalSince1970]]){
        return DMCacheResult_Expire;
    }
    return DMCacheResult_Found;
}

-(void)clear{
    
    [[NSFileManager defaultManager] removeItemAtPath:_cacheDir error:nil];
    [[NSFileManager defaultManager] createDirectoryAtPath:_cacheDir
                              withIntermediateDirectories:YES
                                               attributes:nil
                                                    error:NULL];
}

@end
