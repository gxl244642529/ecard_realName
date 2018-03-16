//
//  TaskThread.h
//  ecard
//
//  Created by randy ren on 15/3/19.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "DMServiceThread.h"
#import "DMJob.h"

@interface DMJobThread : DMServiceThread
-(id)initWithQueue:(NSMutableArray*)queue lock:(NSCondition*)lock handler:(id<DMJobHandler>)hander index:(NSInteger)index;
@end
