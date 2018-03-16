//
//  DMAbsHandler.m
//  DMLib
//
//  Created by 任雪亮 on 16/3/3.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMAbsHandler.h"
#import "DMExceptionHandler.h"

@implementation DMAbsHandler

-(void)handleTask:(DMJob*)task index:(NSInteger)index{
    
    _currentJob = task;
    [task setCtrl:self];
    
    @try {
        [self handleTaskImpl:task];
    }
    @catch (NSException *exception) {
        [DMExceptionHandler handleException:exception];
    }
    @finally {
        _currentJob = nil;
        [task setCtrl:nil];
    }
}

-(void)stop{
    
}

-(void)handleTaskImpl:(id)task{
    
}



-(void)cancelJob:(id)job{
    if(_currentJob == job){
        [self cancelProgress];
    }
}

-(void)cancelProgress{
    
}

@end
