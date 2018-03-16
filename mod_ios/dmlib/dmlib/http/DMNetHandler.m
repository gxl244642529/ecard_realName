//
//  NetHandler.m
//  libs
//
//  Created by randy ren on 16/1/7.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMNetHandler.h"
#import "DMNetwork.h"
#import "DMHttpJob.h"
#import "DMJobTypes.h"


@interface DMNetHandler()
{
}

@end

@implementation DMNetHandler

-(void)onCancel:(id)task{
}

-(id)initWithNetwork:(DMNetwork *)network parsers:(NSArray<id<DataParser>>*)parsers  cache:(id<DMCache>)cache successListeners:(NSArray<id<DMJobSuccess>>*)successListeners errorDelegate:(id<DMJobError>)errorDelegate{
    if(self = [super init]){
        _network = network;
        _parsers = parsers;
        _cache = cache;
        _successListeners  =successListeners;
        _errorDelegate = errorDelegate;
    }
    return self;
}

-(void)dealloc{
    _network = nil;
}


-(NSMutableURLRequest*)createRequest:(DMHttpJob*)task{
    NSMutableURLRequest* request = [[NSMutableURLRequest alloc]initWithURL:[NSURL URLWithString:task.url]];
    if(task.method == POST){
         [request setHTTPMethod: @"POST"];
        if(task.httpHeader){
            for (NSString* key in task.httpHeader) {
                [request setValue:task.httpHeader[key] forHTTPHeaderField:key];
            }
        }
    }else{
        [request setHTTPMethod: @"GET"];

    }
   
    
    return request;
}

-(NSData*)getData:(DMHttpJob*)task error:(NSError**)error{
    NSMutableURLRequest* request = [self createRequest:task];
#ifdef DEBUG
    NSLog(@"Start load :%@",task.url);
#endif
    return [_network execute:request delegate:task.delegate error:error];
}

-(void)handleTask:(DMHttpJob*)task index:(NSInteger)index{
    _currentTask = task;
    task.ctrl = self;
    NSError* error = nil;
    NSData* data = [self getData:task error:&error];
    if([task isCanceled]){
        _currentTask = nil;
        return;
    }
    if(error){
        //错误,进入错误处理
        task.error = [[DMException alloc]initWithReason:@"网络错误" code:DMErrorType_Network];
        //正确了
        [self postError:task];
    }else{
        //正确,进入正确处理
        id result = [_parsers[task.dataType]parseData:task data:data error:&error];
        if(error){
            task.error = [[DMException alloc]initWithReason:@"数据解析失败" code:DMErrorType_InvalidData];
            [self postError:task];
        }else{
            //写入缓存
            task.data = result;
            [_successListeners[task.type] jobSuccess:task];
            
            if(task.cachePolicy!=DMCachePolicy_NoCache){
                [_cache put:task.cacheKey value:data];
            }
        }
    }
    task.ctrl = nil;
    _currentTask = nil;
}

-(void)postError:(DMHttpJob*)task{
#ifdef DEBUG
    NSLog(@"发生错误%@",task.error);
#endif
    [_errorDelegate jobError:task];
}

-(void)stop{
    [_network cancel];
}

-(void)cancelJob:(id)job{
    if(job == _currentTask){
        [_network cancel];
    }
}



@end
