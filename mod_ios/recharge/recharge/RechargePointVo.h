//
//  RechargePointVo.h
//  recharge
//
//  Created by 任雪亮 on 17/1/17.
//  Copyright © 2017年 任雪亮. All rights reserved.
//

#import <DMLib/DMLib.h>

@interface RechargePointVo : NSObject<IJsonValueObject>
PROPERTY_STRING(title);
PROPERTY_STRING(address);
PROPERTY_DOUBLE(lat);
PROPERTY_DOUBLE(lng);
@end
