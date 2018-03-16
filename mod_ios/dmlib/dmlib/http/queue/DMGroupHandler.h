//
//  GroupHandler.h
//  libs
//
//  Created by randy ren on 16/1/7.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "DMJob.h"
@interface DMGroupHandler : NSObject<DMJobHandler>


-(void)registerHandler:(NSInteger)type handler:(id<DMJobHandler>)handler;

@end
