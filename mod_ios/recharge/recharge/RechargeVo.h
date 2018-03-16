//
//  RechargeVo.h
//  ecard
//
//  Created by 任雪亮 on 16/3/2.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>

#import <DMLib/DMLib.h>

typedef enum {
    RechargeStatus_NoPay,
    RechargeStatus_Success,
    RechargeStatus_Refund,
    RechargeStatus_Finish,
    RechargeStatus_Invoce
}RechargeStatus;

@interface RechargeVo : NSObject<IJsonValueObject>


PROPERTY_STRING(orderId);
PROPERTY_STRING(tyId);
PROPERTY_STRING(payTime);
PROPERTY_STRING(fee);
PROPERTY_STRING(cardId);
PROPERTY_INTEGER(status);

@property (nonatomic,readonly,copy) NSString* statusStr;
DECLARE_IMG_PROPERTY(statusImg);
DECLARE_HIDDEN_PROPERTY(statusImg);
DECLARE_HIDDEN_PROPERTY(btnGo);
DECLARE_HIDDEN_PROPERTY(btnRefund);
DECLARE_HIDDEN_PROPERTY(btnInvoce);
@end
