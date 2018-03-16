//
//  SMyView.m
//  ecard
//
//  Created by randy ren on 15/3/31.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "SMyView.h"
#import "SOrderListController.h"
#import "SCollectionController.h"
#import "SAddrListController.h"
#import "MineItemView.h"
#import <DMLib/DMLib.h>
@interface SMyView()
{
    
    __weak IBOutlet UIImageView *_head;
    __weak IBOutlet UILabel *_txtAccount;
    __weak IBOutlet TouchableView *_personalView;
    __weak IBOutlet UIView *_noLoginView;
    
    
    __weak UIViewController* _parent;
    
    
    __weak IBOutlet MineItemView *_orderState3;
    __weak IBOutlet MineItemView *_orderState1;
    __weak IBOutlet MineItemView *_orderState2;
    
    BOOL _error;
}
@property (weak, nonatomic) IBOutlet ItemView *btnAllOrder;
@property (weak, nonatomic) IBOutlet ItemView *btnCollection;

@end

@implementation SMyView



-(void)awakeFromNib{
  [super awakeFromNib];
    _head.userInteractionEnabled = YES;
    AddTapGestureRecognizer(_head, onLogin:)
}

-(void)setParent:(UIViewController *)parent{
    _parent = parent;
    [_btnAllOrder setLoginTarget:self parent:parent withAction:@selector(onAllOrder:)];
    [_btnCollection setLoginTarget:self parent:parent withAction:@selector(onCollection:)];
    [_personalView setLoginTarget:self parent:parent withAction:@selector(onPsersonal:)];
    
    _orderState1.tag = ORDER_NOPAY;
    _orderState2.tag = ORDER_PAYED;
    _orderState3.tag = ORDER_DELIVERED;
    
    [_orderState1 setLoginTarget:self parent:parent withAction:@selector(onStateOrder:)];
    [_orderState2 setLoginTarget:self parent:parent withAction:@selector(onStateOrder:)];
    [_orderState3 setLoginTarget:self parent:parent withAction:@selector(onStateOrder:)];
    
}

-(UIViewController*)parent{
    return _parent;
}

- (IBAction)onLogin:(id)sender {
    [[JsonTaskManager sharedInstance]checkLogin:_parent loginSuccess:nil];
}

-(void)onPsersonal:(id)sender{
  
  Class clazz = NSClassFromString(@"PersonalController");
  
    [_parent.navigationController pushViewController:[[clazz alloc]init] animated:YES];
}



-(void)onSetUserInfo:(ECardUserInfo*)userInfo{
    if(userInfo){
        _noLoginView.hidden = YES;
        _personalView.hidden = NO;
        _txtAccount.text = userInfo.userAccount;
    }else{
        [_orderState1 setCount:0];
        [_orderState2 setCount:0];
        [_orderState3 setCount:0];
        _noLoginView.hidden = NO;
        _personalView.hidden = YES;
    }

}

-(void)onViewWillAppear{
    
    if([[JsonTaskManager sharedInstance]isLogin]){
         [[OrderModel sharedInstance]getList:self state:-1];
    }
    
    
    [self onSetUserInfo:[DMAccount current]];
    
    
   
}

-(void)task:(id)task error:(NSString *)errorMessage isNetworkError:(BOOL)isNetworkError{
    _error = YES;
    [SVProgressHUD showErrorWithStatus:errorMessage];
    
}
-(void)task:(id)task result:(NSArray *)result position:(NSInteger)position isLastPage:(BOOL)isLastPage{
    
    int count1 = 0;
    int count2 = 0;
    int count3 = 0;
    
    for (SOrderListVo* data in result) {
        
        if(data.status == ORDER_NOPAY){
            count1 ++;
        }else if(data.status == ORDER_PAYED){
            count2 ++;
        }else if(data.status == ORDER_DELIVERED){
            count3++;
        }
    }
    
    [_orderState1 setCount:count1];
    [_orderState2 setCount:count2];
    [_orderState3 setCount:count3];
}


-(void)onStateOrder:(UIView*)sender{
    SOrderListController* controller = [[SOrderListController alloc]init];
    controller.state =(int)sender.tag;
    [_parent.navigationController pushViewController:controller animated:YES];
    
}
-(void)onAllOrder:(id)sender{
    SOrderListController* controller = [[SOrderListController alloc]init];
    controller.state =-1;
     [_parent.navigationController pushViewController:controller animated:YES];
   
}

-(void)onCollection:(id)sender{
    [_parent.navigationController pushViewController:[[SCollectionController alloc]init] animated:YES];
    
}



@end
