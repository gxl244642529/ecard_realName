//
//  RCTSysModule.h
//  ecard
//
//  Created by 任雪亮 on 17/3/20.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>

#import <React/RCTEventDispatcher.h>

@interface RCTSysModule : NSObject<RCTBridgeModule>

+(RCTSysModule*)runningInstance;
-(void)notify:(NSString*)eventName data:(NSDictionary*)data;
+(void)viewWillAppear;
+(void)viewWillDespear;
@end
