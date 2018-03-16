//
//  DMNetState.m
//  DMLib
//
//  Created by randy ren on 16/1/23.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMNetState.h"

#import "DMLoadingView.h"
#import "DMErrorView.h"
#import "DMNoResultView.h"
#import "DMViewUtil.h"
#import "DMMacro.h"
@interface DMNetState()
{
    NSBundle* _bundle;
    __weak UIView* _view;
    //这个不能点击
    UIView* _loadingView;
    UIView* _noresultView;
    UIView<DMErrorView>* _errorView;
}

@end



@implementation DMNetState


-(void)dealloc{
    _bundle = nil;
    _loadingView = nil;
    _noresultView = nil;
    _errorView = nil;
}

-(id)initWithView:(UIView*)view  bundle:(NSBundle*)bundle{
    if(self = [super init]){
        _view = view;
        _bundle = bundle;
    }
    return self;
}
-(void)onLoadingRefresh:(id)sender{
    [_delegate onLoadingRefresh:_view];
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

-(void)layoutSubViews:(CGRect)frame{
    _loadingView.frame = frame;
    _noresultView.frame = frame;
    _errorView.frame = frame;
}

#pragma private
-(void)createLoadingView{
    if(!_loadingView){
        _loadingView = [self createLoadingView:_view.frame];
        [_view.superview addSubview:_loadingView];
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
        _noresultView = [self createNoresultView:_view.frame];
        if( [_noresultView conformsToProtocol:@protocol(LoadingState)]){
            id<LoadingState> loadingState = (id<LoadingState>)_noresultView;
            loadingState.loadingDelegate = self;
        }
        [_view.superview addSubview:_noresultView];
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
    if(!_errorView){
        _errorView = [self createErrorView:_view.frame];
        [_errorView setError:@"" isNetworkError:isNetworkError];
        _errorView.loadingDelegate = self;
        [_view.superview addSubview:_errorView];
    }
}

-(void)destroyLoadingErrorView{
    if(_errorView){
        [_errorView removeFromSuperview];
        _errorView = NULL;
    }
}


-(UIView*)createLoadingView:(CGRect)frame{
    if(_loading){
        UIView* view= [DMViewUtil createViewFormNibName:_loading bundle: _bundle owner:self];
        view.frame = frame;
        return view;
    }
    return [[DMLoadingView alloc]initWithFrame:frame];
}



-(UIView<DMErrorView>*)createErrorView:(CGRect)frame{
    if(_error){
        UIView<DMErrorView>* view= [DMViewUtil createViewFormNibName:_error bundle: _bundle owner:self];
        view.frame = frame;
        return view;
    }
    
    return[[DMErrorView alloc]initWithFrame:frame];
}
-(UIView*)createNoresultView:(CGRect)frame{
    if(_noResult){
        UIView* view= [DMViewUtil createViewFormNibName:_noResult bundle: _bundle owner:self];
        view.frame = frame;
        return view;
    }
    return [[DMNoResultView alloc]initWithFrame:frame];
}

@end
