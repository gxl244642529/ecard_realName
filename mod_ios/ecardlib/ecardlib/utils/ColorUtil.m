//
//  ColorUtil.m
//  eCard
//
//  Created by randy ren on 14-1-27.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "ColorUtil.h"

static UIColor* _titleColor;
static UIColor* _defaultPageColor;
static UIColor* _titleTextColor;
static UIBarStyle _style;
static UIColor* _itemNormalColor;

@implementation ColorUtil

+(UIBarStyle)defaultStateBarStyle{
    return _style;//UIBarStyleBlackOpaque;
}
+(void)defaultStateBarStyle:(UIBarStyle)style{
    _style = style;
}
+(void)setDefaultPageColor:(UIColor*)color{
    _defaultPageColor = color;
}
+(UIColor*)titleColor{
 //   return _titleColor;
    return [[UIColor alloc]initWithRed: 255/ 255.0f green: 92/ 255.0f blue:75/ 255.0f alpha:1];
}

+(UIColor*)rechargeColor{
    return [[UIColor alloc]initWithRed: 71/ 255.0f green: 176/ 255.0f blue:0/ 255.0f alpha:1];
}
+(UIColor*)redHighLight{
    return [[UIColor alloc]initWithRed: 255/ 255.0f green: 92/ 255.0f blue:75/ 255.0f alpha:1];
}
+(UIColor*)defaultPageColor
{
    return [[UIColor alloc]initWithRed:0xf2/255.0f green:0xf2/255.0f blue:0xf2/255.0f alpha:1];
}

+(UIColor*)itemHighlightColor
{
    return [[UIColor alloc]initWithRed:0xe4/255.0f green:0xe4/255.0f blue:0xe4/255.0f alpha:0.8];
}

+(UIColor*)travelColor{
    return [[UIColor alloc]initWithRed:0xfd/255.0f green:0xf8/255.0f blue:0xf2/255.0f alpha:1];
}
+(UIColor*)itemNormalColor{
    return _itemNormalColor;
}
+(UIColor*)titleTextColor{
    return [UIColor darkGrayColor];
}
+(void)configColor:(UIColor*)titleTextColor titleBgColor:(UIColor*)titleBgColor itemNormalColor:(UIColor*)itemNoamralColor{
    _itemNormalColor = itemNoamralColor;
    _titleTextColor = titleTextColor;
    _titleColor = titleBgColor;
}
@end
