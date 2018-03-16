//
//  RCTPushModule.h
//  ebusiness
//
//  Created by 任雪亮 on 16/10/25.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>
#import <React/RCTEventDispatcher.h>
@interface RCTPushModule : NSObject<RCTBridgeModule>
+(RCTPushModule*)runningInstance;

-(void)onRectNotification:(NSDictionary*)userInfo;
@end
