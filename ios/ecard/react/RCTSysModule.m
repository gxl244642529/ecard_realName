//
//  RCTSysModule.m
//  ecard
//
//  Created by 任雪亮 on 17/3/20.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import "RCTSysModule.h"
#import <UIKit/UIKit.h>
#import <dmlib/dmlib.h>

__weak RCTSysModule* __sysModule;

@interface RCTSysModule()
{
  
  NSDictionary* _dic;
  
}

@end

@implementation RCTSysModule

@synthesize bridge = _bridge;
+(RCTSysModule*)runningInstance{
  return __sysModule;
}
-(id)init{
  if(self = [super init]){
    __sysModule = self;
    _dic = @{
             @"yuanxin":@"YuanxinController",
             @"selling":@"SellingMainController",
             @"myecard":@"ECardController",
             @"recharge":@"RechargeController",
             @"pingan":@"PinganController",
             @"licai":@"LicaiController",
             @"safe":@"SafeController",
             @"bus":@"BusController",
             @"personInfo":@"PersonalController",
             @"setting":@"SettingController",
             @"myorder":@"SOrderListController",
             @"myrecharge":@"RechargeRecordController",
             @"mysafe":@"MySafeController",
             @"mycollection":@"SCollectionController",
             @"business":@"BusinessMainController",
             @"net":@"NetController",
             @"question":@"QuestionController",
             @"pickCard":@"GoodPersonalController",
             @"news":@"NewsController",
             @"fund":@"FundController",
             @"bindECard":@"ECardBindController",
             @"ecardDetail":@"ECardDetailController",


      };
    
  }
  return self;
}

RCT_EXPORT_MODULE();

+(void)viewWillAppear{
  if(__sysModule){
     [__sysModule.bridge.eventDispatcher sendDeviceEventWithName:@"viewWillAppear" body:@{}];
  }
}

-(void)notify:(NSString*)eventName data:(NSDictionary*)data{
   [__sysModule.bridge.eventDispatcher sendDeviceEventWithName:eventName body:data];
}

+(void)viewWillDespear{
  if(__sysModule){
    [__sysModule.bridge.eventDispatcher sendDeviceEventWithName:@"viewWillDispear" body:@{}];
  }
}

RCT_EXPORT_METHOD(setBlackStyle){
  dispatch_async(dispatch_get_main_queue(), ^{
    [[UIApplication sharedApplication] setStatusBarStyle:UIStatusBarStyleDefault animated:YES];
  });
}

RCT_EXPORT_METHOD(callModule:(NSString*)module data:(NSDictionary*)data){
  NSDictionary* _data =data;
  
  UIViewController* top =  [DMJobManager sharedInstance].topController;
  dispatch_async(dispatch_get_main_queue(), ^{
    NSString* name  = _dic[module];
    UIViewController* controller =   [[NSClassFromString(name) alloc]init];
    if([controller isKindOfClass:[DMViewController class]]  && [_data allKeys].count > 0){
      id __data = [[NSClassFromString(@"MyECardVo") alloc]init];
      [__data setValue:_data[@"cardid"] forKey:@"cardId"];
      [__data setValue:_data[@"cardidExt"] forKey:@"cardidExt"];
      [__data setValue:_data[@"cardName"] forKey:@"cardName"];
      [__data setValue:_data[@"expireTime"] forKey:@"expireTime"];
      [__data setValue:_data[@"cardType"] forKey:@"cardType"];
      
      [__data setValue:_data[@"createDate"] forKey:@"createDate"];
      [__data setValue:_data[@"cardFlag"] forKey:@"cardFlag"];
      
      ((DMViewController*)controller).data = __data;
    }
    [top.navigationController pushViewController:controller animated:YES];
  });
}

RCT_EXPORT_METHOD(onStartup){
  [[NSNotificationCenter defaultCenter] postNotificationName:@"startupSuccess"
                                                      object:nil];
}


RCT_EXPORT_METHOD(setWhiteStyle){
  dispatch_async(dispatch_get_main_queue(), ^{
     [[UIApplication sharedApplication] setStatusBarStyle:UIStatusBarStyleLightContent animated:YES];
  });
}

RCT_EXPORT_METHOD(setFirstRead){
  NSUserDefaults* def = [NSUserDefaults standardUserDefaults];

  [def setValue:@true forKey:@"firstUse"];
  [def synchronize];
}


@end
