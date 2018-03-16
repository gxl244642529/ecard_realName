//
//  RechargeVo.m
//  ecard
//
//  Created by 任雪亮 on 16/3/2.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "RechargeVo.h"

@implementation RechargeVo

-(void)fromJson:(NSDictionary *)json{

    FROM_JSON(orderId);
    FROM_JSON(tyId);
    FROM_JSON(payTime);
   // FROM_JSON(fee);
    FROM_JSON(cardId);
    FROM_JSON_INTEGER(status);
    
    _fee = [FeeUtil formatFee:[json[@"fee"]integerValue]];
}

IMPLEMENT_IMG_PROPERTY(statusImg){
    
    switch (_status) {
        case RechargeStatus_NoPay:
        case RechargeStatus_Success:
            return [UIImage imageNamed:@"rechargebundle.bundle/recharge_status_1.png"];
        case RechargeStatus_Refund:
            return [UIImage imageNamed:@"rechargebundle.bundle/recharge_status_2.png"];
        case RechargeStatus_Invoce:
            return [UIImage imageNamed:@"rechargebundle.bundle/recharge_status_2.png"];
        default:
            if([self isRefundFail]){
                return [UIImage imageNamed:@"rechargebundle.bundle/recharge_status_3.png"];
            }
           return [UIImage imageNamed:@"rechargebundle.bundle/recharge_status_2.png"];
    }
}

-(BOOL)isRefundFail{
    
   return _status > 10;
}

-(NSString*)statusStr{
    switch (_status) {
        case RechargeStatus_NoPay:
        case RechargeStatus_Success:
            return @"未完成";
        case RechargeStatus_Refund:
            return @"已退款";
        case RechargeStatus_Invoce:
            return @"已领发票";
        default:
            if([self isRefundFail]){
                return @"未完成";
            }
            return @"已完成";
    }
}

IMPLEMENT_HIDDEN_PROPERTY(statusImg){
    return _status == RechargeStatus_Invoce;
}

IMPLEMENT_HIDDEN_PROPERTY(btnInvoce){
    return _status != RechargeStatus_Finish;
}

IMPLEMENT_HIDDEN_PROPERTY(btnGo){
    return _status != RechargeStatus_Success;
}

-(BOOL)isUnfinished{
    return [self isRefundFail] || _status == RechargeStatus_Success ;
}

IMPLEMENT_HIDDEN_PROPERTY(btnRefund){
    return ![self isUnfinished];
}

@end
