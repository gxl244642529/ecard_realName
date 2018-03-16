//
//  YuanxinController.m
//  ecardlib
//
//  Created by 任雪亮 on 16/12/14.
//  Copyright © 2016年 任雪亮. All rights reserved.
//

#import "YuanxinController.h"

@interface YuanxinController ()

@end

@implementation YuanxinController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    /**
     DMAccount* account = [DMAccount current];
     NSString* url = [NSString stringWithFormat:@"http://www.cczcc.net/index.php/yuanxin/index/%@", account.userID ];
     YuanxinController* c= [[YuanxinController alloc]initWithTitle:@"圆信永丰" url:url];
     [self.navigationController pushViewController:c animated:YES];

     */
    
    DMAccount* account = [DMAccount current];
    NSString* url = [NSString stringWithFormat:@"http://www.cczcc.net/index.php/yuanxin/index/%@", account.userID ];
    [self setTitle:@"圆信永丰"];
    [self load:url];
    
}



//判断如果是主页
-(void)finish{
    NSString* url = self.webView.request.URL.absoluteString;
    if([url hasPrefix:@"http://test.m.gtsfund.com.cn/app-fund/pages/account-openAcco-tl-validate.html"]){
        
        DMAccount* account = [DMAccount current];
        NSString* usrId = account.userID;
        NSString* url = [NSString stringWithFormat:@"http://www.cczcc.net/index.php/yuanxin/index/%@/update",usrId];
        [self.webView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:url]]];
        
        
    }else if([url hasPrefix:@"http://www.cczcc.net/index.php/yuanxin/index/"]){
        [self.navigationController popViewControllerAnimated:YES];
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
