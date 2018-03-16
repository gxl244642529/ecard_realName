//
//  ApiHandler.h
//  libs
//
//  Created by randy ren on 16/1/7.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "DMJob.h"
#import "DMServerHandler.h"

@interface DMApiHandler : NSObject<DMJobHandler>


-(void)registerServerHandler:(NSInteger)server handler:(DMServerHandler*)hanlder;
-(void)setServerUrl:(NSString*)url index:(NSInteger)index;
-(NSArray<DMServerHandler*>*)handlers;
-(void)clearSession;
@end
