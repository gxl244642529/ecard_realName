//
//  BasePayAction.m
//  MyProject
//
//  Created by 任雪亮 on 16/7/17.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import "DMBasePayAction.h"

@interface DMBasePayAction()<DMApiDelegate>
{
  __weak id<DMApiDelegate> _delegate;
}
@end


@implementation DMBasePayAction
-(void)prePay:(NSInteger)type orderId:(NSString *)orderId delegate:(id<DMJobDelegate>)delegate{
  
  DMApiJob* task = [self createApi:[NSString stringWithFormat:@"%@/prePay",_moduleId]];
  [task put:@"id" value:orderId];
  [task put:@"type" value:[NSNumber numberWithInteger:type]];
  task.crypt = CryptType_Both;
  task.delegate = delegate;
  task.waitingMessage = @"请稍候...";
  [task execute];
}

-(void)getOrderInfo:(NSString *)orderId info:(id)info delegate:(id<DMJobDelegate>)delegate{
  
  
  DMApiJob* task = [self createApi:[NSString stringWithFormat:@"%@/clientNotify",_moduleId]];
  [task put:@"id" value:orderId];
  [task put:@"info" value:info];
  task.crypt = CryptType_Both;
  task.delegate = delegate;
  task.waitingMessage = @"正在拉取订单信息...";
  [task execute];
}



-(void)jobSuccess:(DMApiJob*)request{
  NSDictionary* data = request.data;
  
  NSString* orderId = data[@"orderId"];
  NSInteger fee = [data[@"fee"]integerValue];
  
  [[DMPayModel runningInstance] setOrderId:orderId];
  [[DMPayModel runningInstance] setFee:fee];
  
  
  request.data = data[@"info"];
  
  [_delegate jobSuccess:request];
}

-(BOOL)jobError:(id)request{
  if([_delegate respondsToSelector:@selector(jobError:)]){
    return [_delegate jobError:request];
  }
  return FALSE;
}


-(BOOL)jobMessage:(id)request{
  if([_delegate respondsToSelector:@selector(jobMessage:)]){
     return [_delegate jobMessage:request];
  }
  return FALSE;

}


-(void)prePay:(NSInteger)type api:(NSString*)api data:(NSDictionary*)data delegate:(id<DMApiDelegate>)delegate{
  _delegate= delegate;
  
  DMApiJob* task = [self createApi:api];
  task.timeout = 8;
  [task putAll:[[NSMutableDictionary alloc ] initWithDictionary:data ]];
  task.crypt = CryptType_Both;
  task.delegate = self;
  [task execute];

  
  
}

@end
