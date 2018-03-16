
//
//  RefreshableBase.m
//  DMLib
//
//  Created by randy ren on 16/1/14.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "RefreshableBase.h"
#import "DMServers.h"
#import "TableDataAdapter.h"
#import "DMCollectionDataAdapter.h"
#import "MJRefresh.h"
#import "OnItemClickOpenCtroller.h"
#import "DMJobManager.h"
#import "OnItemClickOpenCtroller.h"
#import "DMCellDataSetter.h"
#import "DMPage.h"
#import "DMViewUtil.h"
#import "DMLoadingView.h"
#import "DMErrorView.h"
#import "DMNoResultView.h"
#import "Alert.h"


@interface RefreshableBase ()
{
     DMDataAdapter* _adapter;
    DMApiJob* _task;
    DMNetState* _state;
    
}

@end

@implementation RefreshableBase
-(DMApiJob*)apiJob{
    return _task;
}
-(void)reload{
    [_task reload];
}

-(void)dealloc{
    _adapter = nil;
    [self cancel];
    _state = nil;
}
-(void)reloadWithState{
    [_state onRefresh];
    [self reload];
}

-(void)controllerWillFinish:(UIViewController *)controller{
    
    UITableView* _tableView = _scrollView;
    [_tableView deselectRowAtIndexPath:_adapter.currentIndex animated:YES];
    
}

-(void)from:(NSObject<DMList>*)value{
    
    
    _refreshable = value.refreshable;
    _paged = value.paged;
    _server = value.server;
    
    
    
    if(_refreshable){
        
        [_scrollView addHeaderWithTarget:self action:@selector(onRefresh:)];
    }
      //任务
    if(value.api){
        if(_paged){
            _task = [[DMJobManager sharedInstance]createPageJsonTask:value.api cachePolicy:value.useCache ? DMCachePolicy_TimeLimit :DMCachePolicy_NoCache server:value.server];
            
        }else{
            _task = [[DMJobManager sharedInstance]createArrayJsonTask:value.api cachePolicy:value.useCache ? DMCachePolicy_TimeLimit :DMCachePolicy_NoCache server:value.server];
            
        }
        
        if(value.entityName){
            _task.clazz = NSClassFromString(value.entityName);
        }
        
        _task.delegate = self;
    }
    
    if(value.controllerName){
        id<DMControllerDelegate> delegate = value.deselectWhenBack ? self : nil;
        [_adapter setOnItemClickListener:[[OnItemClickOpenCtroller alloc]initWithControllerName:value.controllerName delete:delegate]];
    }
    _state.loading = value.loadingView;
    _state.error = value.networkErrorView;
    _state.noResult = value.noResultView;
    
    [_state onRefresh];
    if(value.autoExecute){
        dispatch_async(dispatch_get_main_queue(), ^{
            [self execute];
        });
       
    }

}


-(void)onLoadingRefresh:(id)sender{
    [_state onRefresh];
    if(_paged){
        [_task put:@"position" value:[NSNumber numberWithInteger:[DMServers startPosition:_server]]];
    }
    [_task reload];
}


-(DMNetState*)netState{
    return _state;
}

-(instancetype)initWithScrollView:(UIScrollView*)scrollView adapter:(DMDataAdapter*)adapter  bundle:(NSBundle*)bundle{
    if(self = [super init]){
        _scrollView = scrollView;
        _adapter = adapter;
        _state = [[DMNetState alloc]initWithView:scrollView bundle:bundle];
        _state.delegate = self;
       
    }
    return self;
}
-(DMDataAdapter*)adapter{
    return _adapter;
}
-(void)cancel{
    if(_task){
        [_task cancel];
        _task = nil;
    }
}

-(void)put:(NSString*)key value:(id)value{
    [_task put:key value:value];
}
-(void)execute{
    if(_paged){
        [_task put:@"position" value:[NSNumber numberWithInteger:[DMServers startPosition:_server]]];
    }
    [_task execute];
}

-(void)jobSuccess:(DMApiJob*)request{
    [_scrollView headerEndRefreshing];
    if(_paged){
        DMPage* page = request.data;
        if([page isFirst]){
            [_adapter setData:page.list];
        }else{
            [_adapter addData:page.list];
        }
        if([page isLast]){
            [_scrollView removeFooter];
        }else{
            [_scrollView addFooterWithTarget:self action:@selector(onLoadMore:)];
            [_scrollView footerEndRefreshing];
        }
        
    }else{
        [_adapter setData:request.data];
    }
   [_state onSuccess:[_adapter getCount]>0];
}
-(void)endRefresh:(BOOL)hasResult{
    [_state onSuccess:hasResult];
}
-(BOOL)jobError:(DMApiJob*)request{
    [_scrollView headerEndRefreshing];
    
    if(request.error){
        [_state onError:request.error.reason isNetworkError:YES];
    }else{
        if(request.serverMessageType == MessageType_Toast){
            [Alert toast:request.serverMessage];
        }else{
            [Alert alert:request.serverMessage];
        }
    }
    return YES;
}
-(void)onLoadMore:(id)sender{
    [_task put:@"position" value:[NSNumber numberWithInteger: [_adapter getCount]+ [DMServers startPosition:_server]]];
    [_task execute];
}

-(void)onRefresh:(id)sender{
    //这里需要设置
    if(_paged){
        [_task put:@"position" value:[NSNumber numberWithInteger:[DMServers startPosition:_server]]];
    }
    [_task reload];
}



@end




