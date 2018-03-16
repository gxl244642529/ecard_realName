//
//  BusController.m
//  ecard
//
//  Created by 任雪亮 on 17/3/22.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import "BusController.h"

@interface BusController ()

@end

@implementation BusController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"公交"];
    [self load:@"http://m.doudou360.com/bus/Index.aspx?area=xiamen&partner=etongka.com"];
  
  
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
