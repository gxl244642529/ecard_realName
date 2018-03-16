//
//  UpdatePasswordController.m
//  eCard
//
//  Created by randy ren on 14-3-3.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "UpdatePasswordController.h"

#import "ViewUtil.h"

@interface UpdatePasswordController ()
{
    NSString* _password;
}
@property (weak, nonatomic) IBOutlet UITextField *txtOldPwd;
@property (weak, nonatomic) IBOutlet UITextField *txtNewPwd;
@property (weak, nonatomic) IBOutlet UIButton *btnUpdate;
@property (weak, nonatomic) IBOutlet UIButton *btnShowOrHide;
@end

@implementation UpdatePasswordController
INIT_BUNDLE_CONTROLLER(UpdatePasswordController, ecardlibbundle.bundle)

- (void)dealloc
{
    _password = nil;
}


- (void)viewDidLoad
{
    [super viewDidLoad];
    [self setTitle:@"修改密码"];
    //[ViewUtil setButtonBg:_btnUpdate];
}



-(void)submit:(id<DMSubmit>)submit completeData:(NSMutableDictionary*)data{
    _password = data[@"newPass"];
}

- (IBAction)onShowOrHide:(id)sender {
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
}

API_JOB_SUCCESS(user, updatePass, id){
    [Alert toast:@"修改密码成功"];
    [DMAccount current].userPwd = _password;
    [[DMAccount current] save];
    [self.navigationController popViewControllerAnimated:YES];
}



/*
-(void)task:(NSObject<IJsonTask> *)task result:(id)result{
    [Alert cancelWait:self.view];
    [Alert alert:@"修改密码成功"];
  //  [[ECardTaskManager sharedInstance]userInfo].userPwd =_password;
    [[DMAccount current]save];
    [self finish];
}
-(void)task:(NSObject<IJsonTask> *)task error:(NSString *)errorMessage isNetworkError:(BOOL)isNetworkError{
     [Alert cancelWait:self.view];
    [Alert alert:errorMessage];
}
*/
@end
