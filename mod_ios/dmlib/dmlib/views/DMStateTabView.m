//
//  DMStateTabView.m
//  ecard
//
//  Created by randy ren on 16/1/23.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "DMStateTabView.h"
#import "DMStateList.h"
#import "DMServers.h"
#import "DMDataAdapter.h"
#import "TableDataAdapter.h"
#import "DMCollectionDataAdapter.h"
#import "MJRefresh.h"
#import "OnItemClickOpenCtroller.h"
#import "DMJobManager.h"
#import "OnItemClickOpenCtroller.h"
#import "DMCellDataSetter.h"
#import "DMPage.h"
#import "RefreshableBase.h"
#import "DMJobManager.h"
#import "Alert.h"
#import "MJRefresh.h"

#import "DMCollectionDataAdapter.h"
#import "MJRefreshBaseView.h"
#import "UIScrollView+Adapter.h"

#import "UIView+ViewNamed.h"




@interface DMStateTabView()
{
    DMCellDataSetter* _setter;
    DMApiJob* _task;
    NSMutableArray<NSMutableArray*>* _dataArray;
    
    NSMutableArray<UIScrollView*>* _array;
    
    __weak UIScrollView* _currentRefresh;
    //点击item
    __weak UIScrollView* _currentParent;
    NSMutableArray<UIView*>* _views;
    
    NSMutableArray<DMNetState*>* _stateArray;
    NSInteger _lastWidth;
    
    Class _controllerClass;
    
    __weak UIViewController* _parent;
}

@end

@implementation DMStateTabView

-(void)dealloc{
    _stateArray = nil;
    _array = nil;
    _views = nil;
    _dataArray = nil;
    _task = nil;
    _setter = nil;
    _loadingView = nil;
    _networkErrorView = nil;
    _noResultView = nil;
    
    _type = nil;
    _api = nil;
    _entityName = nil;
    
    _cellName = nil;
    _controllerName = nil;
    
}

-(void)tab:(id)tab didSelectIndex:(NSInteger)index{
    [self setContentOffset:CGPointMake(index * self.bounds.size.width, 0) animated:YES];
}

-(void)setCurrentIndex:(NSInteger)index animated:(BOOL)animated{
   [self setContentOffset:CGPointMake(index * self.bounds.size.width, 0) animated:animated];
}

-(void)layoutSubviews{
    [super layoutSubviews];
    NSInteger currentWidth = self.bounds.size.width;
    if(_lastWidth!=currentWidth && currentWidth>0){
        _lastWidth = currentWidth;
        [self layoutContent];
    }
    
}


-(void)layoutContent{
    NSInteger i = 0;
    for(UIView* view in _views){
        view.frame = CGRectOffset(self.bounds, self.bounds.size.width*i, 0);
        [_stateArray[i] layoutSubViews:view.frame];
        ++i;
    }
   
#ifdef DEBUG
    NSLog(@"%lf",self.bounds.size.width* _views.count);
#endif
    self.contentSize = CGSizeMake(self.bounds.size.width*_views.count, 0);
}

