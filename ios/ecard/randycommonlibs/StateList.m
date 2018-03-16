//
//  StateList.m
//  ecard
//
//  Created by randy ren on 15/12/8.
//  Copyright © 2015年 citywithincity. All rights reserved.
//

#import "StateList.h"




@interface StateList()
{
    UIScrollView* _scrollView;
    NetLoadingState* _state;
    DMDataAdapter* _adapter;
    ArrayJsonTask* _task;
    
    
     __weak NSObject<IPullTorefreshListener>* _pullListener;
}

@end

@implementation StateList
-(void)registerView:(NSString*)nibName forState:(NSString*)state{
    [_state registerState:nibName forState:state];
}
-(void)dealloc{
    _task = nil;
    _state = nil;
    _scrollView = nil;
    _adapter = nil;
}
-(void)setOnItemClickListener:(NSObject<IOnItemClickListener>*)listener{
    [_adapter setOnItemClickListener:listener];
}
-(void)setCellBackgroundColor:(UIColor*)color{
    [_adapter setCellBackgroundColor:color];
}
-(void)setCellSelectionStyle:(UITableViewCellSeparatorStyle)stype{
    [((UITableView*)_scrollView)setSeparatorStyle:stype];
}
-(id)initWithFrame:(CGRect)frame type:(StateListType)type{
    if(self = [super initWithFrame:frame]){
        frame = CGRectMake(0, 0, frame.size.width, frame.size.height);
        switch (type) {
            case StateListType_TableView:
            {
                _scrollView = [[UITableView alloc]initWithFrame:frame];
                _adapter = [[TableDataAdapter alloc]init];
                
            }
                break;
            case StateListType_CollectionView:
            {
                _scrollView = [[UICollectionView alloc]initWithFrame:frame];
                _adapter = [[DMCollectionDataAdapter alloc]init];
            }
                break;
            default:
            {
                _scrollView = [[UITableView alloc]initWithFrame:frame];
            }
                break;
        }
        [_scrollView addHeaderWithTarget:self action:@selector(onRefresh:)];
        [_adapter setScrollView:_scrollView];
        [self addSubview:_scrollView];
        
        _state = [[NetLoadingState alloc]initWithParentView:self];
        _state.delegate = self;
        [_state onRefresh];
        
    }
    return self;
}
-(void)onRefresh:(id)sender{
    if(_pullListener){
        [_pullListener onLoadData:Start_Position ];
    }else{
        [self.task reload];
    }
    
}
-(void)registerCell:(NSString*)nibName rowHeight:(NSInteger)rowHeight{
    ((UITableView*)_scrollView).rowHeight = rowHeight;
    [((TableDataAdapter*)_adapter) registerCell:nibName bundle:nil];
}
-(void)layoutSubviews{
    [super layoutSubviews];
    
}
-(void)setDataListener:(NSObject<IDataAdapterListener>*)listener{
    [_adapter setListener:listener];
}
-(void)setPullToRefreshListener:(NSObject<IPullTorefreshListener>*)listener{
    [_adapter setListener:listener];
    _pullListener = listener;
}
-(ArrayJsonTask*)task{
    return _task;
}
-(void)setTask:(ArrayJsonTask *)task{
    _task =task;
    [_task setListener:self];
}



-(void)deselectRowAtIndexPath:(NSIndexPath*)indexPath animated:(BOOL)animated{
    [((UITableView*)_scrollView)deselectRowAtIndexPath:indexPath animated:animated];
}

-(void)onLoadingRefresh:(id)sender{
    if(_pullListener){
        [_pullListener onLoadData:Start_Position];
    }else{
        [_task reload];
    }
}
-(UIScrollView*)scrollView{
    return _scrollView;
}
-(void)task:(id)task error:(NSString *)errorMessage isNetworkError:(BOOL)isNetworkError{
    [_scrollView headerEndRefreshing];
    [_state onError:errorMessage isNetworkError:isNetworkError];
}
-(void)task:(id)task result:(NSArray *)result position:(NSInteger)position isLastPage:(BOOL)isLastPage{
    [_scrollView headerEndRefreshing];
    [_state onSuccess:position > Start_Position || result.count>0];
    if(position > Start_Position){
        [_adapter addData:result];
    }else{
        [_adapter setData:result];
    }
    if (isLastPage) {
        [_scrollView removeFooter];
    }else{
        [_scrollView addFooterWithTarget:self action:@selector(onLoadMore:)];
        [_scrollView footerEndRefreshing];
    }
}

-(void)onLoadMore:(id)sender{
    if(_pullListener){
        [_pullListener onLoadData:Start_Position + [_adapter getCount] ];
    }else{
        [self.task loadMore:Start_Position + [_adapter getCount]];
    }
    
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
