//
//  RCTAccountModule.m
//  ebusiness
//
//  Created by 任雪亮 on 16/8/19.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import "RCTAccountModule.h"
#import <dmlib/dmlib.h>
#import <ecardlib/ecardlib.h>
#import <myecard/myecard.h>

#import "NewLoginController.h"

__weak RCTAccountModule* RCTAccountModuleRunning;

@interface RCTAccountModule()
{
   __weak id<DMLoginDelegate> _delegate;
}

@end

@implementation RCTAccountModule

 @synthesize bridge = _bridge;

-(id)init{
  if(self = [super init]){
    [DMAccount loadAccount];
    [DMAccount setLoginCaller:self];
    RCTAccountModuleRunning = self;
    AddObserver(self, @"gotoRealCard", onGo:);
  }
  return self;
}

-(void)onGo:(NSNotification*)sender{
  id data = sender.object;
  [RCTAccountModuleRunning.bridge.eventDispatcher sendDeviceEventWithName: @"gotoRealCard" body:data];
  
}

+(void)changeHead:(NSString*)headUrl{
  [RCTAccountModuleRunning.bridge.eventDispatcher sendDeviceEventWithName: @"headChanged" body:@{@"url":headUrl} ];

}


+(void)onLogout{
  [RCTAccountModuleRunning.bridge.eventDispatcher sendDeviceEventWithName: @"logoutSuccess" body:@{} ];

}


RCT_EXPORT_MODULE();

-(void)callLoginController:(id<DMLoginDelegate>)delegate{
  self.loginDelegate = delegate;
  if(self.loginCallerback != nil){
     self.loginCallerback(@[]);
  }else{
    _delegate  = delegate;
    LoginController* controller = [[LoginController alloc]init];
    controller.delegate = self;
    [[DMJobManager sharedInstance].topController.navigationController pushViewController:controller animated:YES];
  }
}

RCT_EXPORT_METHOD(login:(RCTResponseSenderBlock)callback){
  self.onLoginResultCallback = callback;
  dispatch_async(dispatch_get_main_queue(), ^{
    LoginController* controller = [[LoginController alloc]init];
    controller.delegate = self;
    [[DMJobManager sharedInstance].topController.navigationController pushViewController:controller animated:YES];
  });
}

RCT_EXPORT_METHOD(save:(NSDictionary*)data){
  ECardUserInfo* account = [DMAccount current];
  account.data = [[NSMutableDictionary alloc]initWithDictionary:data];
  [account save];
}

//登录成功

-(DMPopType)onLoginSuccess{
  if(self.onLoginResultCallback){
    self.onLoginResultCallback(@[@1]);
    self.onLoginResultCallback = nil;
  }
  
 
  ECardUserInfo* user = [DMAccount current];
  
  [self.bridge.eventDispatcher sendDeviceEventWithName: @"loginSuccess" body: [user toJson] ];
  
  if(_delegate){
    DMPopType type= [_delegate onLoginSuccess];
    _delegate = nil;
    return type;
  }
  return DMPopBySelf;
}


-(void)onLoginCancel{
  if(_delegate){
    [_delegate onLoginCancel];
  }
  _delegate = nil;
  if(self.onLoginResultCallback){
    self.onLoginResultCallback(@[@0]);
    self.onLoginResultCallback = nil;
  }
  

}
RCT_EXPORT_METHOD(logout){
  [ECardUserInfo logout];
}


/*
RCT_EXPORT_METHOD(setLoginCaller:(RCTResponseSenderBlock)callback){
  self.loginCallerback  = callback;
}

RCT_EXPORT_METHOD(load:(RCTResponseSenderBlock)callback){
  
}

RCT_EXPORT_METHOD(onLoginSuccess:(NSDictionary*)data callback:(RCTResponseSenderBlock)callback){
  NSString* account = [data objectForKey:@"account"];
  NSString* password = [data objectForKey:@"pwd"];
  
  ECardUserInfo* ac = [DMAccount current];
  if(!ac){
    ac = [[ECardUserInfo alloc]init];
  }
  ac.userID = [NSString stringWithFormat:@"%@",[data valueForKey:@"userid"]];
  ac.userAccount = account;
  ac.userPwd = password;
  //ac.data = data;
  
  [DMAccount onLoginSuccess:ac];
  //保存
 // NSDictionary* d = [data valueForKey:@"token"];
 // [[EBusinessServerHandler runningInstance]setToken:d];
  
  callback(@[]);
}

RCT_EXPORT_METHOD(onLoginCancel){
  
}
*/
@end
