//
//  SafeDetailVo.m
//  ecard
//
//  Created by randy ren on 16/1/24.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "SafeDetailVo.h"
#import "SafeUtil.h"

@implementation SafeDetailVo
-(void)fromJson:(NSDictionary*)json{
    _headImage =[SafeUtil getUrl: json[@"detail_url"] ];
    _title = json[@"title"];
    _company = json[@"company"];
    _price = json[@"product_price"];
    _orgPrice = json[@"ori_price"];
    _summary = json[@"summary"];
    _price = [NSString stringWithFormat:@"¥%@",json[@"price"]];
    _orgPrice =[NSString stringWithFormat:@"¥%@",json[@"ori_price"]];
    _startTime = [[json getString:@"start_time"]substringWithRange:NSMakeRange(0,10)];
    _endTime = [[json getString:@"end_time"]substringWithRange:NSMakeRange(0,10)];
    _company = [NSString stringWithFormat:@"以上产品由%@提供",json[@"company"]];
    _serviceTel = json[@"service_tel"];
    
    _guardUrl = [SafeUtil getUrl:json[@"safeguard_url"]];
    _limit = [json[@"pid_sales_limit"]integerValue];
    if(_startTime && _endTime){
         _duration =[NSString stringWithFormat:@"%@至%@",_startTime,_endTime];
    }
    
    
    _protocolUrl = [SafeUtil getUrl: json[@"protocol_url"] ];
    _noticeUrl =[SafeUtil getUrl: json[@"notice_url"] ];
    
   
    _priceVal =[json[@"price"]floatValue];
}



@end
