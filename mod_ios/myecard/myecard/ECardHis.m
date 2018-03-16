//
//  ECardHis.m
//  ecard
//
//  Created by randy ren on 16/2/2.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "ECardHis.h"

#import <ecardlib/ecardlib.h>

@implementation ECardHis

-(void)fromJson:(NSDictionary*)json{
    
    _value = [NSString stringWithFormat:@"%.2f",[json[@"value"]floatValue]];
    _shpName = json[@"shpName"];
    int type = [json[@"type"]integerValue];
    switch (type) {
        case 0:
             _type = @"消费";
            break;
        case 1:
            _type = @"充值";
            break;
            
        default:
            _type = @"其他";
            break;
    }
   
    _time = [ECardUtil parseTime:json[@"time"]];
    
}

-(UIColor*)bg_type{
    UIColor* color;
    if([_type isEqualToString:@"充值"]){
        color =[DMColorConfig item:@"recharge"];
    }else{
        color =[DMColorConfig item:@"consume"];
    }
    
    return color;
    
}

@end
