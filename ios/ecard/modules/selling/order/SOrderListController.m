//
//  SOrderListController.m
//  ecard
//
//  Created by randy ren on 15-1-8.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "SOrderListController.h"
#import "SOrderDetailController.h"
#import <ecardlib/ecardlib.h>
#import "OnClickListenerExt.h"
#import "SOrderCell.h"
#import "SellingModel.h"

#import "OrderModel.h"

@interface SOrderListController ()
{
    PullToRefreshTableView* _tableView;
    __weak ObjectJsonTask* _confirmTask;
}
@end

@implementation SOrderListController
-(void)dealloc{
     [[OrderModel sharedInstance]onDestroy:_confirmTask];
    _tableView = nil;
    
   
}


-(id)init{
  if(self = [super init]){
    
    self.state = -1;
  }
  return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    
    
    [self setTitle: self.state < 0 ? @"全部订单" : [SellingModel getState:self.state]];
    
    _tableView = [[PullToRefreshTableView alloc]initWithFrame:self.view.bounds];
    [self.view addSubview:_tableView];
    [_tableView setEnableState:YES];
    _tableView.rowHeight = 105;
    [_tableView registerCell:@"SOrderCell"];
    [_tableView setOnItemClickListener:self];
    [_tableView setPullToRefreshListener:self];

    
    [[OrderModel sharedInstance]getList:self state:self.state];
}


-(void)task:(id)task result:(NSArray*)result position:(NSInteger)position isLastPage:(BOOL)isLastPage{
    [_tableView task:task result:result position:position isLastPage:isLastPage];
}
-(void)task:(id)task error:(NSString *)errorMessage isNetworkError:(BOOL)isNetworkError{
    [_tableView task:task error:errorMessage isNetworkError:isNetworkError];
}


-(void)onItemClick:(UIView*)parent data:(NSObject*)data index:(NSInteger)index{
    SOrderDetailController* controller = [[SOrderDetailController alloc]init];
    controller.data = data;
    controller.delegate = self;
    [self.navigationController pushViewController:controller animated:YES];
}

-(void)onInitializeView:(UIView*)parent cell:(SOrderCell*)cell data:(SOrderListVo*)data index:(NSInteger)index{
    cell.selectionStyle =UITableViewCellSelectionStyleNone;
    [JsonTaskManager setImageSrcDirect:cell.image src:data.img];
    cell.title.text = data.title;
    cell.total.text =[NSString stringWithFormat:@"共%d张卡", data.count];
    cell.price.text =data.realPriceString;
    cell.status.text = [SellingModel getState:data.status];
    cell.btnPay.tag = index;
    cell.btnConfirm.tag = index;
    Control_AddTarget(cell.btnPay, onPay);
    Control_AddTarget(cell.btnConfirm, onConfirm);
    
    if(data.status == ORDER_NOPAY){
        [cell.btnPay setHidden:NO];
    }else{
        [cell.btnPay setHidden:YES];
    }
    
    if(data.status == ORDER_DELIVERED){
        [cell.btnConfirm setHidden:NO];
    }else{
        [cell.btnConfirm setHidden:YES];
    }
}

-(void)onLoadData:(NSInteger)position{
    [[OrderModel sharedInstance]reloadList];
}

-(void)onPay:(UIView*)sender{
    
    SOrderListVo* data = [_tableView.adapter getItem:sender.tag];
    [SellingPayController pay:self id:data.ID total:data.realPrice backController:self delegate:self];
}
-(void)onPaySuccess:(id)data{
  [_tableView onLoadingRefresh:nil];
    
    
    [[OrderModel sharedInstance]reloadList];
}
-(void)onConfirm:(UIView*)sender{
    [Alert confirm:@"确认收到货了吗?" title:@"收货确认" delegate:self tag:sender.tag];
}

// Called when a button is clicked. The view will be automatically dismissed after this call returns
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    if(buttonIndex==1){
        
         SOrderListVo* data = [_tableView.adapter getItem:alertView.tag];
        _confirmTask = [[OrderModel sharedInstance]confirmRecv:data.ID completion:^{
            
        }];
    }
}



- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
