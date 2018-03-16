//
//  DMWXPay.m
//  DMLib
//
//  Created by randy ren on 16/1/28.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMWXPay.h"
@interface DMWXPay(){
    id _serverInfo;
}

@end
@implementation DMWXPay

@synthesize model=_model;

-(id)init{
    if(self =[super init]){
        self.payType = DMPAY_WEIXIN;
    }
    return self;
}

-(void)dealloc{
    _serverInfo = nil;
}

-(NSString*)title{
    return @"微信支付";
}

-(UIImage*)icon{
    return [UIImage imageNamed:@"ecardlibbundle.bundle/safe_weixin_pay"];
}


-(BOOL)handleOpenUrl:(NSURL *)url{
   return [WXApi handleOpenURL:url delegate:self];
    
}
/*! @brief 收到一个来自微信的请求，第三方应用程序处理完后调用sendResp向微信发送结果
 *
 * 收到一个来自微信的请求，异步处理完成后必须调用sendResp发送处理结果给微信。
 * 可能收到的请求有GetMessageFromWXReq、ShowMessageFromWXReq等。
 * @param req 具体请求内容，是自动释放的
 */
-(void) onReq:(BaseReq*)req{
    
}



/*! @brief 发送一个sendReq后，收到微信的回应
 *
 * 收到一个来自微信的处理结果。调用一次sendReq后会收到onResp。
 * 可能收到的处理结果有SendMessageToWXResp、SendAuthResp等。
 * @param resp具体的回应内容，是自动释放的
 */
-(void) onResp:(BaseResp*)resp{
    if([resp isKindOfClass:[PayResp class]]){
        //支付返回结果，实际支付结果需要去微信服务器端查询
        if(resp.errCode==WXSuccess){
            
            [_model notifyClientPaySuccess:self data:nil];
            
        }else if(resp.errCode == WXErrCodeUserCancel){
            [_model notifyPayCancel];
        }else{
           // [_model notifyPayError:resp.errStr];
        }
    }
}

-(void)jobSuccess:(DMApiJob*)request{
    _serverInfo = request.data;
    NSDictionary *resultMap= _serverInfo;
    
    
    //调起微信支付
    PayReq* req             = [[PayReq alloc] init];
    
    req.openID = resultMap[@"appid"];
    req.partnerId = resultMap[@"partnerid"]; //Constants.MCH_ID;
    req.prepayId = resultMap[@"prepayid"];
    req.package = resultMap[@"package"];
    req.nonceStr = resultMap[@"noncestr"]; //genNonceStr();
    req.timeStamp = (UInt32)[resultMap[@"timestamp"]integerValue];//String.valueOf(genTimeStamp());
    req.sign =  resultMap[@"sign"];
    

    [WXApi sendReq:req];
    
    
}




@end
