//
//  DMTabIndicatorView.h
//  DMLib
//
//  Created by randy ren on 16/1/24.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "DMTabDelegate.h"
/**
 切换指示器
 */
IB_DESIGNABLE
@interface DMTabIndicatorView : UIView<DMTab>

//指示器的颜色
@property (nonatomic,copy) IBInspectable UIColor* color;

//指示器的数量
@property (nonatomic,assign) IBInspectable NSInteger count;

-(id)initWithColor:(UIColor*)color count:(NSInteger)count;


@end
