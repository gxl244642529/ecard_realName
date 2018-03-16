//
//  BusinessVo.m
//  ecard
//
//  Created by randy ren on 16/2/22.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "BusinessVo.h"

@implementation BusinessVo


-(void)fromJson:(NSDictionary *)json{
    _id = [json[@"id"]integerValue];
    _name = json[@"name"];
    _img = [DMServers formatUrl:0 url:json[@"img"]];
}


@end