-(void)onLoadingRefresh:(UIScrollView*)sender{
    
    _currentRefresh = sender;
    [_stateArray[sender.tag] onRefresh];
    [_task reload];
}
-(void)awakeFromNib{
    [super awakeFromNib];
    self.clipsToBounds = YES;
    _parent = [self findViewController];
    _array = [[NSMutableArray alloc]init];
    
    
    self.showsHorizontalScrollIndicator = NO;
    self.pagingEnabled = YES;
    self.delegate = self;
    if(!_cellName){
        //这里需要警告
       @throw [NSException exceptionWithName:@"必须有CellName" reason:@"视必须有CellName" userInfo:nil];
    }
    
    if(_targetTabTag>0){
        //找到
        UIView* target = [self.superview viewWithTag:_targetTabTag];
        if([target conformsToProtocol:@protocol(DMTabDelegate)]){
            _tabDelegate = (id<DMTabDelegate>)target;
        }
    }
    
    NSBundle* bundle =CREATE_BUNDLE_WHEN_NOT_NULL(_bundleName);

    _setter = [DMCellDataSetter create:_parent cellName:_cellName];
    _dataArray = [[NSMutableArray alloc]initWithCapacity:_tabCount];
    _stateArray =[[NSMutableArray alloc]initWithCapacity:_tabCount];
    _views = [[NSMutableArray alloc]initWithCapacity:_tabCount];
    for(NSInteger i =0; i < _tabCount; ++i){
        [_dataArray addObject:[[NSMutableArray alloc]init]];
        UIScrollView* scrollView = nil;
        if([_type  isEqual: @"t"]){
            UITableView* tableView = [[UITableView alloc]initWithFrame:CGRectZero];
            [self addSubview:tableView];
            TableDataAdapter* adapter = [[TableDataAdapter alloc]init];
            [adapter setScrollView:tableView];
            [adapter registerCell:_cellName bundle:bundle];
            tableView.rowHeight = _rowHeight;
            tableView.adapter = adapter;
            [adapter setListener:_setter];
            [_views addObject:tableView];
            scrollView = tableView;
            if(_controllerName){
                _controllerClass = NSClassFromString(_controllerName);
                [adapter setOnItemClickListener:self];
            }
        }else if([_type  isEqual: @"c"]){
            UICollectionView* collectionView = [[UICollectionView alloc]initWithFrame:CGRectZero];
            [self addSubview:collectionView];
            DMCollectionDataAdapter* adapter = [[DMCollectionDataAdapter alloc]init];
            [adapter setScrollView:collectionView];
            [adapter registerCell:_cellName bundle:bundle];
            collectionView.adapter = adapter;
            [adapter setListener:_setter];
            [_views addObject:collectionView];
             scrollView = collectionView;
        }else{
            
        }
        if(_refreshable){
            [scrollView addHeaderWithTarget:self action:@selector(onRefresh:)];
        }
        [_array addObject:scrollView];
        scrollView.tag = i;
        DMNetState* state = [[DMNetState alloc]initWithView:scrollView bundle:bundle];
        state.delegate = self;
        [_stateArray addObject:state];
        state.loading = self.loadingView;
        state.error = self.networkErrorView;
        state.noResult = self.noResultView;
        [state onRefresh];
    }
    
    

    
    //任务
    _task = [[DMJobManager sharedInstance]createArrayJsonTask:_api cachePolicy:_useCache ? DMCachePolicy_TimeLimit :DMCachePolicy_NoCache server:_server];
    _task.delegate = self;
    if(_entityName){
         _task.clazz = NSClassFromString(_entityName);
    }
   

    
    if(_autoExecute){
        [self execute];
    }
}
-(void)reloadWithStatus{
    for(DMNetState* state in _stateArray){
        [state onRefresh];
    }
    [_task reload];
}

-(void)onItemClick:(UIView *)parent data:(NSObject *)data index:(NSInteger)index{
    _currentParent = (UIScrollView*)parent;
    
    DMViewController* controller = [[_controllerClass alloc]init];
    controller.data = data;
    [_parent.navigationController pushViewController:controller animated:YES];
    
}

-(void)controllerWillFinish:(UIViewController*)controller{
    
    [((UITableView*)_currentParent) deselectRowAtIndexPath:_currentParent.adapter.currentIndex animated:YES];
    
}
-(void)setOnitemClickListener:(id<IOnItemClickListener>)listener{
    for (UIScrollView* view in _views) {
        [view.adapter setOnItemClickListener:listener];
    }
}

-(void)execute{
    [_task execute];
}

-(void)jobSuccess:(DMApiJob*)request{
    if(_currentRefresh){
         [_currentRefresh headerEndRefreshing];
        _currentRefresh = nil;
    }
    for (NSMutableArray* src in _dataArray){
        [src removeAllObjects];
    }
    NSArray* arr = request.data;
    //这里进行分析状态
    for (id data in arr) {
        NSInteger index = [_dataSource getDataIndex:data];
        [_dataArray[index] addObject:data];
    }
    
    for(NSInteger i=0; i < _tabCount; ++i){
        UIScrollView * scrollView = _array[i];
        [scrollView.adapter setData:_dataArray[i]];
        [_stateArray[i] onSuccess:_dataArray[i].count>0];
    }
    
}
-(void)jobError:(DMApiJob*)request{
    if(_currentRefresh){
        [_currentRefresh headerEndRefreshing];
        _currentRefresh = nil;
    }
    
    if(request.error){
        for(NSInteger i=0; i < _tabCount; ++i){
           [_stateArray[i] onError:@"" isNetworkError:YES];
        }
    }else{
        if(request.serverMessageType == MessageType_Toast){
            [Alert toast:request.serverMessage];
        }else{
            [Alert alert:request.serverMessage];
        }
    }
    
}
-(void)onRefresh:(MJRefreshBaseView*)sender{
    if(!_currentRefresh){
        _currentRefresh = sender.scrollView;
        //这里需要设置
        [_task reload];
    }else{
        //表示还在加载应该不能拉
        if(sender){
            dispatch_async(dispatch_get_main_queue(), ^{
                [sender endRefreshing];
            });
        }
        
        
        
    }
   
}

#pragma UIScrollViewDelegate
-(void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView{
    NSInteger current =scrollView.contentOffset.x;
    NSInteger width = self.frame.size.width;
    [_tabDelegate tab:self didSelectIndex:current/width];
}
@end
