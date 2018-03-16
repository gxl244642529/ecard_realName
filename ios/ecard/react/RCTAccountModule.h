//
//  RCTAccountModule.h
//  ebusiness
//
//  Created by 任雪亮 on 16/8/19.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>
#import <React/RCTEventDispatcher.h>
#import <dmlib/dmlib.h>
@interface RCTAccountModule : NSObject<RCTBridgeModule,DMLoginCaller,DMLoginDelegate>

+(void)changeHead:(NSString*)headUrl;

+(void)onLogout;


@property (nonatomic,weak) id<DMLoginDelegate> loginDelegate;

@property (readwrite,copy) RCTResponseSenderBlock loginCallerback;
@property (readwrite,copy) RCTResponseSenderBlock onLoginResultCallback;
@end
