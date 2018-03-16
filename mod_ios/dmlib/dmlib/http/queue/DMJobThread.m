//
//  TaskThread.m
//  ecard
//
//  Created by randy ren on 15/3/19.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "DMJobThread.h"

@interface DMJobThread()
{
    __weak NSMutableArray* _queue;
    __weak NSCondition* _lock;
    __weak id<DMJobHandler> _hander;
    
  //  NSObject<JobHandler>* _handlers;
}

@end

@implementation DMJobThread


-(id)initWithQueue:(NSMutableArray*)queue lock:(NSCondition *)lock handler:(id<DMJobHandler>)hander  index:(NSInteger)index{
    if(self=[super initWithIndex:index]){
        _queue = queue;
        _lock = lock;
        _hander = hander;
    }
    return self;
}




-(void)repeationRun{
    [_lock lock];
    while (![self isCancelled] && _queue.count==0) {
        [_lock waitUntilDate:[NSDate dateWithTimeIntervalSinceNow:1]];
    }
    if([self isCancelled]){
         [_lock unlock];
        return;
    }
    DMJob* job = _queue[0];
    [_queue removeObjectAtIndex:0];
    [_lock unlock];
    //这里处理任务
    if([self isCancelled])return;
    if([job isCanceled]){
        return;
    }
    
    [_hander handleTask:job index:_index];
    
}
@end
