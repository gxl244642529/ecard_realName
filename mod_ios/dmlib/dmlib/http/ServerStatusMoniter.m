//
//  ServerStatusMoniter.m
//  libs
//
//  Created by randy ren on 16/1/7.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "ServerStatusMoniter.h"
#import "Reachability.h"
@interface ServerStatusMoniter()
{
    Reachability* _reachability;
    NSString* _url;
}

@end

@implementation ServerStatusMoniter

-(NSString*)url{
    return _url;
}
-(void)dealloc{
    _reachability = nil;
    _url = nil;
}
-(void)setUrl:(NSString*)url{
     _url =[_url substringFromIndex:7];
}
-(void)start{
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(networkStateChange) name:kReachabilityChangedNotification object:nil];
    _reachability = [Reachability reachabilityWithHostName:_url];
    [_reachability startNotifier];
}
-(NetworkStatus)status{
    return _reachability.currentReachabilityStatus;
}
-(void)networkStateChange{
    
}

-(void)stop{
    [_reachability stopNotifier];
    _reachability = nil;
}
@end
