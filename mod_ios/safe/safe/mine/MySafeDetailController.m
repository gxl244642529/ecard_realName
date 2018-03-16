//
//  MySafeDetailController.m
//  ecard
//
//  Created by randy ren on 16/1/24.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "MySafeDetailController.h"

@interface MySafeDetailController ()

@end

@implementation MySafeDetailController
INIT_BUNDLE_CONTROLLER(MySafeDetailController, safebundle.bundle)
- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"保单详情"];
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
