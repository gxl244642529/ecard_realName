//
//  DMViewDecoder.m
//  DMLib
//
//  Created by randy ren on 16/2/23.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMViewDecoder.h"

/*
 DMCachePolicy_NoCache,
 DMCachePolicy_TimeLimit,
 DMCachePolicy_Permanent,
 DMCachePolicy_UseCacheFirst,
 DMCachePolicy_Offline

 */
@implementation DMViewDecoder
+(DMCachePolicy)cachePolicy:(NSString*)cachePolicy{
    if(!cachePolicy){
        return DMCachePolicy_NoCache;
    }
    if([cachePolicy isEqualToString:@"noCache"]){
        return DMCachePolicy_NoCache;
    }else if([cachePolicy isEqualToString:@"permanent"]){
         return DMCachePolicy_Permanent;
    }else if([cachePolicy isEqualToString:@"timeLimit"]){
         return DMCachePolicy_TimeLimit;
    }else if([cachePolicy isEqualToString:@"useCacheFirst"]){
         return DMCachePolicy_UseCacheFirst;
    }else if([cachePolicy isEqualToString:@"offline"]){
         return DMCachePolicy_Offline;
    }
    NSLog(@"Cache policy value must be one of noCache,permanent,timeLimit,useCacheFirst,offline, you must check view settings, the value is noCache default");
    return DMCachePolicy_NoCache;
}




@end
