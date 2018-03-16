//
//  DMAliPay.m
//  DMLib
//
//  Created by randy ren on 16/1/28.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMAliPay.h"


@implementation DMAliPay
@synthesize model=_model;
-(id)init{
    if(self =[super init]){
        self.payType = DMPAY_ALIPAY;
    }
    return self;
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
            [_model notifyClientPaySuccess:self data:[DMBase64 encodeString: result]];
        }else{
           // [_model notifyPayCancel];
        }
        return YES;
    }
    
    return NO;
}

-(void)jobSuccess:(DMApiJob*)request{
    __weak DMAliPay* __self = self;
    [[AlipaySDK defaultService] payOrder:request.data fromScheme:APP_PACKAGE callback:^(NSDictionary *resultDic) {
        [__self onPayResult:resultDic];
    }];
}

-(NSString*)title{
    return @"支付宝";
}
-(UIImage*)icon{
    return [UIImage imageNamed:@"ecardlibbundle.bundle/safe_ali_pay"];
}


-(void)onPayResult:(id)result{
    int status = [[result valueForKey:@"resultStatus"]intValue];
    if(status == 9000){
        //成功
        [_model notifyClientPaySuccess:self data:[DMBase64 encodeString:result[@"result"]]];
    }else if(status== 6001){
        //取消
     //   [_model notifyPayCancel];
    }else if(status == 6002){
        //网络连接错误
        //[_model notifyPayError:@"网络连接错误"];
    }else if(status == 8000){
        //处理中
    }else if(status==4000){
        //失败
     //   [_model notifyPayError:@"付款失败,请稍候重试"];
    }
    
}

@end
