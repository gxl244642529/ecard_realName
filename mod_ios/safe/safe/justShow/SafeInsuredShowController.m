//
//  SafeInsuredShowController.m
//  ecard
//
//  Created by 任雪亮 on 16/3/8.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "SafeInsuredShowController.h"
#import "SafeUtil.h"

@interface SafeInsuredShowController ()

@end

@implementation SafeInsuredShowController

@synthesize data=_data;
INIT_BUNDLE_CONTROLLER(SafeInsuredShowController, safebundle.bundle)
- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:_data.title];
    
    
}
- (IBAction)onCall:(id)sender {
    [CommonUtil makePhoneCall:@"4000040506" parent:self.view];
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
