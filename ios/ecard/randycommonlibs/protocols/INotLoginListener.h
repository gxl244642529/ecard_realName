//
//  INotLoginListener.h
//  randycommonlibs
//
//  Created by randy ren on 14-7-21.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "JsonTask.h"

@class JsonTask;

@protocol INotLoginListener <NSObject>

-(BOOL)onNotLogin:(JsonTask*)task;

@end
