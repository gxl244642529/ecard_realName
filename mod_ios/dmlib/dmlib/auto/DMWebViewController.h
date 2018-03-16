//
//  WebViewController.h
//  libs
//
//  Created by randy ren on 16/1/9.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "DMViewController.h"


/**
 实现地址解析功能
 地址必须以ecard开头
 格式如：
 ecard:param1:param2
 */
@protocol DMWebViewUrlParser <NSObject>

-(BOOL)shouldHandleFunction:(NSString*)key params:(NSArray*)params parent:(UIViewController*)parent;

@end

@interface DMWebViewController : DMViewController<UIWebViewDelegate>
{
    BOOL _displayRefresh;
}
-(id)initWithTitle:(NSString*)title url:(NSString*)url;
-(id)initWithTitle:(NSString*)title url:(NSString*)url delegate:(id<DMWebViewUrlParser>)delegate;
-(void)load:(NSString*)url;
-(UIWebView*)webView;
-(NSString*)url;
-(void)cleanCacheAndCookie;
-(void)onExit:(id)sender;
@property (nonatomic,weak) id<DMWebViewUrlParser> delegate;

- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType;
- (void)webViewDidFinishLoad:(UIWebView *)webView;
-(UIView*)createBackButtonWithExit;

@end
