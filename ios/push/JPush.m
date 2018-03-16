//
//  JPush.m
//  ecard
//
//  Created by 任雪亮 on 17/3/22.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import "JPush.h"
#import "RCTBusQrModule.h"
// iOS10注册APNs所需头文件
#ifdef NSFoundationVersionNumber_iOS_9_x_Max
#import <UserNotifications/UserNotifications.h>
#endif
// 如果需要使用idfa功能所需要引入的头文件（可选）
#import <AdSupport/AdSupport.h>
#import <ecardlib/ecardlib.h>

#import "RCTPushModule.h"


@implementation UIResponder(JPush)

-(NSString*)getPushId{
  return [JPUSHService registrationID];
}
-(void)onRecvRegId:(NSString*)regId{
  NSLog(@"registrationID获取成功：%@",regId);
  
  if([DMAccount isLogin]){
    DMAccount* account =[DMAccount current];
    [self onLoginSuccess:account];
  }

}
-(void)onLoginSuccess:(DMAccount*)account{
  NSMutableSet* set = [[NSMutableSet alloc]init];
  //   [set addObject:@"123"];
  [JPUSHService setTags:set alias:account.userAccount fetchCompletionHandle:^(int iResCode, NSSet *iTags, NSString *iAlias) {
    
    NSLog(@"设置成功:======%@",iAlias);
    
  }];

}


-(void)onLogout{
   NSMutableSet* set = [[NSMutableSet alloc]init];
  [JPUSHService setTags:set alias:@"" fetchCompletionHandle:^(int iResCode, NSSet *iTags, NSString *iAlias) {
    
    NSLog(@"设置成功:======%@",iAlias);
    
  }];

}



-(void)startupJPush:(NSDictionary*)launchOptions appKey:(NSString*)appKey{
  
  
  [PushUtil setPush:self];
  
  // Override point for customization after application launch.
  NSString *advertisingId = [[[ASIdentifierManager sharedManager] advertisingIdentifier] UUIDString];
  
  // 3.0.0及以后版本注册可以这样写，也可以继续用旧的注册方式
  JPUSHRegisterEntity * entity = [[JPUSHRegisterEntity alloc] init];
  entity.types = JPAuthorizationOptionAlert|JPAuthorizationOptionBadge|JPAuthorizationOptionSound;
  if ([[UIDevice currentDevice].systemVersion floatValue] >= 8.0) {
    //可以添加自定义categories
    //    if ([[UIDevice currentDevice].systemVersion floatValue] >= 10.0) {
    //      NSSet<UNNotificationCategory *> *categories;
    //      entity.categories = categories;
    //    }
    //    else {
    //      NSSet<UIUserNotificationCategory *> *categories;
    //      entity.categories = categories;
    //    }
  }
  [JPUSHService registerForRemoteNotificationConfig:entity delegate:self];
  
  // 3.0.0以前版本旧的注册方式
  //  if ([[UIDevice currentDevice].systemVersion floatValue] >= 10.0) {
  //#ifdef NSFoundationVersionNumber_iOS_9_x_Max
  //    JPUSHRegisterEntity * entity = [[JPUSHRegisterEntity alloc] init];
  //    entity.types = UNAuthorizationOptionAlert|UNAuthorizationOptionBadge|UNAuthorizationOptionSound;
  //    [JPUSHService registerForRemoteNotificationConfig:entity delegate:self];
  //#endif
  //  } else if ([[UIDevice currentDevice].systemVersion floatValue] >= 8.0) {
  //      //可以添加自定义categories
  //      [JPUSHService registerForRemoteNotificationTypes:(UIUserNotificationTypeBadge |
  //                                                        UIUserNotificationTypeSound |
  //                                                        UIUserNotificationTypeAlert)
  //                                            categories:nil];
  //  } else {
  //      //categories 必须为nil
  //      [JPUSHService registerForRemoteNotificationTypes:(UIRemoteNotificationTypeBadge |
  //                                                        UIRemoteNotificationTypeSound |
  //                                                        UIRemoteNotificationTypeAlert)
  //                                            categories:nil];
  //  }
  
#ifdef DEBUG
  BOOL isProduction = NO;
#else
  BOOL isProduction = YES;
#endif

  //如不需要使用IDFA，advertisingIdentifier 可为nil
  [JPUSHService setupWithOption:launchOptions appKey:appKey
                        channel:@"ios"
               apsForProduction:isProduction
          advertisingIdentifier:advertisingId];
  
  //2.1.9版本新增获取registration id block接口。
  [JPUSHService registrationIDCompletionHandler:^(int resCode, NSString *registrationID) {
    if(resCode == 0){
      NSLog(@"registrationID获取成功：%@",registrationID);
      [PushUtil onRecvRegId:registrationID];
    }
    else{
      NSLog(@"registrationID获取失败，code：%d",resCode);
    }
  }];
  
}



