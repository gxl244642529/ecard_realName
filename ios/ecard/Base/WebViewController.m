//
//  BaseWebViewController.m
//  eCard
//
//  Created by randy ren on 14-2-28.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "WebViewController.h"
#import "LoadingView.h"
#import "ECardTaskManager.h"
//#import "MyECardController.h"


#import <ecardlib/ecardlib.h>

@interface WebViewController ()
{
    NSString* _currentUrl;
    
    //将要加载的网页
    NSString* _processUrl;
    
    //设置值的id
    NSString* _functionName;
    UIWebViewNavigationType _navType;
    
}
@end

@implementation WebViewController

+(NSString*)parseUrl:(NSString*)url{
    //检查第一个字符
    if('/' == [url characterAtIndex:0]){
        //相对路径
        url = [Constants formatUrl:url];
    }

    return url;
}


+(void)openUrl:(NSString*)url parent:(UIViewController*)parent title:(NSString*)title requireLogin:(BOOL)requireLogin{
    WebViewController* controller = [[WebViewController alloc]init];
    [controller setTitle:title];
    
    //检查第一个字符
    url = [WebViewController parseUrl:url];
    
    controller.requireLogin = requireLogin;
    controller.url = url;
    [parent.navigationController pushViewController:controller animated:YES];
}

-(id)init{
    if(self = [super init]){
        self.needWebView = TRUE;
    }
    return self;
}

-(void)dealloc{
    _functionName = nil;
    _currentUrl = nil;
    _webView=NULL;
    _processUrl = nil;
    self.url = nil;
     [SVProgressHUD dismiss];
}


- (void)viewDidLoad
{
    [super viewDidLoad];
    if(self.needWebView){
        [self createWebView];
    }
    
}

-(UIButton*)createBackButton{
    
    UIButton* addButton = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, 27,22)];
    [addButton setImage:[UIImage imageNamed:@"ic_back"] forState:UIControlStateNormal];
    Control_AddTarget(addButton, backToPrevious);
    return addButton;
}

-(void)onExit:(id)sender{
    [super finish];
}

-(UIView*)createBackButtonWithExit{
    UIButton* addButton = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, 27,22)];
    [addButton setImage:[UIImage imageNamed:@"ic_back"] forState:UIControlStateNormal];
    Control_AddTarget(addButton, backToPrevious);
    UIView* view = [[UIView alloc]initWithFrame:CGRectMake(0, 0, 27+30, 22)];
    [view addSubview:addButton];
    
    UIButton* backButton = [[UIButton alloc]initWithFrame:CGRectMake(27, 0, 30,22)];
    [backButton setTitle:@"退出" forState:UIControlStateNormal];
    [backButton.titleLabel setFont:[UIFont systemFontOfSize:13]];
    [backButton setTitleColor:[UIColor darkGrayColor] forState:UIControlStateNormal];
    Control_AddTarget(backButton, onExit);
    [view addSubview:backButton];
    
    return view;
}

-(void)backToPrevious:(id)sender{
    if([_webView canGoBack]){
        [_webView goBack];
    }else{
        [super backToPrevious:sender];
    }
}

-(void)createWebView{
    CGRect rect = self.view.frame;
    //创建UIWebView
    _webView = [[UIWebView alloc] initWithFrame:rect];
    [self.view addSubview:_webView];
    [_webView setUserInteractionEnabled:YES];
    [_webView setBackgroundColor:[UIColor clearColor]];
    [_webView setDelegate:self];
    [_webView setOpaque:NO];//使网页透明
    UIScrollView* view = _webView.subviews[0];
    [view setBounces:NO];
    
    LoadingView* loading = [[LoadingView alloc]initWithFrame:rect];
    loading.backgroundColor = [[UIColor blackColor]colorWithAlphaComponent:0.1];
    loading.tag = 100;
    [self.view addSubview:loading];
    
    
    UIButton* button = [self createTitleButton:@"刷新"];
    Control_AddTarget(button, onRefresh);
    [self loadWebView];
}

