//
//  CachePolicy.h
//  libs
//
//  Created by randy ren on 16/1/9.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>
typedef enum{
    DMCachePolicy_NoCache,
    DMCachePolicy_TimeLimit,
    DMCachePolicy_Permanent,
    DMCachePolicy_UseCacheFirst,
    DMCachePolicy_Offline
    
}DMCachePolicy;
