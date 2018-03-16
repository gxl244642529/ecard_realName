//
//  PinganProductController.m
//  ecardlib
//
//  Created by 任雪亮 on 2017/10/14.
//  Copyright © 2017年 任雪亮. All rights reserved.
//

#import "PinganProductController.h"

@implementation PinganProductController


- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationTyp{
    
    if ([request.URL.absoluteString containsString:@"weixin://"]) {//拦截url，截取参数，
        NSLog(@"%@",request.URL.absoluteString);
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:request.URL.absoluteString]];
        return TRUE;
    }
    return YES;
    
}
-(void)finish{
    [Alert cancelWait];
    if([self.webView canGoBack]){
        [self.webView goBack];
    }else{
        [self.navigationController popViewControllerAnimated:YES];
    }
}
-(void)loadPage{
    
     [self setTitle:@"平安之家"];
    //加载
    
    
    
}
    
@end