-(void)loadWebView{
    
    NSURLRequest* request ;
    NSString*  loadUrl =self.url;
    if(self.requireLogin){
        if([[JsonTaskManager sharedInstance]isLogin]){
            loadUrl = [NSString stringWithFormat:@"%@/%@",self.url,[[JsonTaskManager sharedInstance]userInfo].userID];
        }
    }
    if(!_currentUrl || [loadUrl compare:_currentUrl]!=0){
        request = [[NSURLRequest alloc]initWithURL:[NSURL URLWithString:loadUrl]];
        [_webView loadRequest:request];
    }else{
        [_webView reload];
    }
    
    _currentUrl = loadUrl;
}

-(void)onRefresh:(id)sender{
    [self loadWebView];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


//开始加载数据
- (void)webViewDidStartLoad:(UIWebView *)webView {
    //[activityIndicator startAnimating];
}

//数据加载完
- (void)webViewDidFinishLoad:(UIWebView *)webView {
    UIView* view=[self.view viewWithTag:100];
    if(view)
    {
        [view removeFromSuperview];
    }
    
    if([_webView canGoBack]){
        
        self.navigationItem.leftBarButtonItem = [[UIBarButtonItem alloc]initWithCustomView:[self createBackButtonWithExit]];
    }else{
        self.navigationItem.leftBarButtonItem = [[UIBarButtonItem alloc]initWithCustomView:[self createBackButton]];
    }
    [SVProgressHUD dismiss];
}

- (BOOL)webView:(UIWebView *)webView
shouldStartLoadWithRequest:(NSURLRequest *)request
 navigationType:(UIWebViewNavigationType)navigationType{
    _navType = navigationType;
   

    if(navigationType == UIWebViewNavigationTypeFormSubmitted || navigationType == UIWebViewNavigationTypeLinkClicked
       
              || navigationType == UIWebViewNavigationTypeOther){
        
        
         NSString *orgString = [[request.URL absoluteString]stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
       NSString* urlString = [orgString lastPathComponent];
        
        NSArray *urlComps = [urlString componentsSeparatedByString:@":"];
        NSInteger count = [urlComps count];
        if(count && [[urlComps objectAtIndex:0] isEqualToString:@"ecard"])
        {
            NSString *funcStr = [urlComps objectAtIndex:1];
            if([funcStr isEqualToString:@"login"])
            {
                /*调用本地的login*/
                if(count>=3){
                    _processUrl = urlComps[2];
                    _processUrl = [WebViewController parseUrl:_processUrl];
                }else{
                    _processUrl = nil;
                }
                
                [[ECardTaskManager sharedInstance]checkLogin:self loginSuccess:@selector(onLoginLoadView:)];
                
            }else if([funcStr isEqualToString:@"ecard"]){
                if(count>=3){
                    _functionName = urlComps[2];
                }
             //   [MyECardController selectCard:self];
            }else if([funcStr isEqualToString:@"wait"]){
                if(count >=3){
                    [SVProgressHUD showWithStatus:urlComps[2]];
                }else{
                    [SVProgressHUD show];
                }
            }else if([funcStr isEqualToString:@"cancelWait"]){
                if(count>=3){
                    NSString* type = urlComps[2];
                    if([type isEqualToString:@"error"]){
                        [SVProgressHUD showErrorWithStatus:urlComps[3]];
                    }else if([type isEqualToString:@"success"]){
                        [SVProgressHUD showSuccessWithStatus:urlComps[3]];
                    }else if([type isEqualToString:@"alert"]){
                        [Alert alert:urlComps[3]];
                        [SVProgressHUD dismiss];
                    }
                }else{
                    [SVProgressHUD dismiss];
                }
            }else {
                if([self parseUrl:funcStr arr:urlComps]){
                    return NO;
                }
            }
            return NO;
        }

    }
   
    return YES;
}
-(BOOL)parseUrl:(NSString*)key arr:(NSArray*)arr{
    return NO;
}
-(void)onControllerResult:(NSInteger)resultCode requestCode:(NSInteger)requestCode data:(NSObject *)data{
    if(resultCode==RESULT_OK){
        if(_functionName){
            [_webView stringByEvaluatingJavaScriptFromString:[NSString stringWithFormat:@"%@(\"%@\")",_functionName,data]];
        }
    }
}

-(void)onLoginLoadView:(id)sender{
    if(_processUrl){
        self.url = _processUrl;
        _processUrl = nil;
    }
    LoadingView* loading = [[LoadingView alloc]initWithFrame:self.view.bounds];
    loading.tag = 100;
    [self.view addSubview:loading];
    [self loadWebView];
}

@end

