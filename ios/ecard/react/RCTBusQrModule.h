//
//  RCTBusQrModule.h
//  ecard
//
//  Created by 任雪亮 on 17/3/31.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>
#import <dmlib/dmlib.h>

@interface RCTBusQrModule : NSObject<RCTBridgeModule>

+(void)disableToken;

@end
