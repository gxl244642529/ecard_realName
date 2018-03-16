//
//  BaseWebViewController.h
//  eCard
//
//  Created by randy ren on 14-2-28.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BaseViewController.h"
#import "PullToRefreshViewWrap.h"
@interface WebViewController : BaseViewController<UIWebViewDelegate>
{
    UIWebView* _webView;
}

@property (nonatomic) BOOL requireLogin;
@property (nonatomic,retain) NSString* url;


@property (nonatomic) BOOL needWebView;
+(NSString*)parseUrl:(NSString*)url;
+(void)openUrl:(NSString*)url parent:(UIViewController*)parent title:(NSString*)title requireLogin:(BOOL)requireLogin;
-(void)createWebView;


-(BOOL)parseUrl:(NSString*)key arr:(NSArray*)arr;

@end
