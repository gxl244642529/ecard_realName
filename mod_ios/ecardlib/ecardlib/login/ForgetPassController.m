//
//  ForgetPassController.m
//  ecard
//
//  Created by randy ren on 16/2/20.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "ForgetPassController.h"
#import "CommonButton.h"
#import "DMCodeButton.h"
#import "LoginController.h"
#import "ECardUserInfo.h"


@interface ForgetPassController ()
@property (weak, nonatomic) IBOutlet CommonButton *btnRegister;
@property (weak, nonatomic) IBOutlet DMCodeButton *btnCode;
@property (weak, nonatomic) IBOutlet DMFormTextField *txtPhone;
@property (weak, nonatomic) IBOutlet DMFormTextField *txtCode;
@end

@implementation ForgetPassController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"获取密码"];
    
    _btnCode.btnSubmit = _btnRegister;
    _btnCode.txtPhone = _txtPhone;
    _btnCode.txtCode = _txtCode;
    
}

INIT_BUNDLE_CONTROLLER(ForgetPassController, ecardlibbundle.bundle)



-(BOOL)submit:(id<DMSubmit>)submit validate:(NSMutableDictionary *)data{
    if(submit == _btnRegister){
        
        NSString* pass1 = [data valueForKey:@"pass1"];
        NSString* pass = [data  valueForKey:@"pass"];
        if(![pass isEqualToString:pass1]){
            [Alert alert:@"重复密码和密码不一致"];
            return FALSE;
        }
        [data removeObjectForKey:@"pass1"];
        [data setValue:_btnCode.verifyId forKey:@"verify_id"];
        
    }
    
    return YES;
}


API_JOB_SUCCESS(security_api, cp_submit, id){
    [Alert toast:@"重置密码成功"];
    [self finish];
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
