
//
//  DMPay.m
//  DMLib
//
//  Created by randy ren on 16/1/28.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMPay.h"

@implementation DMPay
-(BOOL)handleOpenUrl:(NSURL*)url{
    return NO;
}
/**
 *  预支付成功
 *
 *  @param serverInfo <#serverInfo description#>
 */
-(void)onPrePaySuccess:(id)serverInfo{
    
}

-(UIImage*)icon{
    return nil;
}
-(NSString*)title{
    return nil;
}
/**
 *  通知服务器成功
 *
 *  @param result <#result description#>
 */
-(void)onNotifyServerSuccess:(id)result{
    
}

-(void)onNotifyServerError:(NSString*)error isNetworkError:(BOOL)isNetworkError{
    
}

-(BOOL)jobError:(DMApiJob*)request{
    
    
    
     return [_model notifyPrePayError:request.error.reason isNetworkError: [request.error isNetworkError]  ];
    
}


@end
