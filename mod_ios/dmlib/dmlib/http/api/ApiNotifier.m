//
//  ApiNotifier.m
//  libs
//
//  Created by randy ren on 16/1/9.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "ApiNotifier.h"
#import "DMApiJob.h"
#import "Alert.h"
#import "DMApiUtil.h"
#import "DMNotifier.h"

@interface ApiNotifier()
{
    __weak DMJobManager* _taskManager;
}

@end


@implementation ApiNotifier
-(id)initWithTaskManager:(DMJobManager*)taskManager{
    if(self = [super init]){
        _taskManager = taskManager;
    }
    return self;
}

-(void)dealloc{
    _taskManager = nil;
}

-(void)jobSuccess:(DMApiJob*)request{
   
    [self performSelectorOnMainThread:@selector(onSuccess:) withObject:request waitUntilDone:NO];
}

-(BOOL)jobError:(id)request{
    [self performSelectorOnMainThread:@selector(onError:) withObject:request waitUntilDone:NO];
    return YES;
}


-(BOOL)jobMessage:(id)request{
    [self performSelectorOnMainThread:@selector(onMessage:) withObject:request waitUntilDone:NO];
    return YES;
}



-(void)onMessage:(DMApiJob*)request{
    if([request isCanceled]){
        return;
    }
    [Alert cancelWait];
    if(request.button){
         [DMApiUtil enableButton:request.button];
    }
    //首先进行通知
    @try {
        //查看是否有处理
        BOOL handled = NO;
        if([request.delegate respondsToSelector:@selector(jobMessage:)]){
            handled = [request.delegate jobMessage:request];
        }
        if(![[DMNotifier notifier]notifyObservers:request.messageNotification data:request.serverMessage] && !handled){
            //这里没有处理
            if(request.serverMessageType == MessageType_Toast){
                [Alert toast:request.serverMessage];
            }else{
                [Alert alert:request.serverMessage];
            }
        }
    }
    @catch (NSException *exception) {
        NSLog(@"%@",exception);
    }
    @finally {
        [_taskManager releaseJob:request];
    }
}
-(void)onError:(DMApiJob*)request{
    if([request isCanceled]){
        return;
    }
     [Alert cancelWait];
    if(request.button){
         [DMApiUtil enableButton:request.button];
    }
    //首先进行通知
    @try {
         BOOL handled = NO;
        if([request.delegate respondsToSelector:@selector(jobError:)]){
            handled = [request.delegate jobError:request];
        }
        if(! [[DMNotifier notifier]notifyObservers:request.errorNotification data:request.error] && !handled){
            [Alert toast:request.error.reason];
        }
    }
    @catch (NSException *exception) {
        NSLog(@"%@",exception);
    }
    @finally {
        [_taskManager releaseJob:request];
    }
}



-(void)onSuccess:(DMApiJob*)request{
    if([request isCanceled]){
        return;
    }
    [Alert cancelWait];
    if(request.button){
        [DMApiUtil enableButton:request.button];
    }
    
    if(request.successCallback){
        request.successCallback(request.data);
    }
    //首先进行通知
    @try {
        if([request.delegate respondsToSelector:@selector(jobSuccess:)]){
            [request.delegate jobSuccess:request];
        }
        [[DMNotifier notifier]notifyObservers:request.successNotification data:request.data];
    }
    @catch (NSException *exception) {
        NSLog(@"%@",exception);
    }
    @finally {
        [_taskManager releaseJob:request];
    }

    
   
}

@end
