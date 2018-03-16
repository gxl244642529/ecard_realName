//
//  IOnItemClickListener.h
//  randycommonlibs
//
//  Created by randy ren on 14-9-21.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
@protocol IOnItemClickListener <NSObject>
-(void)onItemClick:(UIView*)parent data:(NSObject*)data index:(NSInteger)index;
@end
