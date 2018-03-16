//
//  ImageToolUtil.h
//  ecard
//
//  Created by randy ren on 15/5/7.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface ImageToolUtil : NSObject
+ (UIImage*)filteredImage:(UIImage*)image saturation:(CGFloat)saturation brightness:(CGFloat)brightness contrast:(CGFloat)contrast;


+(UIImage*)rorateImage:(UIImage*)image rotateTransform:(CATransform3D)rotateTransform;
@end
