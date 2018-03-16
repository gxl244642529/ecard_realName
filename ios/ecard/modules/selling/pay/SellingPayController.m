



//
//  SPayController.m
//  ecard
//
//  Created by randy ren on 15/4/2.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "SellingPayController.h"
#import "PayModel.h"
#import "SPayCell.h"

#import "SOrderResultController.h"


@interface SellingPayController ()
{
    TableDataAdapter* _adapter;
    PayModel* _payModel;
    __weak IBOutlet UIButton *_btnPay;
    __weak IBOutlet UITableView *_tableView;
    __weak IBOutlet UILabel *_totalPay;
}
@end

@implementation SellingPayController


-(void)dealloc{
    _adapter = nil;
    _payModel = nil;
    [_payModel cancel];
    self.orderId = nil;
}
+(void)pay:(UIViewController*)parent id:(NSString*)orderId total:(NSInteger)total backController:(UIViewController*)backController delegate:(NSObject<PaySuccessDelegate>*)delegate{
    SellingPayController* f = [[SellingPayController alloc]initWithNibName:@"SellingPayController" bundle:nil];
    f.total = total;
    f.orderId = orderId;
    f.backController = backController;
    f.delegate = delegate;
     [parent.navigationController pushViewController:f animated:YES];
}
- (IBAction)onPay:(id)sender {
    [_btnPay setEnabled:NO];
    [_payModel pay:self.orderId type:self.payType];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"收银台"];
    
    _payModel = [PayModel sharedInstance];
    [_payModel setDelegate:self];
    
    _adapter= [[ TableDataAdapter alloc]init];
    [_adapter setScrollView:_tableView];
    [_adapter registerCell:@"SPayCell" height:44 bundle:nil];
    [_adapter setListener:self];
    [_adapter setData:@[
                        
                        @{
                            @"icon":@"s_pay_1",
                            @"title":@"支付宝",
                            @"type":[NSNumber numberWithInt:PAY_ALIPAY]
                            }
                        
                        
                        ]];
    [_adapter setOnItemClickListener:self];
    self.payType =PAY_ALIPAY;
    //记录
    NSUserDefaults* def = [NSUserDefaults standardUserDefaults];
    id payType = [def valueForKey:@"pay_type"];
    PayType pt = PAY_ALIPAY;
    if(payType){
        pt = [payType intValue];
     }
    NSInteger index = 0;
    for (NSObject* data in _adapter.array) {
        if([[data valueForKey:@"type"]intValue]==pt){
            self.payType =pt;
            [_tableView selectRowAtIndexPath:[NSIndexPath indexPathForRow:index inSection:0] animated:YES scrollPosition:UITableViewScrollPositionMiddle];
            break;
        }
        
        index++;
    }

    _totalPay.text = [NSString stringWithFormat:@"%0.2f",(float)self.total / 100];
    
}



-(void)onPaySuccess:(ECardRPayModel *)model{
    [SVProgressHUD showSuccessWithStatus:@"支付成功"];
    [_btnPay setEnabled:YES];
    [self.delegate onPaySuccess:self.orderId];

    
    
    UINavigationController* nav = self.navigationController;
    [self.navigationController popToViewController:self.backController animated:NO];
    SOrderResultController* result = [[SOrderResultController alloc]init];
    result.data =_totalPay.text;
    
    [nav pushViewController:result animated:YES];
}
-(void)onPayError:(ECardRPayModel *)model code:(NSInteger)code message:(NSString *)message{
    [_btnPay setEnabled:YES];
}
-(void)onPayCancel:(ECardRPayModel *)model{
    [_btnPay setEnabled:YES];
}

-(void)backToPrevious:(id)sender{
    //放弃付款
    [Alert confirm:@"放弃支付吗?" title:@"放弃确认" delegate:self];
    
}

// Called when a button is clicked. The view will be automatically dismissed after this call returns
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    if(buttonIndex==1){
        [self.navigationController popToViewController:self.backController animated:YES];
    }
}

-(void)onItemClick:(UIView*)parent data:(NSObject*)data index:(NSInteger)index{
    //记录
    NSUserDefaults* def = [NSUserDefaults standardUserDefaults];
    [def setValue:[NSNumber numberWithInteger:[[data valueForKey:@"type"]intValue]] forKey:@"pay_type"];
    [def synchronize];
    
    self.payType =[[data valueForKey:@"type"]intValue];
    
}


-(void)onInitializeView:(UIView*)parent cell:(SPayCell*)cell data:(NSObject*)data index:(NSInteger)index{
    cell.icon.image = [UIImage imageNamed:[data valueForKey:@"icon"]];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    cell.payType = [[data valueForKey:@"type"]intValue];
    
    cell.title.text = [data valueForKey:@"title"];
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
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
