//
//  ECardPayController.m
//  ecardlib
//
//  Created by 任雪亮 on 16/10/18.
//  Copyright © 2016年 任雪亮. All rights reserved.
//


#define ECARD_PAY_ACCOUNT @"ecardPayAccount"


#import "ECardPayController.h"

@interface ECardPayModel : DMModel

-(void)pay:(NSString*)account pwd:(NSString*)pwd key:(NSString*)key
    platId:(NSString*)platId button:(UIButton*)button;


@end


@implementation ECardPayModel

-(void)pay:(NSString*)account pwd:(NSString*)pwd key:(NSString*)key
    platId:(NSString*)platId button:(UIButton*)button{
    
    DMApiJob* job = [self createApi:@"ecard_pay/pay"];
    NSString* cryptPwd = [CryptAES encrypt:pwd key:key];
    [job setButton:button];
    [job put:@"cardId" value:account];
    [job put:@"pwd" value: cryptPwd ];
    [job put:@"platId" value:platId];
    job.crypt = CryptType_Both;
    [job setWaitingMessage:@"请稍等..."];
    job.server = 1;
    
    [job execute];
    
    
}

@end





@interface ECardPayController ()
{
    ECardPayModel* _payModel;
}
@property (weak, nonatomic) IBOutlet UIButton *btnPay;
@property (weak, nonatomic) IBOutlet UILabel *txtFee;
@property (weak, nonatomic) IBOutlet DMFormTextField *txtAccount;
@property (weak, nonatomic) IBOutlet DMFormTextField *txtPwd;

@end

@implementation ECardPayController

INIT_BUNDLE_CONTROLLER(ECardPayController, ecardlibbundle.bundle)

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"后台账户支付"];
    _payModel = [[ECardPayModel alloc]init];
    NSDictionary* data = self.data;
    _txtFee.text =  [NSString stringWithFormat:@"¥%.02f",[data[@"fee"]doubleValue]];
    
    
    
    NSUserDefaults* def = [NSUserDefaults standardUserDefaults];
    NSString* account = [def valueForKey:ECARD_PAY_ACCOUNT];
    _txtAccount.text = account;

}

-(void)dealloc{
    _payModel = nil;
}

- (IBAction)onPay:(id)sender {
    
    NSString* account = _txtAccount.text;
    if([account isEqualToString:@""]){
        [Alert alert:@"请输入后台账号"];
        return;
    }
    
    
    NSUserDefaults* def = [NSUserDefaults standardUserDefaults];
    [def setValue:account forKey:ECARD_PAY_ACCOUNT];
    [def synchronize];
    
    
    
    NSString* pwd = _txtPwd.text;
    if([pwd isEqualToString:@""]){
        [Alert alert:@"请输入后台账户密码"];
        return;
    }
    
    NSDictionary* data = self.data;
    NSString* key = data[@"sign"];
    NSString* platId = data[@"platId"];
    [_payModel pay:account pwd:pwd key:key platId:platId button:_btnPay];
    
    
}



API_JOB_SUCCESS(ecard_pay, pay, id){
    
    [_payDelegate onPayResult:result];
    
    //[self finish];
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
