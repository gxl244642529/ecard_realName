//
//  DMModel.m
//  ecard
//
//  Created by 任雪亮 on 16/2/28.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "DMModel.h"
#import "DMJobManager.h"

@interface DMModel()
{
    NSMutableDictionary* _apis;
}

@end

@implementation DMModel

-(id)init{
    if(self = [super init]){
        _apis = [[NSMutableDictionary alloc]init];
    }
    return self;
}

-(void)dealloc{
    if(_apis){
        for (NSString* key in _apis.allKeys) {
            DMApiJob* job = _apis[key];
            [job cancel];
        }
        [_apis removeAllObjects];
    }
}

-(DMApiJob*)createApi:(NSString*)api{
    DMApiJob* job =[_apis objectForKey:api];
    if(!job){
        job =[[DMJobManager sharedInstance]createJsonTask:api cachePolicy:DMCachePolicy_NoCache server:1];
        _apis[api] = job;
    }
    return job;
}
@end
