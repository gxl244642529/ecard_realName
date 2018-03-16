//
//  FundController.m
//  ecardlib
//
//  Created by 任雪亮 on 2017/9/20.
//  Copyright © 2017年 任雪亮. All rights reserved.
//

#import "FundController.h"

@interface FundController ()

@end

@implementation FundController

- (void)viewDidLoad {
    [super viewDidLoad];
}

-(void)loadPage{
    [self setTitle:@"惠民理财"];
    //load(DMServers.getImageUrl(0,"/index.php/fund"));
    [self load:[DMServers formatUrl:0 url:@"/index.php/fund"]];
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
