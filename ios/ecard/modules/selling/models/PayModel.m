//
//  PayModel.m
//  ecard
//
//  Created by randy ren on 15-2-5.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "PayModel.h"
#import <ecardlib/ecardlib.h>


#import <AlipaySDK/AlipaySDK.h>

@interface PayModel()
{
    ObjectJsonTask* _task;
    ObjectJsonTask* _notifyTask;
}

@end

@implementation PayModel

IMPLEMENT_SHARED_INSTANCE_DIRECT(PayModel)

-(void)dealloc{
    _task = nil;
    _notifyTask = nil;
}

-(id)init{
    if(self= [super init]){
        

    }
    return self;
}

-(void)cancel{
    _task = nil;
}

-(void)pay:(id)ID type:(PayType)type{
    if(!_task){
        _task = [[JsonTaskManager sharedInstance]createObjectJsonTask:@"s_order_pay3" cachePolicy:DMCachePolicy_NoCache];
        [_task setWaitMessage:@"正在付款..."];
        [_task setListener:self];
    }
    self.type =type;
    
    [_task put:@"id" value:ID];
    [_task put:@"type" value:[NSNumber numberWithInt:type]];
    [_task execute];
}

-(void)onPayResult:(id)result{
    int status = [[result valueForKey:@"resultStatus"]intValue];
    if(status == 9000){
        //成功
        [self onPaySuccess:result[@"result"]];
    }else if(status== 6001){
        //取消
        [self.delegate onPayCancel:nil];
    }else if(status == 6002){
        //网络连接错误
        [self.delegate onPayError:nil code:status message:@"网络连接错误"];
    }else if(status == 8000){
        //处理中
    }else if(status==4000){
        //失败
         [self.delegate onPayError:nil code:status message:@"付款失败,请稍候重试"];
    }

}
-(void)onPayCancel{
     [self.delegate onPayCancel:nil];
}

-(void)task:(NSObject<IJsonTask> *)task result:(id)result{
    [SVProgressHUD dismiss];
    if(task==_task){
        __weak PayModel* __self = self;
        [[AlipaySDK defaultService] payOrder:result fromScheme:PACKAGE callback:^(NSDictionary *resultDic) {
            [__self onPayResult:resultDic];
        }];
    }
}
-(void)task:(NSObject<IJsonTask> *)task error:(NSString *)errorMessage isNetworkError:(BOOL)isNetworkError{
    [SVProgressHUD showErrorWithStatus:errorMessage];
}

-(void)onPaySuccess:(id)successResult{
    NSString* str = successResult;
    [self.delegate onPaySuccess:nil];
    
    
    if(!_notifyTask){
        _notifyTask = [[JsonTaskManager sharedInstance]createObjectJsonTask:@"s_pay_notify" cachePolicy:DMCachePolicy_NoCache];
    }
    
    
    NSData* data = [str dataUsingEncoding:NSUTF8StringEncoding];
    
    [_notifyTask put:@"verify" value:[CommonUtil encodeURL:[data base64EncodedStringWithOptions:NSDataBase64EncodingEndLineWithLineFeed]]];
    [_notifyTask setListener:self];
    [_notifyTask execute];

}



@end
