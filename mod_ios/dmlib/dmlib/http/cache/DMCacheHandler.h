//
//  CacheHandler.h
//  libs
//
//  Created by randy ren on 16/1/7.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "DataParser.h"
#import "DMJobQueue.h"
#import "DMCache.h"
#import "ServerStatusMoniter.h"

@interface DMCacheHandler : NSObject<DMJobHandler>


-(id)initWithNetQueue:(DMJobQueue*)networkQueue apiQueue:(DMJobQueue*)apiQueue cache:(NSObject<DMCache>*)cache parsers:(NSArray<id<DataParser>>*)parsers
              moniter:(ServerStatusMoniter*)moniter successListeners:(NSArray<id<DMJobSuccess>>*)successListeners;

@end
