//
//  PinganController.h
//  ecardlib
//
//  Created by 任雪亮 on 16/12/13.
//  Copyright © 2016年 任雪亮. All rights reserved.
//

#import <dmlib/dmlib.h>

@interface PinganController : DMWebViewController

- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationTyp;
    
-(void)loadPage;

@end
