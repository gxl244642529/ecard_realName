//
//  TaskQueue.m
//  ecard
//
//  Created by randy ren on 15/3/19.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "DMJobQueue.h"
#import "DMJobThread.h"


@interface DMJobQueue ()
{
    NSMutableArray* _tasks;
    NSCondition* _condition;
    NSMutableArray* _threads;
    NSArray<id<DMJobHandler>>* _taskHandler;
    NSInteger _threadCount;
}

@end

@implementation DMJobQueue

-(void)dealloc{
    _taskHandler = nil;
    _tasks = nil;
    _condition = nil;
    _threads = nil;
}

-(id)init{
    if(self = [super init]){
        _condition = [[NSCondition alloc]init];
        _tasks = [[NSMutableArray alloc]init];
        _threads = [[NSMutableArray alloc]init];
    }
    return self;
}

-(void)addTask:(DMJob*)task{
    [_condition lock];
    [_tasks addObject:task];
    [_condition signal];
    [_condition unlock];
}

-(void)setTaskHandlers:(NSArray<id<DMJobHandler>>*)handler{
    _taskHandler = handler;
    _threadCount = handler.count;
}
-(void)stop{
    
    for(id<DMJobHandler> handler in _taskHandler){
        [handler stop];
    }
    
    for(DMJobThread* thread in _threads){
        [thread stop];
    }
}

-(void)start{
    for(NSInteger i=0; i<_threadCount; ++i){
        DMJobThread* thread = [[DMJobThread alloc]initWithQueue:_tasks lock:_condition handler:_taskHandler[i] index:i];
        [thread start];
        [_threads addObject:thread];
    }
}

@end
