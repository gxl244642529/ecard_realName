//
//  NetLoadingState.m
//  ecard
//
//  Created by randy ren on 15/12/8.
//  Copyright © 2015年 citywithincity. All rights reserved.
//

#import "NetLoadingState.h"
#import "CommonMacro.h"
#import "LoadingView.h"
#import "NoresultView.h"
#import "LoadingErrorView.h"
#import <ecardlib/ecardlib.h>


@interface NetLoadingState()
{
    LoadingErrorView* _loadingErrrView;
    UIView* _noresultView;
    LoadingView* _loadingView;
    __weak UIView* _parentView;
    
    
    NSMutableDictionary* _viewMap;
}

@end

NSString* NetStateType_Loading=@"NetStateType_Loading";
NSString* NetStateType_NoResult=@"NetStateType_NoResult";
NSString* NetStateType_Error=@"NetStateType_Error";

///////////////////////////////////

@implementation NetLoadingState

-(void)dealloc{
    _loadingErrrView = nil;
    _noresultView = nil;
    _loadingView = nil;
    _viewMap = nil;
}

-(void)registerState:(NSString*)viewName forState:(NSString*)type{
    if(!_viewMap){
        _viewMap = [[NSMutableDictionary alloc]init];
    }
    _viewMap[type]= viewName;
    
}
-(id)initWithParentView:(UIView*)parentView{
    if(self = [super init]){
        _parentView = parentView;
        [self refreshRect];
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
        [_parentView addSubview:_loadingView];
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
        if(_viewMap){
            NSString* key = [_viewMap objectForKey:NetStateType_NoResult];
            if(key){
                _noresultView =[ViewUtil createViewFormNibName:key owner:nil];
                _noresultView.frame = self.rect;
                if([_noresultView isKindOfClass:[TouchableView class]]){
                    [(TouchableView*)_noresultView setTarget:self withAction:@selector(onLoadingRresh:)];
                }
            }else{
                _noresultView = [[NoresultView alloc]initWithFrame:self.rect];
                [(NoresultView*)_noresultView setTarget:self withAction:@selector(onLoadingRresh:)];
            }
        }else{
             _noresultView = [[NoresultView alloc]initWithFrame:self.rect];
            [(NoresultView*)_noresultView setTarget:self withAction:@selector(onLoadingRresh:)];
        }
        [_parentView addSubview:_noresultView];
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
        [_parentView addSubview:_loadingErrrView];
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
    
    return CGRectMake(0, 0, _parentView.frame.size.width, _parentView.frame.size.height);
    
}
ON_EVENT(onLoadingRresh){
    if(self.delegate!=NULL){
      [self.delegate onLoadingRefresh:self];
        [self onRefresh];
    }
}

@end
