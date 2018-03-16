//
//  BasePayAction.m
//  ecard
//
//  Created by randy ren on 15/8/28.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "BasePayAction.h"
#import "JsonTaskManager.h"


@interface BasePayAction()
{
    ObjectJsonTask* _prePayTask;
    ObjectJsonTask* _notifyServerTask;
}

@end

@implementation BasePayAction

-(void)dealloc{
    _notifyServerTask = nil;
    _prePayTask = nil;
    [Alert cancelWait:nil];
}


-(id)initWithPrePay:(NSString*)prePayApi notifyServerApi:(NSString*)notifyServerApi{
    if(self = [super init]){
        _prePayApi = prePayApi;
        _notifyServerApi = notifyServerApi;
    }
    return self;
}

-(void)prePay:(PayType)type orderID:(NSString*)orderID listener:(id<IPayActionListener>)listener{
    if(!_prePayTask)
    {
        
        _prePayTask = [[JsonTaskManager sharedInstance]createObjectJsonTask:_prePayApi cachePolicy:DMCachePolicy_NoCache];
        _prePayTask.errorListener = ^(NSString* error,BOOL isNetwork){
            [Alert cancelWait:nil];
            [Alert alert:error];
        };
        [_prePayTask setWaitMessage:@"请稍等..."];
    }
    __weak id<IPayActionListener> __listener = listener;
    _prePayTask.successListener = ^(id result){
        [Alert cancelWait:nil];
        [__listener onPrePaySuccess:result];
    };
    
    
    [_prePayTask put:@"id" value:orderID];
    [_prePayTask put:@"type" value:ITOA(type)];
    [_prePayTask execute];
}

-(void)notifyServer:(PayType)type info:(NSString*)info listener:(id<IPayActionListener>)listener{
    if(!_notifyServerTask)
    {
        
        _notifyServerTask = [[JsonTaskManager sharedInstance]createObjectJsonTask:_notifyServerApi cachePolicy:DMCachePolicy_NoCache];
        _notifyServerTask.errorListener = ^(NSString* error,BOOL isNetwork){
            [Alert cancelWait:nil];
            [Alert alert:error];
        };
        [_notifyServerTask setWaitMessage:@"请稍等..."];
    }
    __weak id<IPayActionListener> __listener = listener;
    _notifyServerTask.successListener = ^(id result){
        [Alert cancelWait:nil];
        [__listener onNotifyServerSuccess:result];
    };
    
    NSLog(@"%@",info);
    [_notifyServerTask put:@"pay_info" value:info];
    [_notifyServerTask put:@"type" value:ITOA(type)];
    [_notifyServerTask execute];
}
@end
