//
//  SafeShowDetailVo.m
//  ecard
//
//  Created by 任雪亮 on 16/3/8.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "SafeShowDetailVo.h"

#import "SafeUtil.h"


@implementation SafeShowDetailVo

-(void)fromJson:(NSDictionary *)json{
    _backgroundUrl = [SafeUtil getUrl:json[@"detail_url"]];
}

@end
