//
//  JsonTask.m
//  eCard
//
//  Created by randy ren on 14-2-20.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "JsonTask.h"


@interface JsonTask ()
{

    NSString* _err;
    __weak NSObject<IRequestError>* _errorDelegate;
}



@end
@implementation JsonTask
-(void)setWaitMessage:(NSString*)wait{
    [_job setWaitingMessage:wait];
}
-(void)clearCache{
    [[DMJobManager sharedInstance]clearCache:_job];
}
-(void)setJob:(DMApiJob*)job{
    _job = job;
    _job.delegate = self;
}
-(void)clearParam{
    [_job.param removeAllObjects];
}
-(void)addFile:(id)file{
    [_job put:@"0" value:file];
}

-(void)setType:(NSInteger)type{
    _job.server = type;
}
-(void)setClass:(Class)cls{
    _job.clazz = cls;
}
-(void)dealloc
{
    [_job cancel];
    _job = nil;
    
}

-(BOOL)jobError:(DMApiJob*)request{
    [_errorDelegate task:self error:request.error.reason isNetworkError:request.error.code == DMErrorType_Network];
    return YES;
}

-(BOOL)isCancel{
    return _job.isCanceled;
}



-(id)get:(NSString*)key{
    return _job.param[key];
}

-(NSDictionary*)params{
    return _job.param;
}



-(void)setCachePolicy:(CachePolicy)cachePolicy{
    [_job setCachePolicy:cachePolicy];
}
/*
-(void)setUrl:(NSString*)url{
    [_request setUrl:url];
}*/
-(void)cancel{
    [_job cancel];
}


-(void)setCancel:(BOOL)cancel{
    
    
    
}
-(void)reload{
    [_job reload];
}


-(void)execute{
    [_job execute];
}
-(void)setErrorDelegate:(NSObject<IRequestError>*)listener{
    _errorDelegate = listener;
}

//数据
-(NSData*)body{
    return nil;
}
-(void)enableAutoNotify{
  
}
-(void)put:(NSString*)key value:(id)data
{
    [_job put:key value:data];
}
-(void)putAll:(NSDictionary*)dic{
    [_job putAll:[dic mutableCopy]];
}

-(void)setUserID:(NSString*)userID{
   
}



-(CryptType)crypt{
    return _job.crypt;
}
-(void)setCrypt:(CryptType)type{
    _job.crypt = type;
}

-(void)setApi:(NSString*)api{
    [_job setApi:api];
}
-(NSString*)api{
    return _job.api;
}


-(NSString*)getCacheKey{
    return _job.cacheKey;
}



@end
