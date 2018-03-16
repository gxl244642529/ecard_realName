//
//  CMBWebViewController.m
//  ecard
//
//  Created by 任雪亮 on 16/6/17.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "CMBWebViewController.h"

#define CMB

#ifdef CMB

#import <cmbkeyboard/CMBWebKeyboard.h>
#import <cmbkeyboard/NSString+Additions.h>

#endif

@interface CMBWebViewController ()

@end

@implementation CMBWebViewController

-(void)onRefresh{
  [self.webView reload];
}

- (void)viewDidLoad {
  [super viewDidLoad];
  [SVProgressHUD showWithStatus:@"加载中..."];
  self.navigationController.navigationItem.rightBarButtonItem = [DMViewController createTextItem:@"刷新" target:self action:@selector(onRefresh)];
  //清除cookies
  NSHTTPCookie *cookie;
  NSHTTPCookieStorage *storage = [NSHTTPCookieStorage sharedHTTPCookieStorage];
  for (cookie in [storage cookies])
  {
    [storage deleteCookie:cookie];
  }
  
  //清除UIWebView的缓存
  [[NSURLCache sharedURLCache] removeAllCachedResponses];
  
  [self setTitle:@"一网通银行卡支付"];
  self.delegate = self;
}
- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType{
    
#ifdef CMB
  if ([request.URL.host isCaseInsensitiveEqualToString:@"cmbls"]) {//此处开始调用键盘
    CMBWebKeyboard* secKeyboard = [CMBWebKeyboard shareInstance];
    [secKeyboard showKeyboardWithRequest:request];
    secKeyboard.webView = webView;
    //以下是实现点击键盘外地方，自动隐藏键盘
    UITapGestureRecognizer* myTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(handleSingleTap:)];
    [self.view addGestureRecognizer:myTap]; //这个可以加到任何控件上,比如你只想响应WebView，我正好填满整个屏幕
    myTap.delegate = self;
    myTap.cancelsTouchesInView = NO;
    return NO;
  }
#endif
    
    NSString *orgString = [[request.URL absoluteString]stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    NSRange range = [orgString rangeOfString:@"ecard:"];
    if(range.location > 0 && range.location < 30){
        NSString* urlString = [orgString lastPathComponent];//  :@"http://" withString:@""];
        NSArray *urlComps = [urlString componentsSeparatedByString:@":"];
        NSInteger count = [urlComps count];
        if(count && [[urlComps objectAtIndex:0] isEqualToString:@"ecard"])
        {
            NSString *funcStr = [urlComps objectAtIndex:1];
            //功能键
            if([self shouldHandleFunction:funcStr params:urlComps parent:self]){
                return NO;
            }
        }
    }
   
    return YES;
  
}

-(void)onExit:(id)sende{
    [_payDelegate onCMBCancel];
    [super onExit:sende];
}
- (void)webViewDidStartLoad:(UIWebView *)webView{
  [SVProgressHUD showWithStatus:@"加载中..."];
}

    
    -(BOOL)isSuccess:(NSString*)url{
        if(!url){
            return false;
        }
        return [url hasSuffix:@"MB_EUserP_PayOK"];
    }

-(void)finish{
    if( [self isSuccess:self.webView.request.URL.absoluteString] ){
        
        [_payDelegate onCMBSuccess:nil];
        [self.navigationController popViewControllerAnimated:YES];
    }else{
         [_payDelegate onCMBCancel];
        [super finish];
    }
   
    
    
}

- (void)webView:(UIWebView *)webView didFailLoadWithError:(NSError *)error
{
  NSLog(@"Load webView error:%@", [error localizedDescription]);
}



-(void)viewDidDisappear:(BOOL)animated{
  [super viewDidDisappear:animated];
#ifdef CMB
  CMBWebKeyboard* secKeyboard = [CMBWebKeyboard shareInstance];
  [secKeyboard hideKeyboard];
#endif
  
}


-(BOOL)shouldHandleFunction:(NSString*)key params:(NSArray*)params parent:(UIViewController*)parent{
  if([key isEqualToString:@"cmb"]){
     id<CMBDelegate> delegate = _payDelegate;
    
      [self.navigationController popViewControllerAnimated:YES];
    [delegate onCMBSuccess:params];
    
    return YES;
  }
  return NO;
}

- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldRecognizeSimultaneouslyWithGestureRecognizer:(UIGestureRecognizer *)otherGestureRecognizer
{
  return YES;
}

-(void)handleSingleTap:(UITapGestureRecognizer *)sender{
  CGPoint gesturePoint = [sender locationInView:self.view];
  NSLog(@"handleSingleTap!gesturePoint:%f,y:%f",gesturePoint.x,gesturePoint.y);
  //[_secKeyboard hideKeyboard];
#ifdef CMB
  CMBWebKeyboard* secKeyboard = [CMBWebKeyboard shareInstance];
  [secKeyboard hideKeyboard];
#endif
  
}



- (void)didReceiveMemoryWarning {
  [super didReceiveMemoryWarning];
  // Dispose of any resources that can be recreated.
}

/*
 #pragma mark - Navigation
 
 // In a storyboard-based application, you will often want to do a little preparation before navigation
 - (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
 // Get the new view controller using [segue destinationViewController].
 // Pass the selected object to the new view controller.
 }
 */

@end
