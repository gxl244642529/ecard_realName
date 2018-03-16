//
//  CacheHandler.m
//  libs
//
//  Created by randy ren on 16/1/7.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMCacheHandler.h"
#import "DMHttpJob.h"
#import "NetworkStatus.h"
#import "DMCacheUtil.h"
#import "DMJobQueue.h"
#import "ServerManager.h"
#import "DMCache.h"
#import "DMApiJob.h"



static double g_3g_timeout = 3 * 60 * 60;
static double g_wifi_timeout = 30 * 60;

////////////////////////////////////
@interface DMCache3GExpire : NSObject<DMCacheExpire>
@end


@implementation DMCache3GExpire
-(BOOL)isExpire:(NSTimeInterval)time{
    return [[NSDate date]timeIntervalSince1970] - time > g_3g_timeout;
}
@end

////////////////////////////////////
@interface DMCacheWifiExpire : NSObject<DMCacheExpire>
@end


@implementation DMCacheWifiExpire
-(BOOL)isExpire:(NSTimeInterval)time{
    return [[NSDate date]timeIntervalSince1970] - time > g_wifi_timeout;
}
@end

////////////////////////////////////
@interface DMCachePermanantExpire : NSObject<DMCacheExpire>
@end


@implementation DMCachePermanantExpire
-(BOOL)isExpire:(NSTimeInterval)time{
    return false;
}
@end

@interface DMCacheHandler()
{
    
    
    id<DMCacheExpire> _cache3g;
    id<DMCacheExpire> _cacheWifi;
    id<DMCacheExpire> _cachePremanant;
    
    
    __weak DMJobQueue* _networkQueue;
    __weak NSObject<DMCache>* _cache;
    __weak ServerStatusMoniter* _moniter;
    __weak NSArray<id<DataParser>>* _parsers;
    __weak NSArray<id<DMJobSuccess>>* _successListeners;
    __weak DMJobQueue* _apiQueue;
}

@end

@implementation DMCacheHandler

-(void)dealloc{
    _cache3g = nil;
    _cacheWifi = nil;
    _cachePremanant = nil;
}

-(id)initWithNetQueue:(DMJobQueue*)networkQueue apiQueue:(DMJobQueue*)apiQueue cache:(NSObject<DMCache>*)cache parsers:(NSArray<id<DataParser>>*)parsers moniter:(ServerStatusMoniter*)moniter  successListeners:(NSArray<id<DMJobSuccess>>*)successListeners{
    if(self = [super init]){
        _apiQueue = apiQueue;
        _successListeners  =successListeners;
        _networkQueue = networkQueue;
        _cache = cache;
        _cache3g = [[DMCache3GExpire alloc]init];
        _cachePremanant = [[DMCachePermanantExpire alloc]init];
        _cacheWifi = [[DMCacheWifiExpire alloc]init];
        _parsers =parsers;
        _moniter = moniter;
    }
    return self;
}

-(void)handleTask:(DMHttpJob*)task index:(NSInteger)index{
    NetworkStatus netStatus = _moniter.status;
    NSString* key = [task cacheKey];
    
#ifdef DEBUG
    NSLog(@"Cache %@",key);
#endif
    
    switch (task.cachePolicy) {
        case DMCachePolicy_TimeLimit:
        {
            id<DMCacheExpire> cacheExpire;
            if(netStatus == ReachableViaWWAN){
                cacheExpire = _cache3g;
            }else{
                cacheExpire = _cacheWifi;
            }
            
            DMCacheResult result = [_cache check:key expire:cacheExpire];
            if(result == DMCacheResult_Found && [self parseResult:task content:[_cache get:key]]){
                return;
            }
            [self addToQueue:task];
        }
            break;
        case DMCachePolicy_Permanent:
        {
            NSData* content = [_cache get:key];
            if(content && [self parseResult:task content:content]   ){
                return;
            }
            [self addToQueue:task];
        }
            break;
        case DMCachePolicy_UseCacheFirst:
        {
            
            id<DMCacheExpire> cacheExpire;
            if(netStatus == ReachableViaWWAN){
                cacheExpire = _cache3g;
            }else{
                cacheExpire = _cacheWifi;
            }
            
            DMCacheResult result = [_cache check:key expire:cacheExpire];
            if(DMCacheResult_Empty == result){
                [self addToQueue:task];
                return;
            }
            
            NSData* data = [_cache get:key];
            
            if(![self parseResult:task content:data]){
                [self addToQueue:task];
                return;
            }
            
            if(DMCacheResult_Expire == result){
                [self addToQueue:task];
            }
            
        }
            break;
            
        case DMCachePolicy_Offline:
        {
            NSData* content = [_cache get:key];
            if(content && [self parseResult:task content:content]){
                return;
            }
            
            if(netStatus != ReachableViaWiFi){
                return;
            }
            
            [self addToQueue:task];
        }
            break;
        default:
        {
            [self addToQueue:task];
        }
            break;
    }

}

-(void)addToQueue:(DMHttpJob*)task{
    if([task isCanceled]){
        return;
    }
    if([task isKindOfClass:[DMApiJob class]]){
        [_apiQueue addTask:task];
    }else{
        [_networkQueue addTask:task];
    }
    
}

-(BOOL)parseResult:(DMHttpJob*)request content:(NSData*)content{
    NSError* error = nil;
    id result = [_parsers[request.dataType]parseData:request data:content error:&error];
    if(error || !result){
        
#ifdef DEBUG
        NSLog(@"Cache parse error %@",request.cacheKey);
#endif
        return NO;
    }
    //这里成功,交给成功的处理
    request.data = result;
#ifdef DEBUG
    NSLog(@"Cache parse success %@",request.cacheKey);
#endif
    [_successListeners[request.type]jobSuccess:request];
    return YES;
}


-(void)stop{
    
}

@end
