//
//  WebViewGroup.m
//  ecard
//
//  Created by randy ren on 14-9-26.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "WebViewGroup.h"
#import "PullToRefreshViewWrap.h"
#import "LoadingState.h"
#import "NetState.h"
@interface WebViewGroup()
{
    UIWebView* _webView;
    PullToRefreshViewWrap* _wrap;
    NetState* _state;
    BOOL isFinished;
    NSString* _url;
}

@end

@implementation WebViewGroup
-(void)dealloc{
    _webView = NULL;
    _state = NULL;
    _wrap = NULL;
    _url = NULL;
}

-(id)initWithFrame:(CGRect)frame parent:(UIView*)parent{
    if(self = [super init]){
        //创建UIWebView
        _webView = [[UIWebView alloc] initWithFrame:frame];
        [parent addSubview:_webView];
        [_webView setUserInteractionEnabled:YES];
        [_webView setBackgroundColor:[UIColor clearColor]];
        [_webView setDelegate:self];
        [_webView setOpaque:NO];//使网页透明s
        
        
        _wrap = [[PullToRefreshViewWrap alloc]initWithScrollView:_webView.scrollView];
        [_wrap setPullRefreshEnable];
        [_wrap setRefreshDelegate:self];
        
        
        _state = [[NetState alloc]initWithScrollView:_webView];
        _state.delegate = self;
    }
    return self;
}
-(void)onLoadingRefresh:(id)sender{
    [self onRefresh];
}
//加载数据
-(void)onRefresh{
    if(isFinished){
        [_webView reload];
    }else{
        [self loadUrl:_url];
    }
    
}
//加载更多数据
-(void)onLoadMore{
    [_webView reload];
}
-(void)loadUrl:(NSString*)url
{
    NSURL *req = [NSURL URLWithString:url];
    [_webView loadRequest:[NSURLRequest requestWithURL:req]];
    _url = url;
}

- (void)webViewDidFinishLoad:(UIWebView *)webView{
    

    [_wrap onLoadComplete];
    [_state onSuccess:YES];
    isFinished = TRUE;
}
- (void)webView:(UIWebView *)webView didFailLoadWithError:(NSError *)error{
    [_wrap onLoadComplete];
    [_state onError:NULL isNetworkError:YES];
    isFinished = NO;
}
@end
