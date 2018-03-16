//
//  MenuGroup.h
//  ecard
//
//  Created by randy ren on 15/3/29.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <ecardlib/ecardlib.h>
#import "TreeMenuView.h"
#import "MenuView.h"


/**
 点击按钮的时候,弹出菜单的范围是点击按钮底部开始到屏幕最底下结束(背景),而菜单的大小为固定大小(最大大小,如实际大小小于这个大小,则按照实际大小)
 点击背景后,菜单消失
 */

@interface MenuGroup : NSObject<MenuViewDelegate>

-(id)initWithFrame:(CGRect)frame parent:(UIView*)parent;

//按钮触发,包含一个label和三角指示
-(MenuView*)createSingleMenu:(ItemView*)triggerView;
-(TreeMenuView*)createTreeMenu:(ItemView*)triggerView;

-(MenuView*)getMenu:(NSInteger)index;

//设置数据
-(void)setData:(NSArray*)data index:(NSInteger)index;

@property (nonatomic,weak) NSObject<MenuViewDelegate>* delegate;

@end
