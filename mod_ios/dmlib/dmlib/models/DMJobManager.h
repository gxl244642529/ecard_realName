//
//  TaskManager.h
//  libs
//
//  Created by randy ren on 16/1/6.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "DMApiJob.h"
#import "DMViewController.h"
#import "DataParser.h"
#import "DMPayModel.h"
#import "DMLoginCaller.h"
#import "DMCache.h"
#import "DMApiParser.h"
#import "DMApiHandler.h"




@class DMPayModel;


@protocol DMServerRegisterDelegate <NSObject>

-(NSArray*)registerServerHandler:(DMApiHandler*)handler parsers:(NSArray<id<DMApiParser>>*)parsers delegate:(id<DMApiDelegate>) delegate cache:(id<DMCache>)cache;

@end



@interface UIImageView (DMHttpJob)

@property (readwrite,retain,strong) DMHttpJob* job;

@end



@interface DMJobManager : NSObject<DMJobFinish,DMLoginCaller>
+(DMJobManager*)sharedInstance;

-(id)initWithRegisterServerHandler:(id<DMServerRegisterDelegate>)delegate;

-(DMPayModel*)createPayModel:(NSString*)moduleName supportTypes:(NSArray<NSNumber*>*)supportTypes;

-(void)loadImage:(NSString*)url image:(UIImageView*)image;
-(void)loadImage:(NSString*)url delegate:(id<DMJobDelegate>)delegate;

-(void)getJson:(NSString*)url delegate:(id<DMJobDelegate>)delegate;
-(void)get:(NSString*)url delegate:(id<DMJobDelegate>)delegate;
-(void)postJson:(NSString*)url data:(NSDictionary*)data delegate:(id<DMJobDelegate>)delegate;

-(DMApiJob*)createArrayJsonTask:(NSString*)api cachePolicy:(DMCachePolicy)cachePolicy server:(NSInteger)server;
-(DMApiJob*)createPageJsonTask:(NSString*)api cachePolicy:(DMCachePolicy)cachePolicy server:(NSInteger)server;
-(DMApiJob*)createJsonTask:(NSString*)api cachePolicy:(DMCachePolicy)cachePolicy server:(NSInteger)server;
-(NSString*)wxId;
-(DMViewController*)createPayCashier:(Class)cashierControllerClass;
-(void)executeApi:(DMApiJob*)task;
-(void)addApiTask:(DMApiJob*)task;
-(void)addNetTask:(DMApiJob*)task;
-(void)clearCache:(DMApiJob*)task;
-(void)start;
-(void)stop;
-(void)clearCache;

-(void)logout;

-(void)onDestroy:(DMViewController*)controller;

-(void)registerServer:(NSInteger)index url:(NSString*)url;

-(void)registerSuccessListener:(id<DMJobSuccess>)successListener forType:(NSInteger)type;

-(void)registerParser:(id<DataParser>)parser forType:(NSInteger)type;


-(NSString*)deviceID;
-(NSString*)pushID;
//根
@property (nonatomic,weak) UIViewController* rootViewController;
@property (nonatomic,weak) id<DMLoginCaller> loginCaller;
-(DMViewController*)topController;

@property (nonatomic,weak) id<DMPayFactory> factory;




#pragma override


@end
