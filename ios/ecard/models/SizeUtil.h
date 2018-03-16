//
//  SizeUtil.h
//  ecard
//
//  Created by randy ren on 15-1-22.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface SizeUtil : NSObject



+(CGSize)getCenterInsideSize:(CGFloat)containerWidth containerHeight:(CGFloat)containerHeight
                 targetWidth:(CGFloat)targetWidth targetHeight:(CGFloat)targetHeight;

@end
