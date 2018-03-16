//
//  PinganController.m
//  ecardlib
//
//  Created by 任雪亮 on 16/12/13.
//  Copyright © 2016年 任雪亮. All rights reserved.
//

#import "PinganController.h"

#import "PinganProductController.h"


@interface PinganController ()
{
    NSMutableSet<NSString*>* whiteList;
    NSMutableSet<NSString*>* whiteUrl;
    BOOL _realease;
    NSMutableSet<NSString*>* titleBlackList;
}
@end

@implementation PinganController

-(id)initWithTitle:(NSString*)title url:(NSString*)url delegate:(id<DMWebViewUrlParser>)delegate{
    if(self = [super initWithTitle:title url:url delegate:delegate]){
        _displayRefresh = FALSE;
    }
    return self;
}

- (BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationTyp{
    
    if ([request.URL.absoluteString containsString:@"weixin://"]) {//拦截url，截取参数，
        NSLog(@"%@",request.URL.absoluteString);
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:request.URL.absoluteString]];
        return TRUE;
    }
    
    //这里判断下
    if ([request.URL.absoluteString containsString:@"{userid}"]) {
        DMAccount* account = [DMAccount current];
        
        NSString* url = [request.URL.absoluteString stringByReplacingOccurrencesOfString:@"{userid}" withString:account.userID];
        request = [NSURLRequest requestWithURL:[NSURL URLWithString:url  ]];
    }
    if ([request.URL.absoluteString containsString:@"%7Buserid%7D"]) {
        DMAccount* account = [DMAccount current];
        
        NSString* url = [request.URL.absoluteString stringByReplacingOccurrencesOfString:@"%7Buserid%7D" withString:account.userID];
        request = [NSURLRequest requestWithURL:[NSURL URLWithString:url  ]];
    }
    if ([request.URL.absoluteString containsString:@"https://home.pingan.com.cn/m/insurance_unlogin/index.html?shareId="]) {
        
        
        PinganProductController* product = [[PinganProductController alloc]initWithTitle:@"平安之家" url:request.URL.absoluteString];
       [ self.navigationController pushViewController:product animated:YES];
        
        return NO;
       
    }
    return [super webView:webView shouldStartLoadWithRequest:request navigationType:navigationTyp];
    
}
-(id)initWithTitle:(NSString *)title url:(NSString *)url{
    if(self = [super initWithTitle:title url:url]){
        _displayRefresh = FALSE;
    }
    return self;
}
    


- (void)viewDidLoad {
    [super viewDidLoad];
    [self cleanCacheAndCookie];
    whiteList = [[NSMutableSet alloc]init];
    [whiteList addObject:@"home.pingan.com.cn"];
    
    
     _realease = [[DMConfigReader getString:@"pingan"] boolValue];
    
    if(!_realease){
        [whiteList addObject:@"egis-cssp-dmzstg1.pingan.com.cn"];
        [whiteList addObject:@"egis-cssp-dmzstg2.pingan.com.cn"];
        [whiteList addObject:@"egis-cssp-dmzstg3.pingan.com.cn"];
    }
   
    whiteUrl = [[NSMutableSet alloc]init];
    if(_realease){
        [ whiteUrl addObject:@"https://cz.pingan.com.cn/ibd/page/web_for_h5/mainAcct_h5/index.html#ylx/index/finish" ];
        
    }else{
        [ whiteUrl addObject:@"https://rsb-stg2.pingan.com.cn:7443/ibd/page/web_for_h5/mainAcct_h5/index.html?environment=rsb#ylx/index/finish" ];
    }
 
    
    titleBlackList =[[NSMutableSet alloc]init];
    
    if(_realease){
        [titleBlackList addObject:@"https://cz.pingan.com.cn/ibd/page/web_for_h5/mainAcct_h5/index.html#ylx/index/input"];
        [titleBlackList addObject:@"https://cz.pingan.com.cn/ibd/page/web_for_h5/mainAcct_h5/index.html#ylx/index/passSet"];
        [titleBlackList addObject:@"https://cz.pingan.com.cn/ibd/page/web_for_h5/mainAcct_h5/index.html#ylx/index/finish"];
    }else{
        [titleBlackList addObject:@"https://rsb-stg2.pingan.com.cn:7443/ibd/page/web_for_h5/mainAcct_h5/index.html?environment=rsb#ylx/index/input"];
        [titleBlackList addObject:@"https://rsb-stg2.pingan.com.cn:7443/ibd/page/web_for_h5/mainAcct_h5/index.html?environment=rsb#ylx/index/passSet"];
        [titleBlackList addObject:@"https://rsb-stg2.pingan.com.cn:7443/ibd/page/web_for_h5/mainAcct_h5/index.html?environment=rsb#ylx/index/finish"];
    }
    
   
    [self loadPage];

}

-(void)loadPage{
    [self setTitle:@"平安之家"];
    DMAccount* account = [DMAccount current];
    if(_realease){
        [self load:[NSString stringWithFormat:@"http://www.cczcc.net/index.php/pingan260/index/%@", account.userID ]];
    }else{
        [self load:[NSString stringWithFormat:@"http://www.cczcc.net/index.php/pingan_test/index/%@", account.userID ]];
    }

}

-(void)dealloc{
    whiteList = nil;
}

//判断如果是主页
-(void)finish{
    if( [whiteUrl containsObject:self.webView.request.URL.absoluteString]){
        [super finish];
        return;
    }
    NSString* host = self.webView.request.URL.host;
#ifdef DEBUG
    NSLog(@"%@",self.webView.request.URL.absoluteString);
#endif
    
    
    
    if([whiteList containsObject:host]){
        NSString* str = self.webView.request.URL.absoluteString;
        if([str hasSuffix:@"pdf"]){
             [super finish];
        }else{
            [self.webView stringByEvaluatingJavaScriptFromString:@"javascript:window.back();"];
        }
        
    }else{
         [super finish];
    }
    
   
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
