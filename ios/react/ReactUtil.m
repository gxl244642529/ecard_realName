//
//  ReactUtil.m
//  ecard
//
//  Created by 任雪亮 on 17/3/22.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import "ReactUtil.h"
#import <dmlib/dmlib.h>
#import <React/RCTBundleURLProvider.h>
#import <React/RCTRootView.h>
@implementation ReactUtil


//
+(UIView*)createRootView:(NSDictionary *)initData moduleName:(NSString *)moduleName{
  
  NSURL *jsCodeLocation;
  
  //jsCodeLocation=[NSURL URLWithString:[NSString stringWithFormat:@"http://%@:8081/index.ios.bundle?platform=ios&dev=true",@"192.168.1.248"]];
 // jsCodeLocation=[NSURL URLWithString:[NSString stringWithFormat:@"http://%@:8081/index.ios.bundle?platform=ios&dev=true",@"192.168.1.241"]];
  NSString* reactServer = [DMConfigReader getString:@"react"];
  if(!reactServer || reactServer.length == 0){
    jsCodeLocation = [[NSBundle mainBundle]URLForResource:@"bundle/index.ios" withExtension:@"jsbundle"];
  }else{
    jsCodeLocation=[NSURL URLWithString:[NSString stringWithFormat:@"http://%@:8081/index.ios.bundle?platform=ios&dev=true",reactServer]];
  }
 //  jsCodeLocation = [[NSBundle mainBundle]URLForResource:@"bundle/index.ios" withExtension:@"jsbundle"];
  NSMutableDictionary* initProps = nil;
  if([DMAccount isLogin]){
    DMAccount* account = [DMAccount current];
    initProps = [[NSMutableDictionary alloc]initWithDictionary:[account toJson]];
  }else{
    //imageurl
    initProps = [[NSMutableDictionary alloc]init];
  }
  
  initProps[@"imageUrl"] = [DMConfigReader getString:@"servers" subkey:@"php"];
  
  NSUserDefaults* def = [NSUserDefaults standardUserDefaults];
  id firstUse = [def valueForKey:@"firstUse"];
  if(firstUse){
    initProps[@"isFirst"] = @false;
  }else{
    initProps[@"isFirst"] = @true;
  }
  // app版本
  NSDictionary *infoDictionary = [[NSBundle mainBundle] infoDictionary];
  NSString *app_Version = [infoDictionary objectForKey:@"CFBundleShortVersionString"];
  initProps[@"version"]=  [NSNumber numberWithInt:[app_Version intValue]];
 //
  
  RCTRootView *rootView = [[RCTRootView alloc] initWithBundleURL:jsCodeLocation
                                                      moduleName:moduleName
                                               initialProperties:initProps
                                                   launchOptions:initData];
  rootView.backgroundColor = [[UIColor alloc] initWithRed:1.0f green:1.0f blue:1.0f alpha:1];
  
  return rootView;
}

@end
