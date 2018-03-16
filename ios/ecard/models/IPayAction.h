//
//  IPayAction.h
//  ecard
//
//  Created by randy ren on 15/8/27.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#ifndef ecard_IPayAction_h
#define ecard_IPayAction_h


typedef enum{
    
    PAY_ALIPAY = 1,     //支付宝
    PAY_WEIXIN,         //微信
    PAY_ETONGKA,        //e通卡
    PAY_UNION           //银行卡
    
}PayType;

@protocol IPayActionListener <NSObject>

/**
 *  预支付成功
 *
 *  @param serverInfo <#serverInfo description#>
 */
-(void)onPrePaySuccess:(NSString*)serverInfo;
/**
 *  通知服务器成功
 *
 *  @param result <#result description#>
 */
-(void)onNotifyServerSuccess:(id)result;

@optional
-(void)onNotifyServerError:(NSString*)error isNetworkError:(BOOL)isNetworkError;
@end


@protocol IPayAction <NSObject>

-(void)prePay:(PayType)type orderID:(NSString*)orderID listener:(id<IPayActionListener>)listener;

-(void)notifyServer:(PayType)type info:(NSString*)info listener:(id<IPayActionListener>)listener;


@end

#endif
