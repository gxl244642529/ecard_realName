//
//  RealInfo.m
//  ecard
//
//  Created by randy ren on 16/2/21.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "RealInfo.h"

@implementation RealInfo


-(void)fromJson:(NSDictionary *)json{
    _idCard = json[@"idCard"];
    _isValid = [json[@"isValid"]boolValue];
    _phone = json[@"phone"];
    _name = json[@"name"];
}

@end
