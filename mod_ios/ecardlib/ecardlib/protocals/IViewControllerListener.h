//
//  IViewControllerListener.h
//  randycommonlib
//
//  Created by randy ren on 14-10-22.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol IViewControllerListener <NSObject>


-(void)onDealloc:(UIViewController*)controller;
-(void)onViewLoaded:(UIViewController*)controller;

@end
