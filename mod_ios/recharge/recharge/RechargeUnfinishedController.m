//
//  RechargeUnfinishedController.m
//  recharge
//
//  Created by 任雪亮 on 16/9/25.
//  Copyright © 2016年 任雪亮. All rights reserved.
//

#import "RechargeUnfinishedController.h"
#import "RechargeRecordController.h"
#import "RechargeVo.h"
#import "RechargeModel.h"
#import "RechargeSpotController.h"
@interface RechargeUnfinishedController ()
{
    RechargeModel* _model;
    BOOL _isChanged;
}
@property (weak, nonatomic) IBOutlet DMStateList *tableView;

@end

@implementation RechargeUnfinishedController


-(void)dealloc{
    _model = nil;
}

INIT_BUNDLE_CONTROLLER(RechargeUnfinishedController, rechargebundle.bundle)

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"卟噔订单(未完成)"];
    _model = [[RechargeModel alloc]init];
   self.navigationItem.rightBarButtonItem= [DMViewController createTextItem:@"全部" target:self action:@selector(onAll)];
    
}

-(void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    if(_isChanged){
        
        [_tableView reloadWithState];
        
        _isChanged = NO;
    }
}

-(void)doRefund:(NSString*)tyId{
    [_model refund:tyId button:nil];
}

ON_ITEM_EVENT(btnGo, RechargeVo*){
    Push_ViewController(RechargeSpotController);
}

ON_ITEM_EVENT(btnRefund, RechargeVo*){
    __weak typeof(self) __self = self;
    [Alert confirm:@"确认要退款吗?" confirmListener:^(NSInteger buttonIndex) {
        if(buttonIndex==1){
            
            [__self doRefund:data.tyId];
        }
    }];
    
    
}


API_JOB_SUCCESS(recharge, refund, id){
    
    if([DMJobManager sharedInstance].topController == self){
        
        [Alert alert:@"退款成功"];
        
        [_tableView reloadWithState];
    }else{
        _isChanged = YES;
    }
    
}

-(void)onAll{
    Push_ViewController(RechargeRecordController);
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
