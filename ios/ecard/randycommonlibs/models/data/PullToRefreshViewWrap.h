//
//  TableViewWrapper.h
//  eCard
//
//  Created by randy ren on 13-12-10.
//  Copyright (c) 2013年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "IPullToRefreshViewDelegate.h"
#import <DMLib/DMLib.h>






@interface PullToRefreshViewWrap : NSObject



-(id)initWithScrollView:(UIScrollView*)tableView;

//是否支持上拉刷新
-(void)setPullLoadEnable;

//是否支持下拉刷新
-(void)setPullRefreshEnable;

//取消上拉刷新
-(void)disablePullLoad;

//监听
-(void)setRefreshDelegate:(NSObject<IPullToRefreshViewDelegate>*)delegate;

//加载数据完毕,设置数据刷新时间
-(void)onLoadComplete;




@end
