//
//  RootViewController.m
//  ebusiness
//
//  Created by 任雪亮 on 16/10/25.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import "RootViewController.h"

@interface RootViewController ()

@end

@implementation RootViewController



-(instancetype)initWithCoder:(NSCoder *)aDecoder{
  if(self = [super initWithCoder:aDecoder]){
    self.navigationBar.translucent = NO;
    // 设置navigationBar是不是使用系统默认返回，默认为YES
    self.interactivePopGestureRecognizer.enabled = YES;
    // self.navigationBar.titleTextAttributes = dict;
  }
  return self;
}

- (instancetype)initWithRootViewController:(UIViewController *)rootViewController
{
  if (self = [super initWithRootViewController:rootViewController]) {
    self.navigationBar.translucent = NO;
    // 设置navigationBar是不是使用系统默认返回，默认为YES
    self.interactivePopGestureRecognizer.enabled = YES;
    // self.navigationBar.titleTextAttributes = dict;
  }
  return self;
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
