//
//  NewLoginController.m
//  ecard
//
//  Created by 任雪亮 on 17/4/24.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import "NewLoginController.h"

#import <ecardlib/ecardlib.h>

@interface NewLoginController ()
@property (weak, nonatomic) IBOutlet UITextField *_txtAccount;
@property (weak, nonatomic) IBOutlet UITextField *_txtPwd;

@end

@implementation NewLoginController

INIT_CONTROLLER(NewLoginController)

- (void)viewDidLoad {
  [super viewDidLoad];
  
  
  
  
}

-(void)jobSuccess:(DMApiJob*)request{
  [ECardUserInfo loginSuccess:request account:request.param[@"account"] pwd:request.param[@"pwd"]];
  
}

ON_LOGIN_SUCCESS{
  if(_delegate ){
    if([_delegate onLoginSuccess]== DMPopBySelf){
      if(self != [DMJobManager sharedInstance].topController){
        [self.navigationController popToViewController:[CommonUtil findPrevController:self] animated:YES];
      }else{
        [self.navigationController popViewControllerAnimated:YES];
      }
    }
    return;
  }else{
    [self.navigationController popViewControllerAnimated:YES];
  }
  
}


- (IBAction)onSubmit:(id)sender {
  
  NSString* account = __txtAccount.text;
  NSString* pwd = [ CommonUtil md5:__txtPwd.text];
  DMApiJob* job = [[DMJobManager sharedInstance]createJsonTask:@"login/login" cachePolicy:DMCachePolicy_NoCache server:1];
  
  job.crypt = CryptType_Both;
  [job put:@"platform" value:PLATFORM];
  [job put:@"version" value:APP_VERSION];
  [job put:@"pushID" value:[[DMJobManager sharedInstance]deviceID]];
  
  
   [job put:@"account" value:account];
   [job put:@"pwd" value:pwd];
  
  job.delegate = self;
  [job execute];
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
