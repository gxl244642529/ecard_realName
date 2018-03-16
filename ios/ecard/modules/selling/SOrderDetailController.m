//
//  SOrderDetailController.m
//  ecard
//
//  Created by randy ren on 15/4/2.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "SOrderDetailController.h"
#import "SellingModel.h"
#import "SOrderCardCell.h"

#import <ecardlib/ecardlib.h>

#import "NetState.h"
#import "OrderModel.h"

@interface SOrderDetailController ()
{
    ObjectJsonTask* _task;
    __weak ObjectJsonTask* _closeTask;
    __weak ObjectJsonTask* _confirmTask;
    
    TableDataAdapter* _adapter;
    
    
    
    __weak IBOutlet UILabel *_txtAddr;
    
    __weak IBOutlet UILabel *_phone;
    __weak IBOutlet UILabel *_name;
    __weak IBOutlet UILabel *_txtOrderId;
    __weak IBOutlet UILabel *_txtTime;
    
    __weak IBOutlet UIButton *_btnConfirm;
    __weak IBOutlet UILabel *_txtPostCode;
    __weak IBOutlet UILabel *_txtDevCode;
    __weak IBOutlet UILabel *_txtComp;
    __weak IBOutlet UILabel *_realFee;
    __weak IBOutlet UILabel *_totalFee;
    __weak IBOutlet UILabel *_txtTransFee;
    __weak IBOutlet UITableView *_tableView;
    __weak IBOutlet NSLayoutConstraint *_tableHeight;
    __weak IBOutlet UILabel *_statusText;
    __weak IBOutlet ItemView *_btnPhone;
}
@property (weak, nonatomic) IBOutlet UIButton *_btnPay;

@end

@implementation SOrderDetailController

-(void)dealloc{
    [[OrderModel sharedInstance]onDestroy:_closeTask];
    [[OrderModel sharedInstance]onDestroy:_confirmTask];
    
    _task = nil;
    _adapter = nil;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"订单详情"];
  self.view.backgroundColor = RGB_WHITE(ff);
    [_btnPhone setTarget:self withAction:@selector(onPhone:)];
    
    _task = [[JsonTaskManager sharedInstance]createObjectJsonTask:@"s_order_detail3" cachePolicy:DMCachePolicy_NoCache];
    
    [_task setDataID:[self.data valueForKey:@"ID"]];
    [_task setClass:[SOrderDetailVo class]];
    [_task setListener:self];
    
    
    _txtOrderId.text = [self.data valueForKey:@"ID"];
    SOrderListVo* data = self.data;
    [__btnPay setHidden:data.status != ORDER_NOPAY];
     _realFee.text = [data realPriceString];
    _adapter = [[TableDataAdapter alloc]init];
    [_adapter setScrollView:_tableView];
    _tableView.rowHeight =73;
    [_adapter registerCell:@"SOrderCardCell" bundle:nil];
    [_adapter setListener:self];
    
    [_task execute];
    
    
    [self updateState:data.status];
}

-(void)updateState:(int)state{
    if(state == ORDER_NOPAY || state == ORDER_PAYING){
        if(!self.navigationItem.rightBarButtonItem){
            UIButton* addButton = [[UIButton alloc]initWithFrame:CGRectMake(0, 0,60,22)];
            [addButton setTitle:@"取消订单" forState:UIControlStateNormal];
            [addButton.titleLabel setFont:[UIFont systemFontOfSize:13]];
            [addButton setTitleColor:[UIColor darkGrayColor] forState:UIControlStateNormal];
            self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc]initWithCustomView:addButton];
            
            Control_AddTarget(addButton, onClose);
        }
    }else{
        self.navigationItem.rightBarButtonItem = nil;
    }
    
    
    if(state==ORDER_NOPAY){
        [__btnPay setHidden:NO];
    }else{
        [__btnPay setHidden:YES];
    }
    if(state==ORDER_DELIVERED){
        [_btnConfirm setHidden:NO];
    }else{
        [_btnConfirm setHidden:YES];
    }
}

// Called when a button is clicked. The view will be automatically dismissed after this call returns
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    if(buttonIndex==1){
        __weak ObjectJsonTask* __task = _task;
        _closeTask = [[OrderModel sharedInstance]closeOrder:[self.data valueForKey:@"ID"] completion:^{
            [__task reload];
        }];
    }
}


- (IBAction)onConfirm:(id)sender {
    __weak ObjectJsonTask* __task = _task;
   _confirmTask= [[OrderModel sharedInstance]confirmRecv:[self.data valueForKey:@"ID"] completion:^{
         [__task reload];
    }];
}

-(void)onClose:(id)sender{
    [Alert confirm:@"确实要取消订单吗?" title:@"取消确认" delegate:self];
}

-(void)onInitializeView:(UIView*)parent cell:(SOrderCardCell*)cell data:(SOrdCrdVo*)data index:(NSInteger)index{
    [ECardTaskManager setImageSrcDirect:cell.image src:data.thumb];
    cell.title.text = data.title;
    cell.count.text = [NSString stringWithFormat:@"%d",data.count];
    cell.recharge.text = [data rechargeString];
    cell.price.text = [data priceString];
}
-(void)task:(id)task result:(SOrderDetailVo*)result{
    [SVProgressHUD dismiss];
    [_adapter setData:result.cards];
    _tableHeight.constant = result.cards.count * 73;
    [self updateViewConstraints];
    
    _statusText.text = [SellingModel getState:result.state];
    _txtTransFee.text = [result transFeeString];
    _totalFee.text = [result totalPriceString];
   
    _txtAddr.text = [result addr];
    
    _txtComp.text = result.deliverCompany;
    _txtTime.text = result.time;
    _txtDevCode.text = result.deliverCode;
    _name.text = result.name;
    _phone.text = result.phone;
    _txtPostCode.text = result.pcode;
    
    [self updateState:result.state];
}


-(void)onPhone:(id)sender{
    [CommonUtil makePhoneCall:@"05925195866" parent:self.view];
}

-(void)task:(id)task error:(NSString *)errorMessage isNetworkError:(BOOL)isNetworkError{
    [SVProgressHUD showErrorWithStatus:errorMessage];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}


- (IBAction)onPay:(id)sender {
    SOrderListVo* data = self.data;
    [SellingPayController pay:self id:[self.data valueForKey:@"ID"] total:data.realPrice backController:self delegate:self];
}

-(void)onPaySuccess:(id)data{
    [SVProgressHUD show];
     self.navigationItem.rightBarButtonItem = nil;
    [_task reload];
    [self.delegate onPaySuccess:data];
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
