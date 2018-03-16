//
//  ApiHandler.m
//  libs
//
//  Created by randy ren on 16/1/7.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMApiHandler.h"
#import "DMApiJob.h"

#import "ArrayContainer.h"
@interface DMApiHandler(){
    NSArray<DMServerHandler*>* _handlers;
    ArrayContainer* _container;
    __weak id _task;
}

@end

@implementation DMApiHandler
-(NSArray<DMServerHandler*>*)handlers{
    return _handlers;
}
-(void)dealloc{
    _container = nil;
    _handlers = nil;
}
-(void)clearSession{
    NSArray *cookies = [[NSHTTPCookieStorage sharedHTTPCookieStorage] cookies];
    for (int i = 0; i < [cookies count]; i++) {
        NSHTTPCookie *cookie = (NSHTTPCookie *)[cookies objectAtIndex:i];
        [[NSHTTPCookieStorage sharedHTTPCookieStorage] deleteCookie:cookie];
        
    }
    for (DMServerHandler* handler in _handlers) {
        [handler clearSession];
    }
}
-(void)setServerUrl:(NSString*)url index:(NSInteger)index{
    _handlers[index].baseUrl = url;
}
-(id)init{
    if(self = [super init]){
        _container = [[ArrayContainer alloc]init];
        _handlers = _container.array;
    }
    return self;
}
-(void)registerServerHandler:(NSInteger)server handler:(DMServerHandler*)hanlder{
    [_container registerObject:server object:hanlder];
}
-(void)handleTask:(DMApiJob*)task index:(NSInteger)index{
    [_handlers[task.server] handleTask:task index:index];
}

-(void)stop{
    for (DMServerHandler* handler in _handlers) {
        [handler stop];
    }
}
@end
