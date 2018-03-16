//
//  DMCashierController.m
//  DMLib
//
//  Created by randy ren on 16/2/21.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMCashierController.h"
#import "Alert.h"
#import "DMPayModel.h"

@interface DMCashierController ()

@end

@implementation DMCashierController



-(void)finish{
    __weak typeof (self) __self = self;
    [Alert confirm:@"真的要退出收银台吗?" confirmListener:^(NSInteger buttonIndex) {
        if(buttonIndex==1){
            [__self exit];

        }
    }];
}
-(void)exit{
    if(![[DMPayModel runningInstance] notifyControllerExit]){
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
