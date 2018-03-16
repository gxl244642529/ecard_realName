//
//  IPullToRefreshViewDelegate.h
//  randycommonlib
//
//  Created by randy ren on 14-9-28.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>

@protocol IPullToRefreshViewDelegate <NSObject>
//加载数据
-(void)onRefresh;
//加载更多数据
-(void)onLoadMore;
@end

