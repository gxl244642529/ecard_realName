//
//  MainViewController.m
//  ebusiness
//
//  Created by 任雪亮 on 16/10/13.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import "MainViewController.h"
#import "ReactUtil.h"
#import "RCTAccountModule.h"
#import <ecardlib/ecardlib.h>

#import "RCTSysModule.h"

@interface MainViewController ()

@end

@implementation MainViewController

-(id)init{
  if(self = [super init]){
    self.automaticallyAdjustsScrollViewInsets = NO;
  }
  return self;
}


- (void)viewDidLoad {
  [super viewDidLoad];
  
  
   [[UIApplication sharedApplication] setStatusBarStyle:UIStatusBarStyleLightContent animated:NO];
  
}

API_JOB_SUCCESS(ecard, bind, id){
  
  //半丁成功
  NSLog(@"绑定成功");
  
  [[RCTSysModule runningInstance]notify:@"ecardUpdate" data:@{}];
  
  
}
API_JOB_SUCCESS(ecard, unbind, id){
   [[RCTSysModule runningInstance]notify:@"ecardUpdate" data:@{}];
  
}
API_JOB_SUCCESS(ecard, update, id){
   [[RCTSysModule runningInstance]notify:@"ecardUpdate" data:@{}];
}

API_JOB_SUCCESS(ecard, bindBarcode, id){
  
  //半丁成功
  
   [[RCTSysModule runningInstance]notify:@"ecardUpdate" data:@{}];
}



-(void)viewWillAppear:(BOOL)animated{
  [super viewWillAppear:animated];
  [self.navigationController setNavigationBarHidden:YES animated:NO];
  [[UIApplication sharedApplication] setStatusBarStyle:UIStatusBarStyleLightContent animated:NO];
  
  
  [RCTSysModule viewWillAppear];
}



-(void)viewWillDisappear:(BOOL)animated{
  [super viewWillDisappear:animated];
    [self.navigationController setNavigationBarHidden:NO animated:YES];
  [[UIApplication sharedApplication] setStatusBarStyle:UIStatusBarStyleDefault animated:NO];
  
  [RCTSysModule viewWillDespear];
  
  
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

ON_NOTIFICATION(logout, id){
  
  
  [RCTAccountModule onLogout];
  
}

API_JOB_SUCCESS(user, head, NSString*){
  ECardUserInfo* info = [DMAccount current];
  info.headImage = result;
  
  [info.data setValue:result forKey:@"head"];
  
  [RCTAccountModule changeHead:result];
  //保存一下
  
  
  
  //这里更新一下文件
  [info save];
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
