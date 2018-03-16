//
//  QRCode SDK API.h
//  sm2
//
//  Created by zhaojunfeng on 17/3/13.
//  Copyright © 2017年 yfc. All rights reserved.
//

#import <Foundation/Foundation.h>
@interface QRCode_SDK_API : NSObject


//设置地址 host: https://110.80.22.108  port:8089
-(void)setHost:(NSString *)host setPort:(NSString *)port;

-(int)activeDevice:(NSString *)token ;

-(NSDictionary *)getQRCode:(NSString *)accountId ;

-(void)usedToken:(NSString *)tokenId;

-(void)disableToken;

-(void)inactiveDevide;

@end
