//
//  SizeUtil.m
//  ecard
//
//  Created by randy ren on 15-1-22.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "SizeUtil.h"

@implementation SizeUtil
+(CGSize)getCenterInsideSize:(CGFloat)containerWidth containerHeight:(CGFloat)containerHeight
                 targetWidth:(CGFloat)targetWidth targetHeight:(CGFloat)targetHeight{
    CGFloat dx = containerWidth / targetWidth;
    CGFloat dy = containerHeight / targetHeight;
    
    dx = MIN(dx, dy);
    
    return CGSizeMake(targetWidth*dx, targetHeight*dx);
}
@end
