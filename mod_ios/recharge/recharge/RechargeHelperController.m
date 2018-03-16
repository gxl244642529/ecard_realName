//
//  RechargeHelperController.m
//  recharge
//
//  Created by 任雪亮 on 16/9/25.
//  Copyright © 2016年 任雪亮. All rights reserved.
//

#import "RechargeHelperController.h"

@interface RechargeHelperController ()

@end

@implementation RechargeHelperController


INIT_BUNDLE_CONTROLLER(RechargeHelperController, rechargebundle.bundle)

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"卟噔帮助"];
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
