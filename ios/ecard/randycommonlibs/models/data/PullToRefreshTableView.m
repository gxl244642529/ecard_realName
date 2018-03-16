//
//  PullToRefreshTableView.m
//  ecard
//
//  Created by randy ren on 15/3/27.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "PullToRefreshTableView.h"
#import "NetState.h"


@interface PullToRefreshTableView ()
{
    NetState* _state;
    TableDataAdapter* _adapter;
    ArrayJsonTask* _task;
    __weak NSObject<IPullTorefreshListener>* _pullListener;
}

@end

@implementation PullToRefreshTableView
-(void)setCellBackgroundColor:(UIColor*)color{
    [_adapter setCellBackgroundColor:color];
}

-(void)dealloc{
    _state = nil;
    _adapter = nil;
    _task = nil;
}
-(DMDataAdapter*)adapter{
    return _adapter;
}

-(void)setEnableState:(BOOL)enableState{
    if(!enableState){
        if(_state){
            [_state removeFromSuperView];
            _state = nil;
        }
        
    }else{
        if(!_state){
            _state = [[NetState alloc]initWithScrollView:self];
            [_state setDelegate:self];
        }
        
    }
}

-(id)initWithFrame:(CGRect)frame{
    if(self = [super initWithFrame:frame]){
        [self addHeaderWithTarget:self action:@selector(onRefresh:)];
        _state = [[NetState alloc]initWithScrollView:self];
        [_state setDelegate:self];
        _adapter = [[TableDataAdapter alloc]init];
        [_adapter setScrollView:self];
    }
    return self;
}

-(void)layoutSubviews{
    [super layoutSubviews];
    
    [_state refreshRect];
}



-(void)setOnItemClickListener:(NSObject<IOnItemClickListener>*)listener{
    [_adapter setOnItemClickListener:listener];
}
-(void)awakeFromNib{
    [super awakeFromNib];
    _state = [[NetState alloc]initWithScrollView:self];
    [_state setDelegate:self];
    [self addHeaderWithTarget:self action:@selector(onRefresh:)];
    _adapter = [[TableDataAdapter alloc]init];
    [_adapter setScrollView:self];
    
    
}

-(void)registerCell:(NSString*)nibName{
    [_adapter registerCell:nibName height:self.rowHeight bundle:nil];
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
-(void)setLineStyle:(UITableViewCellSeparatorStyle)style{
    self.separatorStyle = style;
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
-(void)setTableHeaderView:(UIView *)tableHeaderView{
    [super setTableHeaderView:tableHeaderView];
    [_state refreshRect];
}
-(void)task:(id)task error:(NSString *)errorMessage isNetworkError:(BOOL)isNetworkError{
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

-(void)setLoadingState{
    [_state onRefresh];
}
-(void)onLoadingRefresh:(id)sender{
    if(_pullListener){
        [_pullListener onLoadData:Start_Position];
    }else{
        [_task reload];
    }
}

@end
