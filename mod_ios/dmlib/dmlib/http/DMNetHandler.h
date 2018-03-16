//
//  NetHandler.h
//  libs
//
//  Created by randy ren on 16/1/7.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "DMJob.h"
#import "DMHttpJob.h"
#import "DMNetwork.h"
#import "DataParser.h"
#import "DMCache.h"

@interface DMNetHandler : NSObject<DMJobHandler,DMJobCtrl>
{
    DMNetwork* _network;
    __weak id _currentTask;
    __weak NSArray<id<DataParser>>* _parsers;
    __weak id<DMCache> _cache;
    __weak NSArray<id<DMJobSuccess>>* _successListeners;
    
    __weak id<DMJobError> _errorDelegate;

}

-(id)initWithNetwork:(DMNetwork*)network parsers:(NSArray<id<DataParser>>*)parsers cache:(id<DMCache>)cache successListeners:(NSArray<id<DMJobSuccess>>*)successListeners errorDelegate:(id<DMJobError>)errorDelegate;

//创建请求
-(NSMutableURLRequest*)createRequest:(DMHttpJob*)task;
//处理错误
-(void)postError:(DMHttpJob*)task;
@end
