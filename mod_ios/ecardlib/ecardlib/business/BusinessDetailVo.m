//
//  BusinessDetailVo.m
//  ecard
//
//  Created by randy ren on 16/2/23.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "BusinessDetailVo.h"

@implementation BusinessDetailVo


/*
 @property (nonatomic,copy) NSString* phone;
 @property (nonatomic,copy) NSString* addr;
 @property (nonatomic,copy) NSString* detail;
 @property (nonatomic,copy) NSString* img1;
 @property (nonatomic,copy) NSString* img2;
 @property (nonatomic,copy) NSString* img3;
 */


-(void)fromJson:(NSDictionary *)json{
    _title = json[@"title"];
    _addr = json[@"addr"];
    _detail = json[@"detail"];
    _img1 = json[@"img1"];
    _img2 = json[@"img2"];
    _img3 = json[@"img3"];
    
    _lat = [json[@"lat"]doubleValue];
    _lng = [json[@"lng"]doubleValue];
    
    _count = [NSString stringWithFormat:@"共%@家分店",json[@"count"]];
}

@end
