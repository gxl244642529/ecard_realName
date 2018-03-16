//
//  TabItemGroup.h
//  ecard
//
//  Created by randy ren on 15-1-14.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "TabView.h"


/**
 *  选择
 */
@protocol TabItemGroupDelegate <NSObject>

/**
 *  <#Description#>
 *
 *  @param group TabItemGroup
 *  @param index 当前下标
 *
 *  @return 如需要切换，则返回true，否则，返回false
 */
-(void)tableGroup:(id)group didSelectIndex:(NSInteger)index;

/**
 *  <#Description#>
 *
 *  @param group <#group description#>
 *  @param index <#index description#>
 *
 *  @return true表示已经处理了click事件，不往下处理了
 */
-(BOOL)tableGroup:(id)group didClickIndex:(NSInteger)index;



@end

@interface TabItemGroup : NSObject


-(void)setHidden:(BOOL)hidden;

-(id)initWithTabItems:(NSArray*)items;

-(void)setDelegate:(NSObject<TabItemGroupDelegate>*)delegate;

-(void)setCurrentItem:(NSInteger)index;
@end
