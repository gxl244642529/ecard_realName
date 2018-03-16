//
//  GroupHandler.m
//  libs
//
//  Created by randy ren on 16/1/7.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMGroupHandler.h"
#import "DMJob.h"
#import "ArrayContainer.h"

@interface DMGroupHandler()
{
    NSMutableArray<id<DMJobHandler>>* _arr;
    
    ArrayContainer* _arrContainer;
}

@end

@implementation DMGroupHandler


-(void)dealloc{
    _arrContainer = nil;
    _arr = nil;
}

-(id)init{
    if(self = [super init]){
        _arrContainer = [[ArrayContainer alloc]init];
        _arr = _arrContainer.array;
    }
    return self;
}


-(void)registerHandler:(NSInteger)type handler:(id<DMJobHandler>)handler{
    [_arrContainer registerObject:type object:handler];
}



-(void)handleTask:(DMJob*)task index:(NSInteger)index{
    [_arr[task.type] handleTask:task index:index];
}


-(void)stop{
    for(int i=0 ; i < _arr.count; ++i){
        
    }
}

@end
