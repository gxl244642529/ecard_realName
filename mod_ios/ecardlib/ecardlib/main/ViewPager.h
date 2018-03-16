//
//  ViewPager.h
//  randycommonlib
//
//  Created by randy ren on 14-10-9.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>


@protocol ViewPagerDelegate <NSObject>

@required
/**
 视图数量
 */
-(NSInteger)getPageCount;
/**
 创建视图
 */
-(UIView*)instantiateItem:(NSInteger)index parent:(UIView*)parent frame:(CGRect)frame;

@optional
/**
 *  销毁视图
 *
 *  @param view  <#view description#>
 *  @param index <#index description#>
 */
-(void)destroyItem:(UIView*)view index:(NSInteger)index;


/**
 *  拖动完成后调用
 *
 *  @param view  <#view description#>
 *  @param index <#index description#>
 */
-(void)viewPager:(UIView*)view didSelectedIndex:(NSInteger)index;

@end

@interface ViewPager : UIScrollView<UIScrollViewDelegate>


-(void)setDataSource:(NSObject<ViewPagerDelegate>*)dataSource;


/**
 *  设置当前页面下标
 *
 *  @param index <#index description#>
 */
-(void)setCurrentItem:(NSInteger)index animate:(BOOL)animate;
-(UIView*)getItem:(NSInteger)index;
@end
