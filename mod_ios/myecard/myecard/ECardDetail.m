//
//  ECardDetail.m
//  ecard
//
//  Created by randy ren on 16/2/2.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "ECardDetail.h"

#import "ECardHis.h"


@implementation ECardDetail

-(void)dealloc{
    _hisList = nil;
}


-(void)fromJson:(NSDictionary*)json{
    _time = [NSString stringWithFormat:@"截止:%@", json[@"time" ] ];
    _left = [NSString stringWithFormat:@"%.2f",[json[@"left"]floatValue] ];
    
    _hisList = [json parseArray:@"history" class:[ECardHis class]];
    
}
@end
