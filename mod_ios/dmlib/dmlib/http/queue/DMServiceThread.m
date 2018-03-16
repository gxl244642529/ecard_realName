//
//  ServiceThread.m
//  ecard
//
//  Created by randy ren on 15/3/19.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "DMServiceThread.h"

@interface DMServiceThread()
{
    volatile BOOL isRunning;
    NSThread* _thread;
}

@end

@implementation DMServiceThread


-(void)dealloc{
    _thread = nil;
}

-(id)initWithIndex:(NSInteger)index{
    if(self = [super init]){
        _index = index;
    }
    return self;
}

-(void)start{
     _thread = [[NSThread alloc]initWithTarget:self selector:@selector(run:) object:self];
    [_thread start];
}
-(void)stop{
    [_thread cancel];
}

-(BOOL)isCancelled{
    return [_thread isCancelled];
}
-(void)repeationRun{
    
    
    
}
-(void)run:(DMServiceThread*)sender{
    while (![_thread isCancelled]) {
        @try {
            [sender repeationRun];
        }
        @catch (NSException *exception) {
            NSLog(@"%@",exception);
        }
    }

    
    
}
@end
