//
//  RechargeRecordController.m
//  ecard
//
//  Created by 任雪亮 on 16/3/2.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "RechargeRecordController.h"
#import "RechargeVo.h"
#import "RechargeModel.h"
#import "RechargeSpotController.h"

@interface RechargeRecordController ()
{
    RechargeModel* _model;
    BOOL _isRefresh;
}
@property (weak, nonatomic) IBOutlet DMStateTabView *tabView;

@end

@implementation RechargeRecordController

INIT_BUNDLE_CONTROLLER(RechargeRecordController, rechargebundle.bundle)



- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"卟噔记录"];
    _tabView.dataSource = self;
    _model = [[RechargeModel alloc]init];
    _isRefresh = NO;
}

-(void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    if(_isRefresh){
        
        [_tabView reloadWithStatus];
        _isRefresh = NO;
    }
}

ON_ITEM_EVENT(btnGo, RechargeVo*){
    Push_ViewController(RechargeSpotController);
}
ON_ITEM_EVENT(btnInvoce, RechargeVo*){
    [Alert confirm:@"请到易通卡营业厅领取纸质发票。点击该按钮之前请确认是否已经领取到纸质发票？确认后该订单不可再次领取发票哦?" confirmListener:^(NSInteger buttonIndex) {
        if(buttonIndex==1){
            __weak typeof(self) __self = self;
            [__self doInvoce:data.tyId];
        }
    }];

}
-(void)doInvoce:(NSString*)tyId{
    [_model invoce:tyId button:nil];
}


ON_ITEM_EVENT(btnRefund, RechargeVo*){
    __weak typeof(self) __self = self;
    [Alert confirm:@"确认要退款吗?" confirmListener:^(NSInteger buttonIndex) {
        if(buttonIndex==1){
            
            [__self doRefund:data.tyId];
        }
    }];
}

-(void)doRefund:(NSString*)tyId{
    [_model refund:tyId button:nil];
}

API_JOB_SUCCESS(recharge, refund, id){
    
    if([DMJobManager sharedInstance].topController == self){
        [_tabView reloadWithStatus];
    }else{
        _isRefresh = YES;
    }
    
}


API_JOB_SUCCESS(recharge, invoce, id){
    
    if([DMJobManager sharedInstance].topController == self){
         [_tabView reloadWithStatus];
    }else{
        _isRefresh = YES;
    }
}


//获取下标
-(NSInteger)getDataIndex:(RechargeVo*)data{
    switch (data.status) {
        case RechargeStatus_NoPay:
        case RechargeStatus_Success:
            return 0;
        case RechargeStatus_Refund:
            return 1;
        case RechargeStatus_Finish:
        case RechargeStatus_Invoce:
            return 2;
    }
    return 0;
    
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
