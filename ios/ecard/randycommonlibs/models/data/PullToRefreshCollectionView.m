//
//  PullToRefreshCollectionView.m
//  ecard
//
//  Created by randy ren on 15/3/29.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "PullToRefreshCollectionView.h"


#include "NetState.h"

@interface PullToRefreshCollectionView ()
{
    NetState* _state;
    DMCollectionDataAdapter* _adapter;
    ArrayJsonTask* _task;
    __weak NSObject<IPullTorefreshListener>* _pullListener;
    
    __weak JsonTask* _currentTask;
}

@end

@implementation PullToRefreshCollectionView

-(void)onLoadingRefresh:(id)sender{
    [self onRefresh:sender];
}

-(id)initWithFrame:(CGRect)frame collectionViewLayout:(UICollectionViewLayout *)layout{
    if(self = [super initWithFrame:frame collectionViewLayout:layout]){
        
        _adapter = [[DMCollectionDataAdapter alloc]init];
        [_adapter setScrollView:self];

        [self addHeaderWithTarget:self action:@selector(onRefresh:)];
    }
    return self;
}

-(void)setLoadingState{
    [_state onRefresh];
}

-(void)dealloc{
    _state = nil;
    _adapter = nil;
    _task = nil;
}

-(void)setOnItemClickListener:(NSObject<IOnItemClickListener>*)listener{
    [_adapter setOnItemClickListener:listener];
}

-(void)setEnableState:(BOOL)enabled{
    _state = [[NetState alloc]initWithScrollView:self];
    [_state setDelegate:self];
    [_state onRefresh];
}

-(void)awakeFromNib{
    [super awakeFromNib];
    _state = [[NetState alloc]initWithScrollView:self];
    [_state setDelegate:self];
    [_state onRefresh];
    [self addHeaderWithTarget:self action:@selector(onRefresh:)];
    _adapter = [[DMCollectionDataAdapter alloc]init];
    [_adapter setScrollView:self];
}

-(void)registerCell:(NSString*)nibName{
    [_adapter registerCell:nibName bundle:nil];
}

-(void)onRefresh:(id)sender{
    if(_pullListener){
        [_pullListener onLoadData:Start_Position ];
    }else{
        [self.task reload];
    }
    
}
-(void)setDataListener:(NSObject<IDataAdapterListener>*)listener{
    [_adapter setListener:listener];
}

-(void)setPullToRefreshListener:(NSObject<IPullTorefreshListener>*)listener{
    [_adapter setListener:listener];
    _pullListener = listener;
}


-(void)onLoadMore:(id)sender{
    if(_pullListener){
        [_pullListener onLoadData:Start_Position + [_adapter getCount] ];
    }else{
        [self.task loadMore:Start_Position + [_adapter getCount]];
    }
}

-(void)task:(id)task result:(NSArray *)result position:(NSInteger)position isLastPage:(BOOL)isLastPage{
    _currentTask = task;
    [self headerEndRefreshing];
    [_state onSuccess:position > Start_Position || result.count>0];
    if(position > Start_Position){
        [_adapter addData:result];
    }else{
        [_adapter setData:result];
    }
    if (isLastPage) {
        [self removeFooter];
    }else{
        [self addFooterWithTarget:self action:@selector(onLoadMore:)];
        [self footerEndRefreshing];
    }
}

-(void)task:(id)task error:(NSString *)errorMessage isNetworkError:(BOOL)isNetworkError{
    _currentTask = task;
    [self headerEndRefreshing];
    [_state onError:errorMessage isNetworkError:isNetworkError];
}
-(ArrayJsonTask*)task{
    return _task;
}
-(void)setTask:(ArrayJsonTask *)task{
    _task =task;
    [_task setListener:self];
}


-(void)onLoadingRefresh{
    if(_pullListener){
        [_pullListener onLoadData:Start_Position ];
    }else{
        if(_currentTask){
            [_currentTask reload];
        }else{
            [self.task reload];
        }
        
    }

}

@end