#pragma AppDelegate

- (void)application:(UIApplication *)application
didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {
  
  /// Required - 注册 DeviceToken
  [JPUSHService registerDeviceToken:deviceToken];
}

//可选
- (void)application:(UIApplication *)application didFailToRegisterForRemoteNotificationsWithError:(NSError *)error {
  //Optional
  NSLog(@"did Fail To Register For Remote Notifications With Error: %@", error);
}


#pragma mark- JPUSHRegisterDelegate

// iOS 10 Support
- (void)jpushNotificationCenter:(UNUserNotificationCenter *)center willPresentNotification:(UNNotification *)notification withCompletionHandler:(void (^)(NSInteger))completionHandler {
  // Required
  NSDictionary * userInfo = notification.request.content.userInfo;

  if([notification.request.trigger isKindOfClass:[UNPushNotificationTrigger class]]) {
    [JPUSHService handleRemoteNotification:userInfo];
    
    int type = [userInfo[@"type"]intValue];
    if(type == 1){
      //单点登录
      [ECardUserInfo logout];
      [Alert alert:@"您的账号在其他设备登录,如非本人操作请及时修改密码"];
      
    } if(type == 204){
      [RCTBusQrModule disableToken];
    }
  }
  completionHandler(UNNotificationPresentationOptionAlert); // 需要执行这个方法，选择是否提醒用户，有Badge、Sound、Alert三种类型可以选择设置
}

/**

 点击了顶部的之后
 */
// iOS 10 Support
- (void)jpushNotificationCenter:(UNUserNotificationCenter *)center didReceiveNotificationResponse:(UNNotificationResponse *)response withCompletionHandler:(void (^)())completionHandler {
  // Required
  NSDictionary * userInfo = response.notification.request.content.userInfo;
  if([response.notification.request.trigger isKindOfClass:[UNPushNotificationTrigger class]]) {
    [JPUSHService handleRemoteNotification:userInfo];
     [self onTapTitleNotification:userInfo];
  }
  completionHandler();  // 系统要求执行这个方法
}

- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler {
  
  // Required, iOS 7 Support
  [JPUSHService handleRemoteNotification:userInfo];
  completionHandler(UIBackgroundFetchResultNewData);
}

- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo {
  
  // Required,For systems with less than or equal to iOS6
  [JPUSHService handleRemoteNotification:userInfo];
}




-(void)onTapTitleNotification:(NSDictionary*)userInfo{
    [UIApplication sharedApplication].applicationIconBadgeNumber =  [UIApplication sharedApplication].applicationIconBadgeNumber+1;
  NSInteger type = [userInfo[@"type"]integerValue];
  NSLog(@"收到推送:%@",userInfo);
  if(type==10 || type==11){
    
    DMWebViewController* c = [[DMWebViewController alloc]initWithTitle:userInfo[@"title"] url:userInfo[@"url"]];
    [[DMJobManager sharedInstance].topController.navigationController pushViewController:c animated:YES];
    
    
  }else if(type > 100){
    
    if(type == 204){
      [RCTBusQrModule disableToken];
    }else{
       [[RCTPushModule runningInstance]onRectNotification:userInfo];
    }
    
    
  }
  
}


@end
