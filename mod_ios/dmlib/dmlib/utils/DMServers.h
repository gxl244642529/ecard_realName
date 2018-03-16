//
//  Servers.h
//  libs
//
//  Created by randy ren on 16/1/9.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface DMServers : NSObject

+(NSString*)formatUrl:(NSInteger)server url:(NSString*)url;
+(NSInteger)startPosition:(NSInteger)server;
+(NSString*)getUrl:(NSInteger)index;
@end
