//
//  SafePayAction.m
//  ecard
//
//  Created by randy ren on 16/1/28.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "SafePayAction.h"
#import "SafePayResult.h"


@implementation SafePayAction


-(void)prePay:(DMPayType)type orderId:(NSString *)orderId delegate:(id<DMJobDelegate>)delegate{
    
    DMApiJob* task = [self createApi:@"i_safe/prePay"];
    [task put:@"id" value:orderId];
    [task put:@"type" value:[NSNumber numberWithInteger:type]];
    task.crypt = CryptType_Both;
    task.delegate = delegate;
    task.waitingMessage = @"请稍候...";
    [task execute];
}

-(void)getOrderInfo:(NSString *)orderId info:(id)info delegate:(id<DMJobDelegate>)delegate{
    
    
    DMApiJob* task = [self createApi:@"i_safe/clientNotify"];
    [task put:@"id" value:orderId];
    [task put:@"info" value:info];
    task.crypt = CryptType_Both;
    task.clazz = [SafePayResult class];
    task.delegate = delegate;
    task.waitingMessage = @"正在拉取订单信息...";
    [task execute];

    
}


@end
