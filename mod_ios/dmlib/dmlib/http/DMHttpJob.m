//
//  HttpRequest.m
//  core
//
//  Created by randy ren on 15/12/28.
//  Copyright © 2015年 damai. All rights reserved.
//

#import "DMHttpJob.h"
#import "CommonUtil.h"


@interface DMHttpJob()
{
   
}

@end

@implementation DMHttpJob


-(id)init{
    if(self = [super init]){
       // _timeout = 5;
       
    }
    return self;
}

-(void)dealloc{
#ifdef DEBUG
    NSLog(@"Http %@ dealloc",_url);
#endif
    _cacheKey = nil;
    self.url = nil;
    self.data = nil;
    self.error = nil;
}
-(void)clear{
    _cacheKey = nil;
    _isCanceled = NO;
    self.url = nil;
    self.data = nil;
    self.error = nil;

    
    self.extra = nil;
    self.delegate = nil;
}

-(NSString*)cacheKey{
    if(!_cacheKey){
        _cacheKey = [self makeCacheKey];
    }
    return _cacheKey;
}
-(NSString*)makeCacheKey{
    return [CommonUtil md5:self.url];
}


@end
