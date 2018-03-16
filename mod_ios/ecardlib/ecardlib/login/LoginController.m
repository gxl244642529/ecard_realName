//
//  LoginController.m
//  ecard
//
//  Created by randy ren on 16/1/15.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "LoginController.h"
#import "RegisterController.h"
#import "ForgetPassController.h"
#import "CommonButton.h"
#import "ECardUserInfo.h"

@interface LoginController ()
@property (weak, nonatomic) IBOutlet CommonButton *btnLogin;

@end

@implementation LoginController


INIT_BUNDLE_CONTROLLER(LoginController, ecardlibbundle.bundle)

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"用户登录"];
}

-(void)jobSuccess:(DMApiJob*)request{
    [ECardUserInfo loginSuccess:request account:request.param[@"phone"] pwd:request.param[@"pwd"]];
}

-(void)popupToPrev{
    [self.navigationController popToViewController: [CommonUtil findPrevController:self] animated:YES];
   
    
}

ON_LOGIN_SUCCESS{
    if(_delegate ){
        if([_delegate onLoginSuccess]== DMPopBySelf){
            if(self != [DMJobManager sharedInstance].topController){
                [self.navigationController popToViewController:[CommonUtil findPrevController:self] animated:YES];
            }else{
                [self popupToPrev];
            }
        }
        return;
    }else{
        [self popupToPrev];
    }
    
}


- (IBAction)onRegister:(id)sender {
    Push_ViewController(RegisterController);
}

- (IBAction)onFogetPassword:(id)sender {
    Push_ViewController(ForgetPassController);
}


-(void)finish{
    if([_delegate respondsToSelector:@selector(onLoginCancel)]){
        [_delegate onLoginCancel];
    }
    
    [super finish];
}

-(BOOL)submit:(UIButton<DMSubmit>*)submit validate:(NSMutableDictionary *)data{
    return YES;
}

-(void)submit:(id<DMSubmit>)submit completeData:(NSMutableDictionary *)data{
    [data setValue:PLATFORM forKey:@"platform"];
    [data setValue:[NSNumber numberWithInt:[APP_VERSION intValue]] forKey:@"version"];
    [data setValue:[PushUtil getPushId] forKey:@"pushID"];
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
