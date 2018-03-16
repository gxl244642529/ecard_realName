//
//  ServerStatusMoniter.h
//  libs
//
//  Created by randy ren on 16/1/7.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "NetworkStatus.h"

@interface ServerStatusMoniter : NSObject


-(void)start;
-(void)stop;
-(NetworkStatus)status;
-(NSString*)url;
-(void)setUrl:(NSString*)url;

@end
