//
//  PushUtil.h
//  dmlib
//
//  Created by 任雪亮 on 16/12/20.
//  Copyright © 2016年 任雪亮. All rights reserved.
//

#import <Foundation/Foundation.h>


@protocol IPush <NSObject>

-(NSString*)getPushId;
-(void)onRecvRegId:(NSString*)regId;
-(void)onLoginSuccess:(id)account;
-(void)onLogout;
@end

@interface PushUtil : NSObject

+(void)setPush:(id<IPush>)push;


+(void)onLoginSuccess:(id)account;
//推送id
+(NSString*)getPushId;

+(void)onLogout;

+(void)onRecvRegId:(NSString*)regId;

@end
