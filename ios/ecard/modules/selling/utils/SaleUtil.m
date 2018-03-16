
//
//  SaleUtil.m
//  ecard
//
//  Created by randy ren on 15-1-16.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "SaleUtil.h"
#import "CommonMacro.h"
#import "SellingModel.h"

#define RATE (float)1106/ 638


__weak UIViewController* _root;
@implementation SaleUtil

+(UIViewController*)root{
    return _root;
}
+(void)setRoot:(UIViewController*)controller{
    _root = controller;
}

+(UICollectionViewFlowLayout*)getLayout{
    UICollectionViewFlowLayout *flowLayout=[[UICollectionViewFlowLayout alloc] init];
    [flowLayout setScrollDirection:UICollectionViewScrollDirectionVertical];
    flowLayout.minimumInteritemSpacing=0;
    flowLayout.minimumLineSpacing = 7;
    flowLayout.itemSize = CGSizeMake(([UIScreen mainScreen].bounds.size.width-20)/2, 127);
    flowLayout.sectionInset = UIEdgeInsetsMake(7,7,7,7);//设置其边界
    return flowLayout;
}


+(CGRect)diyImageFrame{
    return CGRectMake(5, 5, 320-10, (float)318 * CARD_HEIGHT /  CARD_WIDTH);
}
+(CGRect)diyImageFrame:(int)top height:(int)height{
    float w = height * CARD_WIDTH / CARD_HEIGHT;
    return CGRectMake((320-w)/2, top, w, height);
}
+(CGRect)diyImageFrame:(int)padding top:(int)top{
    float w = (320-padding*2);
    return CGRectMake(padding, top,w , w * CARD_HEIGHT /  CARD_WIDTH);
}

+(UIColor*)bgColor{
    return RGB(EC,EC,EC);
}
@end
