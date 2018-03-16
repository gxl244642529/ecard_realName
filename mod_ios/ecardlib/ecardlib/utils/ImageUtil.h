//
//  ImageUtil.h
//  ecard
//
//  Created by randy ren on 15-1-22.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>

#import <UIKit/UIKit.h>
extern NSString* g_filter_names[];

@interface ImageUtil : NSObject

/**
 *  创建缩略图，忽视比例
 *
 *  @param image   <#image description#>
 *  @param newSize <#newSize description#>
 *
 *  @return <#return value description#>
 */
+(UIImage *)scaleImage:(UIImage*)image toSize:(CGSize)newSize;

/**
 *  创建缩略图，按照比例
 *
 *  @param image <#image description#>
 *  @param width <#width description#>
 *
 *  @return <#return value description#>
 */
+(UIImage *)createThumbImage:(UIImage*)image width:(float)width;


+ (UIImage *)imageWithImage:(UIImage*)inImage withColorMatrix:(const float*)f;
+ (UIImage *)imageFromView: (UIView *) theView;
/**
 *  应用滤镜
 *
 *  @param image <#image description#>
 *  @param index <#index description#>
 *
 *  @return <#return value description#>
 */
+(UIImage*)applyFilter:(UIImage*)image index:(NSInteger)index;


+(float*)getMatrix:(int)index;



+(NSString*)base64FromImage:(UIImage*)image quality:(CGFloat)quality;
@end
