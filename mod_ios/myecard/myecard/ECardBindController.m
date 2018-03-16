//
//  ECardBindController.m
//  ecard
//
//  Created by randy ren on 16/1/23.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "ECardBindController.h"

@interface ECardBindController ()

@end

@implementation ECardBindController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"绑定e通卡"];
}


INIT_BUNDLE_CONTROLLER(ECardBindController,myecardbundle.bundle)

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

//
API_JOB_SUCCESS(ecard, bind, id){
    [self.navigationController popViewControllerAnimated:YES];
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
