//
//  TaskQueue.h
//  ecard
//
//  Created by randy ren on 15/3/19.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "DMJob.h"


@interface DMJobQueue : NSObject

-(void)addTask:(DMJob*)task;

-(void)setTaskHandlers:(NSArray<id<DMJobHandler>>*)handlers;


-(void)start;
-(void)stop;

@end
