//
//  PopupWindow.h
//  ecard
//
//  Created by randy ren on 15/3/28.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "CommonMacro.h"
@interface PopupWindow : UIView

/**
 *在本方法之前应该设置contentView的frame等信息
 *beforeState动画初始状态
 *endState动画结束状态
 */
+(PopupWindow*)show:(UIView*)contentView beforeState:(void (^)(UIView* contentView))beforeState endState:(void (^)(UIView* contentView))endState frame:(CGRect)frame;
-(void)show;

@property(nonatomic) BOOL notAutoHide;

@property(nonatomic,retain) UIView* contentView;

BLOCK_PROPERTY(beforeState,UIView* contentView);
BLOCK_PROPERTY(endState,UIView* contentView);
BLOCK_PROPERTY_VOID(onRemove);
+(void)hide;
+(UIView*)contentView;
-(void)remove;

@end
