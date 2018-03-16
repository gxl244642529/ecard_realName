//
//  DMAccounts.h
//  DMLib
//
//  Created by randy ren on 16/1/13.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "DMLoginController.h"
#import "DMLoginCaller.h"
#import "DMNotifier.h"

//登录信息
@protocol DMLoginErrorListener <NSObject>

-(void)onLoginFail:(__weak id<DMLoginDelegate>)delegate;

@end



#define ON_LOGIN_SUCCESS ON_NOTIFICATION(loginSuccess,DMAccount*)
#define ON_LOGOUT ON_NOTIFICATION(logout,id)

@interface DMAccount : NSObject
/**
 *  用户id
 */
@property (nonatomic,retain) NSString* userID;
/**
 *  用户账号
 */
@property (nonatomic,retain) NSString* userAccount;

/**
 *  用户密码
 */
@property (nonatomic,retain) NSString* userPwd;

+(void)setLoginCaller:(id<DMLoginCaller>)caller;
+(void)callLoginController:(id<DMLoginDelegate>)delegate;
+(void)onLoginFail:(__weak id<DMLoginDelegate>)listener;
+(void)setAccountClass:(Class)clazz;
+(BOOL)isLogin;
+(__kindof DMAccount*)current;
+(void)onLoginSuccess:(DMAccount*)account;
+(BOOL)loadAccount;


//如果有改动
-(void)save;
-(void)load:(NSUserDefaults*)df;
-(NSDictionary*)toJson;
//登出
-(void)logout;


@end
