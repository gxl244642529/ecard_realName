//
//  ColorUtil.h
//  eCard
//
//  Created by randy ren on 14-1-27.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
@interface ColorUtil : NSObject


+(UIColor*)redHighLight;
//标题颜色
+(UIColor*)titleColor;

//页面默认颜色
+(UIColor*)defaultPageColor;

+(void)setDefaultPageColor:(UIColor*)color;

+(UIColor*)rechargeColor;
//高亮颜色
+(UIColor*)itemHighlightColor;

//旅行颜色
+(UIColor*)travelColor;
//标题颜色
+(UIColor*)titleTextColor;


//itemview normal
+(UIColor*)itemNormalColor;

+(void)configColor:(UIColor*)titleTextColor titleBgColor:(UIColor*)titleBgColor itemNormalColor:(UIColor*)itemNoamralColor;


+(UIBarStyle)defaultStateBarStyle;
+(void)defaultStateBarStyle:(UIBarStyle)style;
@end
