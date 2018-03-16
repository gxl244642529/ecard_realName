//
//  JPush.h
//  ecard
//
//  Created by 任雪亮 on 17/3/22.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>

#import <UIKit/UIKit.h>

#import <dmlib/dmlib.h>
// 引入JPush功能所需头文件
#import "JPUSHService.h"
@interface UIResponder(JPush)<JPUSHRegisterDelegate,IPush>


-(void)startupJPush:(NSDictionary*)launchOptions appKey:(NSString*)appKey;

@end
