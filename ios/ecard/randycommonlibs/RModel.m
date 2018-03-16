//
//  RModel.m
//  ecard
//
//  Created by randy ren on 15/5/14.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "RModel.h"
#import "JsonTaskManager.h"



@interface RModel()
{
    __weak id _observer;
    NSMutableDictionary* _observers;
    NSMutableDictionary* _tasks;
}

@end

@implementation RModel

-(void)dealloc{
    _observers = nil;
    _tasks = nil;
}
-(void)setObserver:(id)observer{
    _observer = observer;
}
-(id)initWithObserver:(id)observer{
    if(self = [super init]){
        _observer = observer;
        _observers = [[NSMutableDictionary alloc]init];
        _tasks = [[NSMutableDictionary alloc]init];
    }
    return self;
}

-(id)init{
    if(self = [super init]){
        _observers = [[NSMutableDictionary alloc]init];
        _tasks = [[NSMutableDictionary alloc]init];
    }
    return self;
}



-(ObjectJsonTask*)createTask:(NSString*)api cachePolicy:(CachePolicy)cachePolicy waitingMessage:(NSString*)waitingMessage{
    return [self createTask:api cachePolicy:cachePolicy waitingMessage:waitingMessage factory:0];
}
-(ObjectJsonTask*)createTask:(NSString*)api cachePolicy:(CachePolicy)cachePolicy{
    return [self createTask:api cachePolicy:cachePolicy factory:0];
}

-(ObjectJsonTask*)createTask:(NSString*)api cachePolicy:(CachePolicy)cachePolicy waitingMessage:(NSString*)waitingMessage factory:(NSInteger)factory{
    ObjectJsonTask* task = [_tasks objectForKey:api];
    if(!task){
        task =[[JsonTaskManager sharedInstance]createObjectJsonTask:api cachePolicy:cachePolicy factory:factory];
        [task setListener:self];
        [task setWaitMessage:waitingMessage];
        [_tasks setValue:task forKey:api];
    }
    return task;
    
}
-(ObjectJsonTask*)createTask:(NSString*)api cachePolicy:(CachePolicy)cachePolicy factory:(NSInteger)factory{
    ObjectJsonTask* task = [_tasks objectForKey:api];
    if(!task){
        task =[[JsonTaskManager sharedInstance]createObjectJsonTask:api cachePolicy:cachePolicy factory:factory];
        [task setListener:self];
        [_tasks setValue:task forKey:api];
        
    }
    return task;
}




-(void)setObserver:(NSString*)key selector:(SEL)selector{
    unsigned long sel = (unsigned long)selector;
    [_observers setValue:[NSNumber numberWithUnsignedLong:sel] forKey:key];
}

-(void)task:(ObjectJsonTask*)task result:(id)result{
    [self notifyObserver:task.api data:result];
}
+(NSString*)makeError:(NSString*)api{
    return [NSString stringWithFormat:@"%@_error",api];
}
-(void)task:(ObjectJsonTask*)task error:(NSString *)errorMessage isNetworkError:(BOOL)isNetworkError{
    NSString* key = [RModel makeError:task.api];
    id ob =  [_observers objectForKey:key];
    if(ob){
        
        if(_observer){
            SEL selector = (SEL)[ob unsignedLongValue];
            PerformSelector(_observer,selector,errorMessage);
        }
        
    }else{
        if([errorMessage isKindOfClass:[NSNull class]]){
            [SVProgressHUD showErrorWithStatus:@"未知错误"];
        }else{
            [SVProgressHUD showErrorWithStatus:errorMessage];
        }
        
    }
}

-(void)notifyObserver:(NSString*)key data:(id)data{
    id ob =  [_observers objectForKey:key];
    if(ob && _observer){
        SEL selector = (SEL)[ob unsignedLongValue];
        PerformSelector(_observer, selector, data);
    }
}
-(ObjectJsonTask*)getTask:(NSString*)api{
    return [_tasks objectForKey:api];
}
@end
