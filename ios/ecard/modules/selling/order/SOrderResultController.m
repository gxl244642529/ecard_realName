//
//  SOrderResultController.m
//  ecard
//
//  Created by randy ren on 15/5/14.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "SOrderResultController.h"
#import "SOrderListController.h"
#import <ecardlib/ecardlib.h>
#import "SellingMainController.h"

@interface SOrderResultController ()
@property (weak, nonatomic) IBOutlet UILabel *txtSuccess;
@property (weak, nonatomic) IBOutlet UIButton *btnContinue;
@property (weak, nonatomic) IBOutlet UIButton *btnOrder;

@end

@implementation SOrderResultController

INIT_CONTROLLER(SOrderResultController)

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"付款成功"];
    
    _txtSuccess.text = [NSString stringWithFormat:@"成功付款￥%@", self.data];
    [ViewUtil setButtonBg:_btnContinue];
    [ViewUtil setButtonBg:_btnOrder];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (IBAction)onContinue:(id)sender {
    
    [self.navigationController popToViewController:[SellingMainController current] animated:YES];
    
}
- (IBAction)onOrder:(id)sender {
    
    UINavigationController* nav = self.navigationController;
    
    [self.navigationController popViewControllerAnimated:NO];
    
    SOrderListController* c = [[SOrderListController alloc]init];
    c.state = -1;
    
    [nav pushViewController:c animated:YES];
    
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
