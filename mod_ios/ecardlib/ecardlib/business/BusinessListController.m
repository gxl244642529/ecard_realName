//
//  BusinessListController.m
//  ecard
//
//  Created by randy ren on 16/2/22.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "BusinessListController.h"

@interface BusinessListController ()
@property (weak, nonatomic) IBOutlet DMStateList *list;

@end

@implementation BusinessListController

INIT_BUNDLE_CONTROLLER(BusinessListController, ecardlibbundle.bundle)
- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"商家列表"];
    
    NSString* type;
    NSInteger tag = [self.data integerValue];
    switch (tag) {
        case 6:
            type = nil;
            break;
        case 7:
            type = nil;
            break;

        default:
            type = [NSString stringWithFormat:@"%d",(int)(tag+10)];
            break;
    }
    [_list put:@"type" value:type];
    [_list execute];
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
