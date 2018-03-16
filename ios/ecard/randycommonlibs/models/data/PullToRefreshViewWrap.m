//
//  TableViewWrapper.m
//  eCard
//
//  Created by randy ren on 13-12-10.
//  Copyright (c) 2013年 citywithincity. All rights reserved.
//

#import "PullToRefreshViewWrap.h"

@interface PullToRefreshViewWrap()
{
    __weak NSObject<IPullToRefreshViewDelegate>* _delegate;
    __weak UIScrollView* _table;
}

@end

@implementation PullToRefreshViewWrap


-(void)dealloc{
    
}

-(id)initWithScrollView:(UIScrollView*)tableView
{
    if(self=[super init])
    {
        _table = tableView;
    }
    return self;
}


/////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////
//是否支持上拉刷新
-(void)setPullLoadEnable
{
    
}

//是否支持下拉刷新
-(void)setPullRefreshEnable
{
   
}
-(void)disablePullLoad{
    
}

//监听
-(void)setRefreshDelegate:(NSObject<IPullToRefreshViewDelegate>*)delegate
{
    _delegate=delegate;
    }



//加载数据完毕,设置数据刷新时间
-(void)onLoadComplete
{// 结束刷新状态
    /*if(_header){
        [_header endRefreshing];
        [_header getLastUpdateTimeLabel].text=[self getCurrentTime];
    }
    if(_footer){
        [_footer endRefreshing];
        [_header getLastUpdateTimeLabel].text=[self getCurrentTime];
    }*/
}

-(NSString*)getCurrentTime
{
    NSDateFormatter *outputFormatter = [[NSDateFormatter alloc] init];
    [outputFormatter setLocale:[NSLocale currentLocale]];
    [outputFormatter setDateFormat:@"HH时mm分ss秒"];
    NSDate* inputDate = [[NSDate alloc]init];
    NSString *str = [outputFormatter stringFromDate:inputDate];
    return str;
}


@end
