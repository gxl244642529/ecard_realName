//
//  PushUtil.m
//  dmlib
//
//  Created by 任雪亮 on 16/12/20.
//  Copyright © 2016年 任雪亮. All rights reserved.
//

#import "PushUtil.h"


__weak id<IPush> _push;

@implementation PushUtil


+(void)setPush:(id<IPush>)push{
    _push = push;
}
+(NSString*)getPushId{
    return [_push getPushId];
}
+(void)onRecvRegId:(NSString*)regId{
    [_push onRecvRegId:regId];
}
+(void)onLogout{
    [_push onLogout];
}
+(void)onLoginSuccess:(id)account{
    [_push onLoginSuccess:account];
}
@end
