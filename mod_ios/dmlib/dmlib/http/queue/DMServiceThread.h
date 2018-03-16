//
//  ServiceThread.h
//  ecard
//
//  Created by randy ren on 15/3/19.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface DMServiceThread : NSObject
{
    NSInteger _index;
}

-(id)initWithIndex:(NSInteger)index;

-(void)start;
-(void)stop;

-(void)repeationRun;

-(BOOL)isCancelled;

@end
