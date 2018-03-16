//
//  RechargeDetailController.m
//  ecard
//
//  Created by 任雪亮 on 16/3/2.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "RechargeDetailController.h"
#import "RechargeModel.h"
#import "RechargeSpotController.h"
#import "RechargeButton.h"

@interface RechargeDetailController ()
{
    RechargeModel* _model;
}
@property (weak, nonatomic) IBOutlet RechargeButton *btnInvoce;
@end

@implementation RechargeDetailController

@synthesize data = _data;

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"卟噔详情"];
    _model = [[RechargeModel alloc]init];
    self.view.backgroundColor = RGB_WHITE(f2);
}

INIT_BUNDLE_CONTROLLER(RechargeDetailController, rechargebundle.bundle)

-(void)doInvoce{
    [_model invoce:self.data.tyId button:self.btnInvoce];
}


API_JOB_SUCCESS(recharge, invoce, id){
    
    [self finish];
    
}
- (IBAction)onInvoce:(id)sender {
    [Alert confirm:@"请到易通卡营业厅领取纸质发票。点击该按钮之前请确认是否已经领取到纸质发票？确认后该订单不可再次领取发票哦?" confirmListener:^(NSInteger buttonIndex) {
        if(buttonIndex==1){
            __weak typeof(self) __self = self;
            [__self doInvoce];
        }
    }];
    
    
    
}
- (IBAction)onGo:(id)sender {
    Push_ViewController(RechargeSpotController);
}
- (IBAction)onRefund:(id)sender {
    [Alert confirm:@"是否真的要退款?" confirmListener:^(NSInteger buttonIndex) {
        if(buttonIndex==1){
            __weak typeof(self) __self = self;
            [__self doRefund:sender];
        }
    }];
   
}

-(void)doRefund:(id)sender{
     [_model refund:_data.tyId button:sender];
}

API_JOB_SUCCESS(recharge, refund, id){
    
    [self.navigationController popViewControllerAnimated:YES];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
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
