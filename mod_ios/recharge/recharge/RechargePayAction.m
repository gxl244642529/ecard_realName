//
//  RechargePayAction.m
//  ecard
//
//  Created by 任雪亮 on 16/3/3.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "RechargePayAction.h"

@implementation RechargePayAction
-(void)prePay:(NSInteger)type orderId:(NSString *)orderId delegate:(id<DMJobDelegate>)delegate{
    
    DMApiJob* task = [self createApi:@"recharge/prePay"];
    [task put:@"id" value:orderId];
    [task put:@"type" value:[NSNumber numberWithInteger:type]];
    task.crypt = CryptType_Both;
    task.delegate = delegate;
    task.waitingMessage = @"请稍候...";
    [task execute];
}

-(void)getOrderInfo:(NSString *)orderId info:(id)info delegate:(id<DMJobDelegate>)delegate{
    
    
    DMApiJob* task = [self createApi:@"recharge/clientNotify"];
    [task put:@"id" value:orderId];
    [task put:@"info" value:info];
    task.crypt = CryptType_Both;
   // task.clazz = [SafePayResult class];
    task.delegate = delegate;
    task.waitingMessage = @"正在拉取订单信息...";
    [task execute];
    
    
}

@end
