//
//  AnimationUtil.h
//  ecard
//
//  Created by randy ren on 14-9-25.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
@interface AnimationUtil : NSObject

//从上到下
+(void)flyViewUpDown:(UIView*)view enter:(BOOL)enter delegate:(id)delegate;

//
+(void)flyView:(UIView*)view enter:(BOOL)enter delegate:(id)delegate;


@end
