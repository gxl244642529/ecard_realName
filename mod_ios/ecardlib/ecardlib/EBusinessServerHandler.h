//
//  EBusinessServerHandler.h
//  ebusiness
//
//  Created by 任雪亮 on 16/10/25.
//  Copyright © 2016年 Facebook. All rights reserved.
//

#import <dmlib/dmlib.h>

@interface EBusinessServerHandler : DMServerHandler

+(EBusinessServerHandler*)runningInstance;
-(NSString*)token;
-(void)setToken:(NSDictionary*)token;

@end
