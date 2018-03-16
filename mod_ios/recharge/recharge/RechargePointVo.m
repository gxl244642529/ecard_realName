//
//  RechargePointVo.m
//  recharge
//
//  Created by 任雪亮 on 17/1/17.
//  Copyright © 2017年 任雪亮. All rights reserved.
//

#import "RechargePointVo.h"

@implementation RechargePointVo
-(void)fromJson:(NSDictionary *)json{
    
    FROM_JSON(title);
    FROM_JSON(address);
   
    FROM_JSON_DOUBLE(lat);
    FROM_JSON_DOUBLE(lng);
    
}
@end
