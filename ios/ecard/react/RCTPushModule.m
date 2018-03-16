//
//  RCTPushModule.m
//  ebusiness
//
//  Created by 任雪亮 on 16/10/25.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import "RCTPushModule.h"
#import <dmlib/dmlib.h>
__weak RCTPushModule* _RCTPushModule;

@interface RCTPushModule(){
  RCTResponseSenderBlock _callback;
}

@end

@implementation RCTPushModule
@synthesize bridge = _bridge;

RCT_EXPORT_MODULE();

+(RCTPushModule*)runningInstance{
  return _RCTPushModule;
}

-(id)init{
  if(self = [super init]){
    _RCTPushModule = self;
  }
  return self;
}


RCT_EXPORT_METHOD(getPushId:(RCTResponseSenderBlock)callback){
  callback(@[ [PushUtil getPushId]  ]);
}



-(void)onRectNotification:(NSDictionary*)userInfo{
  [self.bridge.eventDispatcher sendDeviceEventWithName: @"onPush" body:userInfo];
}
@end
