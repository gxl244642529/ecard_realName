//
//  LostCardDetailController.m
//  ecard
//
//  Created by randy ren on 15/5/5.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "LostCardDetailController.h"

@interface LostCardDetailController ()
@property (weak, nonatomic) IBOutlet UILabel *info;

@end

@implementation LostCardDetailController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"失卡详情"];

    _info.text = self.data[@"INFO"];
    
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
