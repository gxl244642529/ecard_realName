//
//  ServerManager.m
//  libs
//
//  Created by randy ren on 16/1/7.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "ServerManager.h"
#import "ServerStatusMoniter.h"
@interface ServerManager()
{
    NSMutableArray<ServerStatusMoniter*>* _servers;
}

@end

@implementation ServerManager

-(id)init{
    if(self = [super init]){
        _servers  =[[NSMutableArray alloc]init];
    }
    return self;
}

-(void)stop{
    for (ServerStatusMoniter* moniter in _servers) {
        [moniter stop];
    }
}

-(void)start{
    for (ServerStatusMoniter* moniter in _servers) {
        [moniter start];
    }
}

-(NSString*)getUrl:(NSInteger)index{
    return _servers[index].url;
}

-(NetworkStatus)getStatus:(NSInteger)index{
    return NotReachable;
}

-(void)registerServer:(NSString*)url{
    [_servers addObject:[[ServerStatusMoniter alloc]init]];
}

@end
