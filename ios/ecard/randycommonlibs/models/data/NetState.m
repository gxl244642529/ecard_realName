//
//  NetState.m
//  ecard
//
//  Created by randy ren on 15/4/9.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "NetState.h"
#import "CommonMacro.h"
#import "LoadingView.h"
#import "NoresultView.h"
#import "LoadingErrorView.h"

@interface NetState()
{
    LoadingErrorView* _loadingErrrView;
    NoresultView* _noresultView;
    LoadingView* _loadingView;
    
    __weak UIView* _scrollView;
}

@end

@implementation NetState

-(id)initWithScrollView:(UIView*)scrollView{
    if(self = [super init]){
        _scrollView = scrollView;
        [self onRefresh];
        
    }
    return self;
}
-(void)onSuccess:(BOOL)hasResult{
    [self destroyLoadingView];
    [self destroyLoadingErrorView];
    if(!hasResult){
        [self createNoresultView];
    }else{
        [self destroyNoresultView];
    }
}
-(void)onError:(NSString*)errorMessage isNetworkError:(BOOL)isNetworkError{
    [self destroyLoadingView];
    [self createLoadingErrorView:isNetworkError];
}

-(void)removeFromSuperView{
    [self destroyLoadingErrorView];
    [self destroyLoadingView];
    [self destroyNoresultView];
}

-(void)onRefresh{
    [self destroyNoresultView];
    [self destroyLoadingErrorView];
    [self createLoadingView];
}
-(void)onNoResult{
    [self createNoresultView];
    [self destroyLoadingErrorView];
    [self destroyLoadingView];
}

#pragma private
-(void)createLoadingView{
    if(!_loadingView){
        _loadingView = [[LoadingView alloc]initWithFrame:self.rect];
        [_scrollView.superview addSubview:_loadingView];
    }
    
}
-(void)destroyLoadingView{
    if(_loadingView){
        [_loadingView removeFromSuperview];
        _loadingView = NULL;
    }
}
/**
 *  <#Description#>
 */
-(void)createNoresultView{
    if(!_noresultView){
        _noresultView = [[NoresultView alloc]initWithFrame:self.rect];
        [_noresultView setTarget:self withAction:@selector(onLoadingRresh:)];
        [_scrollView.superview addSubview:_noresultView];
    }
}

-(void)destroyNoresultView{
    if(_noresultView){
        [_noresultView removeFromSuperview];
        _noresultView = NULL;
    }
}
/**
 *  Description
 */
-(void)createLoadingErrorView:(BOOL)isNetworkError{
    if(!_loadingErrrView){
        _loadingErrrView = [[LoadingErrorView alloc]initWithFrame:self.rect];
        [_loadingErrrView setTarget:self withAction:@selector(onLoadingRresh:)];
        [_loadingErrrView showError:isNetworkError];
        [_scrollView.superview addSubview:_loadingErrrView];
    }
}

-(void)destroyLoadingErrorView{
    if(_loadingErrrView){
        [_loadingErrrView removeFromSuperview];
        _loadingErrrView = NULL;
    }
}

-(void)refreshRect{
    if(_loadingErrrView){
        _loadingErrrView.frame = self.rect;
    }
    if(_noresultView){
        _noresultView.frame = self.rect;
    }
    if(_loadingView){
        _loadingView.frame = self.rect;
    }
    
}

-(CGRect)rect{
    
    
    CGFloat y = _scrollView.frame.origin.y;
    //如果是tableview
    if([_scrollView isKindOfClass:[UITableView class]]){
        UITableView* tableView = (UITableView*)_scrollView;
        if(tableView.tableHeaderView){
            y += tableView.tableHeaderView.frame.size.height;
        }
    }
    
    return CGRectMake(_scrollView.frame.origin.x, y, [UIScreen mainScreen].bounds.size.width, _scrollView.frame.origin.y + _scrollView.frame.size.height - y);
    
}
ON_EVENT(onLoadingRresh){
    if(self.delegate!=NULL){
        [self.delegate onLoadingRefresh:self];
        [self onRefresh];
    }
}


@end
