//
//  AliPay.m
//  ecard
//
//  Created by randy ren on 15/8/27.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "AliPay.h"
#import <ecardlib/ecardlib.h>

@interface AliPay()
{
    __weak id<IPayAction> _action;
}

@end


@implementation AliPay

-(id)initWithModel:(ECardRPayModel *)model controller:(UIViewController *)parent action:(id<IPayAction>)action{
    if(self = [super initWithModel:model controller:parent]){
        _payType = PAY_ALIPAY;
        _action = action;
    }
    return self;
}

/**
 *  预支付成功
 *
 *  @param serverInfo <#serverInfo description#>
 */
-(void)onPrePaySuccess:(NSString*)serverInfo{
    __weak AliPay* __self = self;
   /* [[AlipaySDK defaultService] payOrder:serverInfo fromScheme:PACKAGE callback:^(NSDictionary *resultDic) {
        [__self onPayResult:resultDic];
    }];*/
}

-(void)onPayResult:(id)result{
    int status = [[result valueForKey:@"resultStatus"]intValue];
    if(status == 9000){
        //成功
       [_action notifyServer:_payType info:result[@"result"] listener:self];
    }else if(status== 6001){
        //取消
        [_model onPayCancel];
    }else if(status == 6002){
        //网络连接错误
        [_model onPayError:6002 error:@"网络连接错误"];
    }else if(status == 8000){
        //处理中
    }else if(status==4000){
        //失败
        [_model onPayError:4000 error:@"付款失败,请稍候重试"];
    }
    
}


-(BOOL)handleOpenUrl:(NSURL *)url{
    
    
    if ([url.host isEqualToString:@"safepay"]) {
        NSString* strUrl = [url absoluteString];
        strUrl = [strUrl stringByReplacingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
        strUrl = [strUrl substringFromIndex:[strUrl rangeOfString:@"?"].location+1];
        NSError* error;
        NSDictionary *resultMap = [NSJSONSerialization JSONObjectWithData:[strUrl dataUsingEncoding:NSUTF8StringEncoding] options:NSJSONReadingMutableLeaves error:&error];
        
        assert(error==NULL);
        NSString* result = [resultMap[@"memo"] objectForKey:@"result"];
        if(result && result.length > 0 ){
            [_action notifyServer:_payType info:[DMBase64 encodeString: result] listener:self];
        }else{
            [_model onPayCancel];
        }
        return YES;
    }
    
    return NO;
}

/**
 *  通知服务器成功
 *
 *  @param result <#result description#>
 */
-(void)onNotifyServerSuccess:(id)result{
    [_model onPaySuccess:_payType result:result];
}
@end
