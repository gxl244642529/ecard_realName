//
//  SaleUtil.h
//  ecard
//
//  Created by randy ren on 15-1-16.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#define SELLING_BG RGB(EC,EC,EC)


@interface SaleUtil : NSObject

+(UIViewController*)root;
+(void)setRoot:(UIViewController*)controller;

+(UICollectionViewFlowLayout*)getLayout;


/**
 *  diy 卡
 *
 *  @return <#return value description#>
 */
+(CGRect)diyImageFrame;

+(CGRect)diyImageFrame:(int)padding top:(int)top;
+(CGRect)diyImageFrame:(int)top height:(int)height;




+(UIColor*)bgColor;

@end
