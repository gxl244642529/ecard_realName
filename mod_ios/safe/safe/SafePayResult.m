//
//  SafePayResult.m
//  ecard
//
//  Created by randy ren on 16/2/22.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "SafePayResult.h"
#import "SafeUtil.h"
@implementation SafePayResult


-(void)fromJson:(NSDictionary *)json{
    _fee = [NSString stringWithFormat:@"¥%.2f",(float)[json[@"fee"]intValue] / 100];
    _noticeUrl =[SafeUtil getUrl: json[@"noticeUrl"]];
    _protocolUrl =[SafeUtil getUrl: json[@"protocolUrl"]];
    _payTime = json[@"payTime"];
    _piccOrderId = json[@"piccOrderId"];
    _serviceTel = json[@"serviceTel"];
    
}

@end
