//
//  SafeCashierController.m
//  ecard
//
//  Created by randy ren on 16/1/28.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "SafeCashierController.h"
#import "CashierCell.h"


@interface SafeCashierController ()
@property (weak, nonatomic) IBOutlet DMPayTableView *tableView;
@property (weak, nonatomic) IBOutlet UILabel *txtFee;

@end

@implementation SafeCashierController
INIT_BUNDLE_CONTROLLER(SafeCashierController, safebundle.bundle)
- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"收银台"];
    [_tableView setListener:self];
    
    _txtFee.text = [FeeUtil formatFee: [DMPayModel runningInstance].fee ];
}




-(void)dealloc{
    [Alert cancelWait];
}

-(void)onInitializeView:(UIView *)parent cell:(CashierCell*)cell data:(DMPay*)data index:(NSInteger)index{
    [cell setData:data];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (IBAction)onPay:(id)sender {
    [[DMPayModel runningInstance]prePay];
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
