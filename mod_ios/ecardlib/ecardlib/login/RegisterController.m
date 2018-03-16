
//
//  RegisterController.m
//  ecard
//
//  Created by randy ren on 16/1/16.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "RegisterController.h"
#import "CommonButton.h"
#import "DMCodeButton.h"
#import "LoginController.h"
#import "ECardUserInfo.h"
#import "RegDirectController.h"


@interface RegisterController ()
@property (weak, nonatomic) IBOutlet CommonButton *btnRegister;
@property (weak, nonatomic) IBOutlet DMCodeButton *btnCode;
@property (weak, nonatomic) IBOutlet DMFormTextField *txtPhone;
@property (weak, nonatomic) IBOutlet DMFormTextField *txtCode;
@property (weak, nonatomic) IBOutlet UIButton *txnCannot;

@end

@implementation RegisterController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"注册"];
    _btnCode.btnSubmit = _btnRegister;
    _btnCode.txtPhone = _txtPhone;
    _btnCode.txtCode = _txtCode;
    _txnCannot.hidden = YES;
}


INIT_BUNDLE_CONTROLLER(RegisterController, ecardlibbundle.bundle)

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(BOOL)submit:(id<DMSubmit>)submit validate:(NSMutableDictionary *)data{
    if(submit == _btnRegister){
        
        NSString* pass1 = [data valueForKey:@"pass1"];
        NSString* pass = [data  valueForKey:@"pwd"];
        if(![pass isEqualToString:pass1]){
            [Alert alert:@"重复密码和密码不一致"];
            return FALSE;
        }
        
    }
    
    return YES;
}


-(void)submit:(id<DMSubmit>)submit completeData:(NSMutableDictionary *)data{
    if(submit == _btnRegister){
        [data setValue:_btnCode.verifyId forKey:@"verifyId"];
        [data removeObjectForKey:@"pass1"];
        [data setValue:PLATFORM forKey:@"platform"];
        [data setValue:[NSNumber numberWithInt:[APP_VERSION intValue]] forKey:@"version"];
        [data setValue:[[DMJobManager sharedInstance]pushID] forKey:@"pushID"];
    }
   
}


- (IBAction)onCannotRecv:(id)sender {
    Push_ViewController(RegDirectController);
}

-(void)jobSuccess:(DMApiJob*)request{
    if([request.api isEqualToString:@"passport/register"]){
        [ECardUserInfo loginSuccess:request account:request.param[@"phone"] pwd:request.param[@"pwd"]];
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
