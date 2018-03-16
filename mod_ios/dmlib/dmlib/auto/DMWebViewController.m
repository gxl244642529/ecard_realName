//
//  WebViewController.m
//  libs
//
//  Created by randy ren on 16/1/9.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMWebViewController.h"
#import "DMMacro.h"
#import "DMColorConfig.h"

#import "MJRefresh.h"
#import "Alert.h"
#import "DMConfigReader.h"
@interface DMWebViewController()
{
    UIWebView* _webView;
    NSString* _url;
    NSString* _title;
}

@end

@implementation DMWebViewController
-(void)dealloc{
    [Alert cancelWait];
    _title = nil;
    _url = nil;
    _webView=NULL;
}
/**清除缓存和cookie*/
- (void)cleanCacheAndCookie{
    //清除cookies
    NSHTTPCookie *cookie;
    NSHTTPCookieStorage *storage = [NSHTTPCookieStorage sharedHTTPCookieStorage];
    for (cookie in [storage cookies]){
        [storage deleteCookie:cookie];
    }
    //清除UIWebView的缓存
    [[NSURLCache sharedURLCache] removeAllCachedResponses];
    NSURLCache * cache = [NSURLCache sharedURLCache];
    [cache removeAllCachedResponses];
    [cache setDiskCapacity:0];
    [cache setMemoryCapacity:0];
}
-(void)finish{
    [Alert cancelWait];
    if([_webView canGoBack]){
        [_webView goBack];
    }else{
        [super finish];
    }
}

-(id)initWithTitle:(NSString*)title url:(NSString*)url{
    if(self = [super init]){
        _title = title;
        _url = url;
        _displayRefresh = YES;
    }
    return self;
}
-(id)initWithTitle:(NSString*)title url:(NSString*)url delegate:(id<DMWebViewUrlParser>)delegate{
    if(self = [super init]){
        _title = title;
        _url = url;
        _delegate = delegate;
        _displayRefresh = YES;
    }
    return self;
}

-(NSString*)url{
    return _url;
}
-(UIWebView*)webView{
    return _webView;
}
- (void)viewDidLoad
{
    [super viewDidLoad];
    //self.view.backgroundColor = RGB(ff,ff,ff);
    //创建UIWebView
    self.view.backgroundColor = [UIColor whiteColor];
    
    CGRect bounds = [UIScreen mainScreen].bounds;
    
    CGRect rect = CGRectMake(0, 0, bounds.size.width, bounds.size.height-65);
    
    
  //  _webView = [[UIWebView alloc] initWithFrame:[UIScreen mainScreen] .bounds];
    _webView = [[UIWebView alloc] initWithFrame:rect];

    [self.view addSubview:_webView];
    [_webView setUserInteractionEnabled:YES];
    [_webView setBackgroundColor:[UIColor clearColor]];
    [_webView setDelegate:self];
    [_webView setOpaque:NO];//使网页透明
    [_webView.scrollView addHeaderWithTarget:self action:@selector(onRefresh:)];
    [_webView.scrollView setBounces:NO];
    
    [Alert wait:@"加载中..."];
    
    if(_url){
        [self load:_url];
    }
    if(_title){
        [self setTitle:_title];
    }
    if(_displayRefresh){
        self.navigationItem.rightBarButtonItem =  [DMWebViewController createTextItem:@"刷新" target:self action:@selector(onRefresh:)];
    }
 }

-(NSString*)clean:(NSString*)uglyString{
    return uglyString;//[uglyString stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    
}

-(void)onRefresh:(id)sender{
    [_webView reload];
}


-(void)load:(NSString*)url{
    _url = url;
     [_webView loadRequest: [ NSURLRequest requestWithURL:[ NSURL URLWithString:[self clean:url]]  ] ];
}

- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType{
    if(navigationType == UIWebViewNavigationTypeFormSubmitted || navigationType == UIWebViewNavigationTypeLinkClicked || navigationType == UIWebViewNavigationTypeOther){
        
        
        NSString *orgString = [[request.URL absoluteString]stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
#ifdef DEBUG
        
        NSLog(@"%@", [request.URL absoluteString]);
#endif
        NSString* urlString = [orgString lastPathComponent];
        NSArray *urlComps = [urlString componentsSeparatedByString:@":"];
        NSInteger count = [urlComps count];
        if(count && [[urlComps objectAtIndex:0] isEqualToString:@"ecard"])
        {
            NSString *funcStr = [urlComps objectAtIndex:1];
            //功能键
            if([self shouldHandleFunction:funcStr params:urlComps]){
                return NO;
            }
        }
        
    }
    
    _url = request.URL;
    
    return YES;
}


/**
 是否应该处理这个命令
 */
-(BOOL)shouldHandleFunction:(NSString*)func params:(NSArray*)params{
    if(_delegate)
        return [_delegate shouldHandleFunction:func params:params parent:self];
    return NO;
}


- (void)webViewDidStartLoad:(UIWebView *)webView{
    
}
- (void)webViewDidFinishLoad:(UIWebView *)webView{
    [webView.scrollView headerEndRefreshing];
    [Alert cancelWait];
    if([_webView canGoBack]){
        self.navigationItem.leftBarButtonItem = [[UIBarButtonItem alloc]initWithCustomView:[self createBackButtonWithExit]];
    }else{
        [self createLeftButton];
    }
    NSString* str= [_webView stringByEvaluatingJavaScriptFromString:@"document.title"];
    if(!str || str.length == 0){
        
    }else{
         [self setTitle:str];
    }
   
}

#define BUTTON_WIDTH 50

-(UIView*)createBackButtonWithExit{
    UIImage* image = [UIImage imageNamed:[DMConfigReader getString:@"back"]];
    UIButton* addButton = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, image.size.width, image.size.height)];
    [addButton setImage:image forState:UIControlStateNormal];
    [addButton addTarget:self action:@selector(finish) forControlEvents:UIControlEventTouchUpInside];
    
    UIButton* backButton = [[UIButton alloc]initWithFrame:CGRectMake(image.size.width, 0, BUTTON_WIDTH,22)];
    [backButton setTitle:@"退出" forState:UIControlStateNormal];
    [backButton.titleLabel setFont:[UIFont systemFontOfSize:13]];
    [backButton setTitleColor:[UIColor darkGrayColor] forState:UIControlStateNormal];
    Control_AddTarget(backButton, onExit);
    UIView* view = [[UIView alloc]initWithFrame:CGRectMake(0, 0, image.size.width+BUTTON_WIDTH, 22)];
    [view addSubview:addButton];
    [view addSubview:backButton];
    
    return view;
}
-(void)onExit:(id)sender{
    [super finish];
}
- (void)webView:(UIWebView *)webView didFailLoadWithError:(NSError *)error{
    
}
@end
