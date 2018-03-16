/**
 * Copyright (c) 2015-present, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

#import "AppDelegate.h"

#import "JPush.h"
#import "ReactUtil.h"
#import <ecardlib/ecardlib.h>
#import <amap/amap.h>
#import "RootViewController.h"
#import "MainViewController.h"
#import <ecardlib/ecardlib.h>
#import "AppLoadingView.h"
#import "PayModel.h"
#import "RCTSysModule.h"

@interface AppDelegate(){
  __weak UIView* _loadingView;
}
@end

@implementation AppDelegate
-(void)onStartup:(NSNotification*)sender{
  dispatch_async(dispatch_get_main_queue(), ^{
    [UIView animateWithDuration:0.25
                          delay:0
                        options:0
                     animations:^{
                       _loadingView.frame = CGRectMake(-[UIScreen mainScreen].bounds.size.width, 0, [UIScreen mainScreen].bounds.size.width, [UIScreen mainScreen].bounds.size.height);
                     } completion:^(__unused BOOL finished) {
                       [_loadingView removeFromSuperview];
                     }];

    
   
  });
  
}
- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
 
  [DMMapUtil setAppKey:@"4efd91100423a6799ac63e80311e2554"];
  
  // Override point for customization after application launch.
  [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onStartup:) name:@"startupSuccess" object:nil];
  
  
  [self startupJPush:launchOptions appKey:@"0fa49a7e347d301cbf2aaae9"];
  [ECardCaller callECard:nil account:@""];
  
  
  
  self.window = [[UIWindow alloc] initWithFrame:[UIScreen mainScreen].bounds];

  
  MainViewController *rootViewController = [[MainViewController alloc]init];
  
  
  ////////////////////////////////////////////////////////////////////////
  //rect开始
  UIView* view = [ReactUtil createRootView:launchOptions moduleName:@"ecard"];
  
  
    ////////////////////////////////////////////////////////////////////////
  
  
  view.frame =[UIScreen mainScreen].bounds;
  UIView* root = [[UIView alloc]initWithFrame:[UIScreen mainScreen].bounds];
  root.backgroundColor = RGB_WHITE(ff);
  [root addSubview:view];
  
  //再来一个
  UIView* loadingView = [[AppLoadingView alloc]initWithFrame:[UIScreen mainScreen].bounds];
  [root addSubview:loadingView];
  _loadingView = loadingView;
  rootViewController.view = root;
  
  
  RootViewController* c = [[RootViewController alloc]initWithRootViewController:rootViewController];
  
  [ECardCaller setRootController: c.topViewController];
  self.window.rootViewController = c;
 
  
  /*
  UIViewController* root = [ECardCaller createMain];
  [ECardCaller callECard:root account:@""];
  self.window.rootViewController = [[ RootViewController alloc ]initWithRootViewController:root];
*/
  
  
 
  [self.window makeKeyAndVisible];
  return YES;
}



-(void)applicationDidBecomeActive:(UIApplication *)application{
  
  [RCTSysModule viewWillAppear];
  
}

-(void)applicationDidEnterBackground:(UIApplication *)application{
  [UIApplication sharedApplication].applicationIconBadgeNumber = 0;
  
  
  
}


-(void)applicationWillResignActive:(UIApplication *)application{
  [RCTSysModule viewWillDespear];
}






- (BOOL)application:(UIApplication *)application
            openURL:(NSURL *)url
  sourceApplication:(NSString *)sourceApplication
         annotation:(id)annotation {
  
  return [self application:application handleOpenURL:url];
}

#pragma mark - 如果使用SSO（可以简单理解成客户端授权），以下方法是必要的
- (BOOL)application:(UIApplication *)application
      handleOpenURL:(NSURL *)url
{
  if([url.host isEqualToString:APP_PACKAGE]){
    return YES;
  }
  
  if([DMPayModel runningInstance] && [[DMPayModel runningInstance]handleOpenUrl:url]){
    return YES;
  }
  
  if ([url.host isEqualToString:@"safepay"]) {
    NSString* strUrl = [url absoluteString];
    strUrl = [strUrl stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    strUrl = [strUrl substringFromIndex:[strUrl rangeOfString:@"?"].location+1];
    NSError* error;
    NSDictionary *resultMap = [NSJSONSerialization JSONObjectWithData:[strUrl dataUsingEncoding:NSUTF8StringEncoding] options:NSJSONReadingMutableLeaves error:&error];
    
    assert(error==NULL);
    NSString* result = [resultMap[@"memo"] objectForKey:@"result"];
    if(result && result.length > 0 ){
      [[PayModel sharedInstance]onPaySuccess:result];
    }else{
      [[PayModel sharedInstance]onPayCancel];
    }
    return YES;
    
  }
  
  if([DMShareUtil handleOpenUrl:url]){
    return YES;
  }
  
  return NO;
}


@end
