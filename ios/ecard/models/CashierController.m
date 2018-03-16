//
//  CashierController.m
//  ecard
//
//  Created by randy ren on 15/8/27.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "CashierController.h"
#import "RPay.h"
#import "SPayCell.h"

@interface CashierController ()
{
    __weak ECardRPayModel* _model;
    TableDataAdapter* _adapter;
}
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (weak, nonatomic) IBOutlet UILabel *totalPrice;

@end

@implementation CashierController

-(void)dealloc{
    _adapter = nil;
}


-(id)initWithModel:(ECardRPayModel*)model{
    if(self = [super initWithNibName:@"CashierController" bundle:nil]){
        _model = model;
    }
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"收银台"];
    
    _adapter = [[TableDataAdapter alloc]init];
    [_adapter setScrollView:_tableView];
    [_adapter registerCell:@"SPayCell" height:44 bundle:nil];
    [_adapter setListener:self];
    [_adapter setOnItemClickListener:self];
    [_adapter setData:[_model supportPayTyptes:@[[NSNumber numberWithInteger:PAY_ALIPAY],
                                                 [NSNumber numberWithInteger:PAY_ETONGKA]
                                                 ]]];
    
    [_tableView selectRowAtIndexPath:[NSIndexPath indexPathForRow:_model.currentIndex inSection:0] animated:YES scrollPosition:UITableViewScrollPositionMiddle];
    
    CashierInfo* info = _model.cashierInfo;
    _totalPrice.text = [info formatFee];
}

-(void)onInitializeView:(UIView*)parent cell:(SPayCell*)cell data:(PayTypeInfo*)data index:(NSInteger)index{
    cell.icon.image = [UIImage imageNamed:data.icon];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    cell.title.text =data.text;
}


-(void)onItemClick:(UIView*)parent data:(NSObject*)data index:(NSInteger)index{
    [_model setCurrentPayIndex:index];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}
- (IBAction)onPay:(id)sender {
    [_model prePay];
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
