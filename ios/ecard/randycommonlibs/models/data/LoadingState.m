//
//  LoadingState.m
//  randycommonlibs
//
//  Created by randy ren on 14-9-21.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "LoadingState.h"
#import "CommonMacro.h"
#import "LoadingView.h"
#import "NoresultView.h"
#import "LoadingErrorView.h"

@interface LoadingState()
{
    LoadingErrorView* _loadingErrrView;
    NoresultView* _noresultView;
    LoadingView* _loadingView;
    __weak UIView* _parentView;
    BOOL _visible;
    
    CGRect _frame;
    
}
@end

@implementation LoadingState
-(void)onRefresh{
    [self onReload];
}


-(void)setVisible:(BOOL)visible{
    _visible = visible;
    if(_loadingErrrView)_loadingErrrView.hidden = !visible;
    if(_noresultView)_noresultView.hidden = !visible;
    if(_loadingView)_loadingView.hidden = !visible;
}
-(id)initWithParentView:(UIView*)parentView{
    if(self = [super init]){
        _parentView = parentView;
        _visible = TRUE;
        _frame = _parentView.bounds;
        [self createLoadingView];
    }
    return  self;
}



-(id)initWithScrollView:(UIView*)scrollView{
    if(self = [super init]){
         _parentView = scrollView;
         _visible = TRUE;
        _frame = _parentView.bounds;
         [self createLoadingView];
    }
    return  self;
}
-(void)onReload{
    [self destroyNoresultView];
    [self destroyLoadingErrorView];
    [self createLoadingView];
}
-(void)setFrame:(CGRect)frame{
    if(_noresultView){
        _noresultView.frame = frame;
    }
    if(_loadingView){
        _loadingView.frame = frame;
    }
    if(_loadingErrrView){
        _loadingErrrView.frame = frame;
    }
    _frame = frame;
}

#pragma private
-(void)createLoadingView{
    if(!_loadingView){
        _loadingView = [[LoadingView alloc]initWithFrame:_frame];
        [_parentView addSubview:_loadingView];
        _loadingView.hidden = !_visible;
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
        _noresultView = [[NoresultView alloc]initWithFrame:_frame];
        [_noresultView setTarget:self withAction:@selector(onLoadingRresh:)];
        [_parentView addSubview:_noresultView];
        _noresultView.hidden = !_visible;
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
        _loadingErrrView = [[LoadingErrorView alloc]initWithFrame:_frame];
        [_loadingErrrView setTarget:self withAction:@selector(onLoadingRresh:)];
        [_loadingErrrView showError:isNetworkError];
        [_parentView addSubview:_loadingErrrView];
        _loadingErrrView.hidden = !_visible;
    }
}

-(void)destroyLoadingErrorView{
    if(_loadingErrrView){
        [_loadingErrrView removeFromSuperview];
        _loadingErrrView = NULL;
    }
}

ON_EVENT(onLoadingRresh){
    if(self.delegate!=NULL){
      [self.delegate onLoadingRefresh:self];
        [self onReload];
    }
}

#pragma end

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
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

@end
