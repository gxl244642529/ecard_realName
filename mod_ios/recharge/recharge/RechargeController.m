//
//  RechargeController.m
//  ecard
//
//  Created by randy ren on 16/2/20.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "RechargeController.h"
#import "RechargeModel.h"
#import "RechargeModel.h"
#import "RechargeButtonGroup.h"
#import "RechargeRecordController.h"
#import "RechargeSpotController.h"
#import "RechargeModel.h"
#import "RechargePayHandler.h"

#import "RechargeUnfinishedController.h"
#import "RechargeHelperController.h"

@interface RechargeController ()
{
    __weak IBOutlet RechargeButtonGroup *_group;
    __weak IBOutlet DMFormTextField *_txtCard;
    __weak UIButton* _clicked;
    __weak IBOutlet ECardPickerView *_cardPickerView;
    
    __weak IBOutlet UILabel *_txtInfo;
    __weak IBOutlet DMItem *btnBottom;
    RechargeModel* _model;
    RechargePayHandler* _handler;
}
@end

@implementation RechargeController

INIT_BUNDLE_CONTROLLER(RechargeController, rechargebundle.bundle)
-(void)dealloc{
    _model = nil;
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}
- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"卟噔充值"];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onWillShow:) name:UIKeyboardWillShowNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(onWillHide) name:UIKeyboardWillHideNotification object:nil];
    _model = [[RechargeModel alloc]init];
    _handler = [[RechargePayHandler alloc]initWithParent:self];
    _handler.paySuccessAction = PaySuccessAction_BacktoCurrentController;
    
    
    _cardPickerView.delegate = self;
    
    NSUserDefaults* def = [NSUserDefaults standardUserDefaults];
    NSString* cardId = [def valueForKey:@"recharge_card_id"];
    if(cardId){
        _txtCard.text = cardId;
    }else{
        [_group setEnabled:NO];
    }
    _txtInfo.text = @"为什么支付完成钱没有到卡里?";
    [btnBottom setTarget:self withAction:@selector(onBottom:)];
}

-(void)onBottom:(id)sender{
    Push_ViewController(RechargeHelperController);
}

-(void)didSelectECard:(ECard *)ecard{
     [_group setEnabled:YES];
}


-(void)onWillHide{
   
}


- (IBAction)onChanged:(UITextField*)sender {
    
    [_group setEnabled: sender.text.length > 0];
    
}

-(void)onWillShow:(NSNotification*)sender{
    NSValue *keyboardBoundsValue = [[sender userInfo] objectForKey:UIKeyboardFrameEndUserInfoKey];
    CGRect keyboardEndRect = [keyboardBoundsValue CGRectValue];
    float kbheight = keyboardEndRect.size.height;
    
}

- (IBAction)onRechargeList:(id)sender {
    
    Push_ViewController(RechargeRecordController);
}
- (IBAction)onRechargeSpot:(id)sender {
    
    Push_ViewController(RechargeSpotController);
}
- (IBAction)onEdit:(UITextField*)sender {
    
   // CGFloat top = sender.frame.origin.y + sender.frame.size.height;
    
    
    
}



- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (IBAction)onSubmit:(UIButton*)sender {
    if(_txtCard.text.length==0){
        [Alert alert:@"请输入或选择卡号"];
        return;
    }
    [_txtCard resignFirstResponder];
    if(![DMAccount isLogin]){
        _clicked = sender;
        [DMAccount callLoginController:self];
        return;
    }
    NSUserDefaults* def = [NSUserDefaults standardUserDefaults];
    [def setValue:_txtCard.text forKey:@"recharge_card_id"];
    [def synchronize];
    [self onPost:sender];
}
//登录成功

-(void)onPost:(UIButton*)sender{
    NSInteger fee = [[ sender titleForState:UIControlStateNormal] integerValue];
    NSString* cardId = _txtCard.text;
    [_model submit:cardId fee:fee button:_group];
}

API_JOB_SUCCESS(recharge, submit, id){
    
    [_handler initParam:[result[@"fee"]integerValue] orderId:result[@"order_id"]];
    
}

-(DMPopType)onLoginSuccess{
    
    [self onPost:_clicked];
    return DMPopBySelf;
}


-(void)onLoginCancel{
    
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
