//
//  MyECardVo.m
//  myecard
//
//  Created by 任雪亮 on 17/3/29.
//  Copyright © 2017年 任雪亮. All rights reserved.
//

#import "MyECardVo.h"

@implementation MyECardVo
-(void)fromJson:(NSDictionary*)json{
    [super fromJson:json];
    
    self.createDate = [json getString:@"createDate"];
    self.cardFlag = [json getInteger:@"cardFlag"];
}
@end
