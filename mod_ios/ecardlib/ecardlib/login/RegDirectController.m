//
//  RegDirectController.m
//  ecard
//
//  Created by randy ren on 16/1/22.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "RegDirectController.h"
#import "ECardUserInfo.h"
#import "LoginController.h"

@interface RegDirectController ()

@end

@implementation RegDirectController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"注册"];
}

INIT_BUNDLE_CONTROLLER(RegDirectController, ecardlibbundle.bundle)

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(BOOL)submit:(id<DMSubmit>)submit validate:(NSMutableDictionary *)data{
    NSString* pass1 = [data valueForKey:@"pass1"];
    NSString* pass = [data  valueForKey:@"pwd"];
    if(![pass isEqualToString:pass1]){
        [Alert alert:@"重复密码和密码不一致"];
        return FALSE;
    }
    


    return YES;
}


-(void)submit:(id<DMSubmit>)submit completeData:(NSMutableDictionary *)data{
    [data setValue:PLATFORM forKey:@"platform"];
    [data setValue:APP_VERSION forKey:@"version"];
    [data setValue:[[DMJobManager sharedInstance]deviceID] forKey:@"pushID"];
}

-(void)jobSuccess:(DMApiJob*)request{
    [ECardUserInfo loginSuccess:request account:request.param[@"phone"] pwd:request.param[@"pwd"]];
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
