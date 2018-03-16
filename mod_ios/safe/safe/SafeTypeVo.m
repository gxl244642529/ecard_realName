//
//  SafeTypeVo.m
//  ecard
//
//  Created by randy ren on 16/1/24.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "SafeTypeVo.h"
#import "SafeUtil.h"
@implementation SafeTypeVo

-(void)fromJson:(NSDictionary*)json{
    _title = json[@"title"];
    _typeId = json[@"type_id"];
    _type = [_typeId integerValue];
}
@end
