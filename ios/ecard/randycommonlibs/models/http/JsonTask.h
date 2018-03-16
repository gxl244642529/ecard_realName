//
//  JsonTask.h
//  eCard
//
//  Created by randy ren on 14-2-20.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>

#import <DMLib/DMLib.h>

#import "ISystemJsonTask.h"
#import "ICache.h"

#import "INotLoginListener.h"
#import "IRequestError.h"
#import "IJsonTask.h"

#define RESULT @"result";


typedef DMCachePolicy CachePolicy;




@interface JsonTask : NSObject<DMJobDelegate>
{
        DMApiJob* _job;
}

-(void)setClass:(Class)cls;
-(void)clearCache;

-(void)setJob:(DMApiJob*)job;

-(void)put:(NSString*)key value:(id)value;
-(void)setWaitMessage:(NSString*)wait;
-(void)clearParam;
-(void)putAll:(NSDictionary*)data;

-(void)setCachePolicy:(CachePolicy)cachePolicy;
//-(void)setUrl:(NSString*)url;
-(void)reload;
-(void)setErrorDelegate:(NSObject<IRequestError>*)listener;
-(void)execute;

-(NSString*)api;

-(void)enableAutoNotify;

-(id)get:(NSString*)key;
-(BOOL)isCancel;

-(void)cancel;
-(void)setCancel:(BOOL)cancel;
-(void)addFile:(id)file;

-(void)setType:(NSInteger)type;

-(CryptType)crypt;
-(void)setCrypt:(CryptType)type;

@property (nonatomic,readwrite,copy)  void(^errorListener)(NSString* error,BOOL isNetworkError);

@property (nonatomic) BOOL isPadding;



@end