//
//  IJsonTaskManager.h
//  randycommonlibs
//
//  Created by randy ren on 14-7-20.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//
#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <DMLib/dmlib.h>
#import <ecardlib/ecardlib.h>
#import "ObjectJsonTask.h"
#import "ArrayJsonTask.h"




@protocol OnResumeUserInfo <NSObject>

-(void)onSetUserInfo:(ECardUserInfo*)data;

@end


/**
 *  创建http json接口访问类
 */
@protocol IJsonTaskManager <NSObject>

-(void)setImageSrcDirect:(UIImageView*)imageView src:(NSString*)url;
-(void)setImageSrcSync:(UIImageView*)imageView src:(NSString*)url;


-(void)saveToCache:(NSData*)data url:(NSString*)url;

-(void)loadImage:(NSString*)url listener:(NSObject<DMJobDelegate>*)listener;
/**
 *  清空缓存
 */
-(void)clearCache;

/**
 *  是否已经登录
 *
 *  @return <#return value description#>
 */
-(BOOL)isLogin;
-(NSString*)getPushID;
-(NSString*)getDeviceID;
/**
 *  设置基础url
 *
 *  @param baseUrl <#baseUrl description#>
 */
-(void)setImageSrc:(UIImageView*)imageView src:(NSString*)url;

/**
 *  注销
 */
-(void)logout;


-(ECardUserInfo*)userInfo;

-(ArrayJsonTask*)createArrayJsonTask:(NSString*)api cachePolicy:(CachePolicy)cachePolicy isPrivate:(BOOL)isPrivate;

-(ObjectJsonTask*)createObjectJsonTask:(NSString*)api cachePolicy:(CachePolicy)cachePolicy;


-(ArrayJsonTask*)createArrayJsonTask:(NSString*)api cachePolicy:(CachePolicy)cachePolicy isPrivate:(BOOL)isPrivate factory:(NSInteger)factory;
-(ObjectJsonTask*)createObjectJsonTask:(NSString*)api cachePolicy:(CachePolicy)cachePolicy factory:(NSInteger)factory;



-(void)checkLogin:(id)sender parent:(UIViewController*)parent loginSuccess:(SEL)loginSuccess;
-(void)checkLogin:(UIViewController*)parent loginSuccess:(SEL)loginSuccess;


@end
