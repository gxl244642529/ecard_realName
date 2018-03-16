//
//  SOrderConfirmController.m
//  ecard
//
//  Created by randy ren on 15/4/1.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "SOrderConfirmController.h"
#import <ecardlib/ecardlib.h>
#import "SOrderCardCell.h"
#import "ECardTaskManager.h"
#import "SAddrListController.h"
#import "OrderModel.h"
#import "CheckBox.h"

@interface SOrderConfirmController ()
{
     ObjectJsonTask* _task;
    __weak IBOutlet UITableView *_tableView;
    __weak IBOutlet UILabel *txtTotalFee;
    __weak IBOutlet UILabel *txtTransFee;
    __weak IBOutlet ItemView *_managerAddr;
    __weak IBOutlet ItemView *_addAddr;
    __weak IBOutlet UILabel *txtName;
    __weak IBOutlet UILabel *txtPhone;
    __weak IBOutlet UILabel *txtAddr;
    __weak IBOutlet UILabel *txtRealFee;
    __weak IBOutlet UILabel *totalCount;
    __weak IBOutlet NSLayoutConstraint *tableHeight;
    TableDataAdapter* _adapter;
    NSArray* _products;
    
    __weak IBOutlet UIButton *_btnPay;
    __weak IBOutlet CheckBox*_invoice;
    
    ObjectJsonTask* _postTask;
    
}
@end

@implementation SOrderConfirmController


-(void)updateViewConstraints{
    [super updateViewConstraints];
    tableHeight.constant = _products.count * 72;
}

-(void)dealloc{
    [[OrderModel sharedInstance]onDestroy:_task];
    _postTask = nil;
    _task = nil;
    _products = nil;
    self.addr = nil;
    _adapter = nil;
    [SVProgressHUD dismiss];
}


- (void)viewDidLoad {
    [super viewDidLoad];
  [self.view setBackgroundColor:RGB_WHITE(ff)];
  _invoice.hidden = YES;
    [self setTitle:@"订单确认"];
    [_addAddr setTarget:self withAction:@selector(onAddAddr:)];
    [_managerAddr setTarget:self withAction:@selector(onManager:)];
    
    [self updateAddr];
    
    NSArray* list;
    if([self.data isKindOfClass:[NSArray class]]){
        list = self.data;
    }else{
        list = [[NSArray alloc]initWithObjects:self.data, nil];
    }
    _products = list;
    createTableDataAdapter(_adapter,_tableView,SOrderCardCell,72)
    [_adapter setData:list];
    
    int count = 0;
    float totalFee = 0;
    for (CartVo* vo in list) {
        count += vo.count;
        totalFee += vo.totalPrice;
    }
    self.transFee = 0;
    self.totalFee = totalFee;
    totalCount.text = [NSString stringWithFormat:@"共%d张卡",count];
    txtTotalFee.text = [NSString stringWithFormat:@"%.2f",totalFee];
    
    //[[OrderModel sharedInstance]loadPost:self.addr.ID];
    
    _postTask = [[JsonTaskManager sharedInstance]createObjectJsonTask:@"s_order_post" cachePolicy:DMCachePolicy_NoCache];
    [_postTask setWaitMessage:@"请稍等..."];
    [_postTask setListener:self];
    if(self.addr){
        [self loadPost];
    }
}

-(void)updateTransFee{
    float realFee = self.totalFee + self.transFee / 100;
    txtRealFee.text =[NSString stringWithFormat:@"%.2f",realFee];
    txtTransFee.text =[NSString stringWithFormat:@"%.2f",self.transFee/100];
}

-(void)loadPost{
    [_postTask put:@"address_id" value:[NSString stringWithFormat:@"%d",self.addr.ID]];
    [_postTask execute];
    [_btnPay setEnabled:NO];
}

-(void)onInitializeView:(UIView*)parent cell:(SOrderCardCell*)cell data:(CartVo*)data index:(NSInteger)index{
    if([data isKindOfClass:[DiyVo class]]){
        DiyVo* diyVo = (DiyVo*)data;
        [cell.image setImage:[UIImage imageWithContentsOfFile:diyVo.thumb]];
    }else{
        [[ECardTaskManager sharedInstance]setImageSrcDirect:cell.image src:data.thumb];
    }
    
    
    
    cell.title.text = data.title;
    cell.price.text = [data priceString];
    cell.recharge.text = [data rechargeString];
    cell.count.text = [data countString];
}

-(void)updateAddr{
    if(self.addr){
        _managerAddr.hidden = NO;
        _addAddr.hidden = YES;
        
        SAddrListVo* data = self.addr;
        txtName.text = data.name;
        txtPhone.text = data.phone;
        txtAddr.text = [data getDetalAddr];
        
    }else{
        _managerAddr.hidden = YES;
        _addAddr.hidden = NO;
    }

   
}

-(void)onAddAddr:(id)sneder{
    SAddrListController* list = [[SAddrListController alloc]init];
    [self openControllerForResult:list requestCode:1 data:nil modal:YES];
    
    
}

-(void)onManager:(id)sender{
    SAddrListController* list = [[SAddrListController alloc]init];
    [self openControllerForResult:list requestCode:1 data:nil modal:YES];
    
}

-(void)onControllerResult:(NSInteger)resultCode requestCode:(NSInteger)requestCode data:(SAddrListVo*)data{
    if(resultCode==RESULT_OK){
        if(requestCode == 1){
            
            if(data){
                if(!self.addr  || self.addr.ID != data.ID){
                    self.addr = data;
                    [self loadPost];
                }else{
                    self.addr = data;
                }
                
                [self updateAddr];
            }
        }
    }
}


- (IBAction)onSubmit:(id)sender {
    if(!self.addr){
        [SVProgressHUD showErrorWithStatus:@"请选择收货地址"];
        return;
    }
    __weak UIButton* __btnPay = _btnPay;
    __weak SOrderConfirmController* __self = self;
    CartVo* cartVo = _adapter.array[0];
    
    //提交订单
    [_btnPay setEnabled:NO];
    _task = [[OrderModel sharedInstance]submit:self.addr.ID title:cartVo.title list:_adapter.array invoice:_invoice.selected completion:^(id result){
         [SellingPayController pay:__self id:[NSString stringWithFormat:@"%@",[result valueForKey:@"order_id"]] total:[[result valueForKey:@"real_price"]intValue] backController:__self.backController delegate:__self];
        
        [__btnPay setEnabled:YES];
    } fail:^{
        [__btnPay setEnabled:YES];
    }];
    
}


-(void)onPaySuccess:(id)data{
    
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)task:(id)task error:(NSString *)errorMessage isNetworkError:(BOOL)isNetworkError{
    if(task==_postTask){
        [SVProgressHUD showErrorWithStatus:errorMessage];
    }
}

-(void)task:(id)task result:(id)result{
    if(task==_postTask){
        [SVProgressHUD dismiss];
        [_btnPay setEnabled:YES];
        NSLog(@"%@",result);
        self.transFee = [result floatValue];
        [self updateTransFee];
    }
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
