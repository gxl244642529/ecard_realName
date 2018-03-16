//
//  PullToRefreshBase.m
//  randycommonlibs
//
//  Created by randy ren on 14-9-22.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "PullToRefreshBase.h"


@interface PullToRefreshBase(){
    ArrayJsonTask* _task;
    __weak NSObject<IPullTorefreshListener>* _listener;
}
@end

@implementation PullToRefreshBase
-(void)dealloc{
    _task = NULL;
    _state = NULL;
    _wrap = NULL;
    _adapter = NULL;
    _scrollView = NULL;
}


-(UIScrollView*)scrollView{
    return _scrollView;
}

-(JsonTask*)task{
    return _task;
}
-(id)initWithTask:(ArrayJsonTask*)task{
    if(self = [super init]){
        _task = task;
        if(task!=NULL){
            [_task setListener:self];
        }
    }
    return self;
}
-(id)initWithListener:(NSObject<IPullTorefreshListener>*)listener{
    if(self = [super init]){
        _listener = listener;
    }
    return self;
}



-(void)onLoadData:(NSInteger)position{
    if(_listener){
        [_listener onLoadData:position];
    }else{
        [_task reload];
    }
}



-(void)refreshData{
    [_state onRefresh];
    [self onLoadData:Start_Position];
}
//加载数据
-(void)onRefresh{
    [self onLoadData:Start_Position];
}
//加载更多数据
-(void)onLoadMore{
    [self onLoadData:[_adapter getCount] + Start_Position];
}

-(void)task:(JsonTask*)task error:(NSString*)errorMessage isNetworkError:(BOOL)isNetworkError{
    [_state onError:errorMessage isNetworkError:isNetworkError];
    [_wrap onLoadComplete];
}

-(void)task:(JsonTask*)task result:(NSArray*)result position:(NSInteger)position isLastPage:(BOOL)isLastPage{
    if(position == Start_Position){
        [_adapter setData:result];
    }else{
        [_adapter addData:result];
    }
    [_state onSuccess:result.count >0];
    [_wrap onLoadComplete];
    if(isLastPage){
        [_wrap disablePullLoad];
    }else{
        [_wrap setPullLoadEnable];
    }
}

@end
