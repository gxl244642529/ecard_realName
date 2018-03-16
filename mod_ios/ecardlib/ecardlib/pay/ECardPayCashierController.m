//
//  ECardPayCashierController.m
//  ecardlib
//
//  Created by 任雪亮 on 16/9/25.
//  Copyright © 2016年 任雪亮. All rights reserved.
//

#import "ECardPayCashierController.h"
#import "CommonButton.h"
#import "ECardCashierCell.h"

@interface ECardPayCashierController ()
{
    TableDataAdapter* _adapter;
}
@property (weak, nonatomic) IBOutlet UILabel *txtFee;
@property (weak, nonatomic) IBOutlet PageButton *btnPay;
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *space;

@end

@implementation ECardPayCashierController



-(void)dealloc{
    _adapter = nil;
}

INIT_BUNDLE_CONTROLLER(ECardPayCashierController, ecardlibbundle.bundle)

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"收银台"];
    self.view.backgroundColor = RGB_WHITE(f2);
    DMPayModel* model= [DMPayModel runningInstance];
    _txtFee.text = [FeeUtil formatFee: model.fee ];
    
    
    TableDataAdapter* adapter = [[TableDataAdapter alloc]init];
    [adapter setScrollView:_tableView];
    [adapter registerCell:@"ECardCashierCell" bundle:CREATE_BUNDLE(ecardlibbundle.bundle)];
    _adapter = adapter;
    [adapter setListener:self];
    [adapter setData:[model getPayTypes]];
    [adapter setOnItemClickListener:self];
    
    NSIndexPath *ip=[NSIndexPath indexPathForRow:model.currentIndex inSection:0];
    [_tableView selectRowAtIndexPath:ip animated:YES scrollPosition:UITableViewScrollPositionBottom];
    
    Control_AddTarget(_btnPay, onPay);
}

-(void)onItemClick:(UIView*)parent data:(NSObject*)data index:(NSInteger)index{
     DMPayModel* model= [DMPayModel runningInstance];
    [model setCurrentIndex:index];
}
-(void)onPay:(id)sender{
     DMPayModel* model= [DMPayModel runningInstance];
    [model prePay];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(void)onInitializeView:(UIView *)parent cell:(ECardCashierCell*)cell data:(DMPay*)data index:(NSInteger)index{
    [cell setData:data];
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
