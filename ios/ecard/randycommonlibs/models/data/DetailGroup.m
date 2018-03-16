//
//  DetailGroup.m
//  randycommonlibs
//
//  Created by randy ren on 14-9-22.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "DetailGroup.h"
#import <ecardlib/ecardlib.h>
#import "LoadingState.h"


@interface DetailGroup()
{
    UIScrollView* _scrollView;
    LoadingState* _state;
    PullToRefreshViewWrap* _wrap;
    ObjectJsonTask* _task;
    UIView* _contentView;
    
    __weak NSObject<IDetailListener>* _listener;
}

@end

@implementation DetailGroup

-(void)dealloc{
    _scrollView = NULL;
    _state = NULL;
    _wrap = NULL;
    _task = NULL;
    _contentView = NULL;
    self.data = NULL;
}

-(id)contentView{
    return _contentView;
}

//加载数据
-(void)onRefresh{
    [_task execute];
}
//加载更多数据
-(void)onLoadMore{
    
}
-(void)task:(JsonTask*)task result:(id)result{
    //初始化
    [_state onSuccess:YES];
    [_wrap onLoadComplete];
    
    [_listener onInitializeView:_contentView listData:self.data detailData:result];
}

-(void)task:(JsonTask *)task error:(NSString *)errorMessage isNetworkError:(BOOL)isNetworkError{
    [_state onError:errorMessage isNetworkError:isNetworkError];
    [_wrap onLoadComplete];
}
-(void)setListener:(NSObject<IDetailListener>*)listener{
    _listener = listener;
}

-(id)initWithFrame:(CGRect)frame parent:(UIView*)parent contentView:(UIView*)contentView  task:(ObjectJsonTask*)task{
    if(self=[super init]){
        _scrollView = [[UIScrollView alloc]initWithFrame:frame];
        [_scrollView setBackgroundColor:[UIColor clearColor]];
        [parent addSubview:_scrollView];
        [_scrollView addSubview:contentView];
        [_scrollView setContentSize:CGSizeMake(0, CGRectGetHeight(contentView.frame))];
        
        _state = [[LoadingState alloc]initWithScrollView:_scrollView];
        _wrap = [[PullToRefreshViewWrap alloc]initWithScrollView:_scrollView];
        [_wrap setPullRefreshEnable];
        [_wrap setRefreshDelegate:self];
        
        _contentView = contentView;
        
        _task = task;
        [_task setListener:self];
        [_task execute];

    }
    return self;
}

-(void)refreshSize{
    [_scrollView setContentSize:CGSizeMake(0, CGRectGetHeight(_contentView.frame))];
}
-(id)initWithFrame:(CGRect)frame parent:(UIView*)parent contentViewNibName:(NSString*)contentViewNibName task:(ObjectJsonTask*)task{
    if(self=[super init]){
        
        UIView* contentView = [ViewUtil createViewFormNibName:contentViewNibName owner:nil];
        
        _scrollView = [[UIScrollView alloc]initWithFrame:frame];
        [_scrollView setBackgroundColor:[UIColor clearColor]];
        [parent addSubview:_scrollView];
        [_scrollView addSubview:contentView];
        [_scrollView setContentSize:CGSizeMake(0, CGRectGetHeight(contentView.frame))];
        
        _state = [[LoadingState alloc]initWithScrollView:_scrollView];
        _wrap = [[PullToRefreshViewWrap alloc]initWithScrollView:_scrollView];
        [_wrap setPullRefreshEnable];
        [_wrap setRefreshDelegate:self];
        
        _contentView = contentView;
        
        _task = task;
        [_task setListener:self];
        [_task execute];
        
    }
    return self;

}
@end
