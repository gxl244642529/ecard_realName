//
//  RechargeSuccessController.m
//  ecard
//
//  Created by 任雪亮 on 16/3/3.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "RechargeSuccessController.h"
#import "RechargeSpotController.h"
#import "RechargeHelperController.h"



@interface RechargeSuccessController ()
@property (weak, nonatomic) IBOutlet DMItem *onGoto;
@property (weak, nonatomic) IBOutlet UILabel *txtCardId;
@property (weak, nonatomic) IBOutlet UILabel *txtFee;

@end

@implementation RechargeSuccessController

INIT_BUNDLE_CONTROLLER(RechargeSuccessController, rechargebundle.bundle)
- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"支付成功"];
    
    [_onGoto setTarget:self withAction:@selector(onGoto:)];
    _txtCardId.text = self.data[@"cardId"];
    _txtFee.text = [FeeUtil formatFee:[self.data[@"fee"]integerValue]];
}

-(void)onGoto:(id)sender{
    Push_ViewController(RechargeSpotController);
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (IBAction)onRechargeHelper:(id)sender {
    
    Push_ViewController(RechargeHelperController);
    
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
