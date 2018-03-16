//
//  CommonUtil.h
//  randycommonlibs
//
//  Created by randy ren on 14-7-20.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
@interface CommonUtil : NSObject


+(NSString*)encodeURL:(NSString *)string;
/**
 *  md5值
 *
 *  @param input <#input description#>
 *
 *  @return <#return value description#>
 */
+(NSString*)md5:(NSString*)input;

+ (NSString *)sha1:(NSString *)input;

+ (NSString *)getIPAddress:(BOOL)preferIPv4;

+ (NSDictionary *)getIPAddresses;
/**
 *  是否长屏幕
 *
 *  @return <#return value description#>
 */
+(BOOL)isLongScreen;
/**
 *  下载app
 *
 *  @param appID <#appID description#>
 */
+(void)downloadApp:(NSString*)appID;


/**
 *  检查是否有安装app，并打开，如没有安装，去appstore下载
 *
 *  @param url   <#url description#>
 *  @param appID <#appID description#>
 */
+(void)checkAndOpenApp:(NSString*)url appID:(NSString*)appID;

/**
 * 检查是否安装app,如有安装打开,否则打开指定网址
 */
+(BOOL)checkAndOpenApp:(NSString*)appUrl url:(NSString*)url parent:(UIViewController*)parent;

+(void)makePhoneCall:(NSString*)phoneNumber parent:(UIView*)parent;


//查找对应的controller
+(__kindof UIViewController*)findController:(UINavigationController*)nav clazz:(Class)clazz;
+(__kindof UIViewController*)findPrevController:(UINavigationController*)nav clazz:(Class)clazz;
+(__kindof UIViewController*)findPrevController:(UIViewController*)controller;
@end
