//
//  ReactUtil.h
//  ecard
//
//  Created by 任雪亮 on 17/3/22.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface ReactUtil : NSObject

+(UIView*)createRootView:(NSDictionary*)initData moduleName:(NSString*)moduleName;

@end
