//
//  CommonApi.m
//  MyProject
//
//  Created by 任雪亮 on 16/6/15.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import "CommonApi.h"


Aes* aes;


@implementation CommonApi


+(void)initialize{
  
  aes = [[Aes alloc]initWithKey:@"0123456789123456"];
  
}

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(md5:(NSString *)str callback:(RCTResponseSenderBlock)callback)
{
  callback(@[[CommonUtil md5:str]]);
}


RCT_EXPORT_METHOD(exit){
  dispatch_async(dispatch_get_main_queue(), ^{
    [[DMJobManager sharedInstance].topController.navigationController popViewControllerAnimated:YES];
  });
  
}

RCT_EXPORT_METHOD(encrypt:(NSString *)str key:(NSString*)key callback:(RCTResponseSenderBlock)callback)
{
  @synchronized (aes) {
    [aes setKey:key];
    callback(@[ [aes encrpyt:str]]);
  }
}
RCT_EXPORT_METHOD(decrypt:(NSString *)str key:(NSString*)key callback:(RCTResponseSenderBlock)callback)
{
  @synchronized (aes) {
    [aes setKey:key];
    callback(@[ [aes decrpyt:str]]);
  }
}


RCT_EXPORT_METHOD(logout){
  [[DMJobManager sharedInstance]logout];
  
}

RCT_EXPORT_METHOD(toast:(NSString*)toast){
  [Alert toast:toast];
}

RCT_EXPORT_METHOD(wait:(NSString*)toast){
  [Alert wait:toast];
}

RCT_EXPORT_METHOD(cancelWait){
  [Alert cancelWait];
}


@end
