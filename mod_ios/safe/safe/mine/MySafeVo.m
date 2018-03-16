
//
//  MySafeVo.m
//  ecard
//
//  Created by randy ren on 16/1/23.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "MySafeVo.h"
#import "SafeUtil.h"
#import "SafeContact.h"

@interface MySafeVo()
{
    NSInteger _fee;
}

@end

@implementation MySafeVo

-(NSInteger)fee{
    return _fee;
}

-(void)fromJson:(NSDictionary*)json{
    NSDictionary* dic = json[@"applicant"];
    if([dic isKindOfClass:[NSDictionary class]]){
        _name = dic[@"BUYER_NAME"];
        _phone = dic[@"BUYER_MOBILE"];
        _idCard = dic[@"BUYER_ID"];
        _carNo = [dic getString:@"CAR_NO"];
        _carFrame = [dic getString:@"VIN"];
        
        _address = [dic getString:@"BUY_HOME_ADDR"];
        
        
        
        NSArray* arr = json[@"insurant"];
        if([arr isKindOfClass:[NSArray class]]){
            NSMutableArray* marray = [[NSMutableArray alloc]init];
            for(NSDictionary* data in arr){
                SafeContact* contact = [[SafeContact alloc]init];
                contact.name = data[@"insurant_name"];
                contact.idCard = data[@"insurant_pid"];
                [marray addObject:contact];
            }
            _insurant = marray;
            
        }
         _picc_policy_no = dic[@"picc_policy_no"];
    }
    
   
    
    
  
    _count = [json[@"qty"]integerValue];
    _fee=[json[@"product_price"]doubleValue] * 100 *[json[@"qty"]integerValue];
    
    _title = json[@"product_name"];
    _cardId = [json getString:@"e_card_id"];
    _typeId = _cardId ? 0:1;
    _company = json[@"company"];
    
    
    _price =[NSString stringWithFormat:@"¥%@", json[@"product_price"]];
    
    
    _payPrice = [NSString stringWithFormat:@"%.02f",_count * [json[@"product_price"] floatValue]];
    
    _orgPrice = json[@"ori_price"];
    _orderId = json[@"order_id"];
    _summary = json[@"summary"];
    _startTime = [[json getString:@"start_time"] substringWithRange:NSMakeRange(0,10)];
    _endTime = [json getString:@"end_time"];
    _image =[SafeUtil getUrl: json[@"thumb_url"]];
    _status = [json[@"status"]integerValue];
    _serviceTel = json[@"service_tel"];
    _orderId = json[@"order_id"];
    _guardUrl = [SafeUtil getUrl: json[@"safeguard_url"]];
    _sampleUrl =[SafeUtil getUrl: json[@"sample_url"]];
    _protocolUrl = [SafeUtil getUrl: json[@"protocol_url"] ];
    _noticeUrl =[SafeUtil getUrl: json[@"notice_url"] ];

    _detailUrl =[SafeUtil getUrl: json[@"detail_url"] ];
    
    id value = json[@"claim_amt"];
    if(value != [NSNull null]){
        _claimsValue =[NSString stringWithFormat:@"¥%.2f",[value floatValue]];
    }
}


-(NSString*)duration{
    if(_startTime && _endTime && _endTime.length>10){
        return [NSString stringWithFormat:@"%@至%@",_startTime,[_endTime substringWithRange:NSMakeRange(0,10)]];
    }else{
        return nil;
    }

}
-(NSString*)statusTag{
    switch (_status) {
        case 1:
            return @"您的报失已经受理，保险公司将在三个工作日内核实处理";
        case 2:
            return @"您的报失已经受理，预计三个工作日内核实确认，请您耐心等待";
        case 3:
            return @"您的e通卡已快递寄出，请留意查收";
    }
    return nil;
}


/**
 是否应该隐藏报失按钮
 */
-(BOOL)shouldHideLostButton{
    return _status!=0;
}

-(NSString*)statusStr{
    switch (_status) {
        case -1:
            return @"提交失败";
        case 0:
            return @"保障中";
        case 1:
        case 2:
            return @"理赔中";
            
        case 3:
            return @"已理赔";
            
        case 4:
            return @"过期";
            
        case 5:
            return @"已退/换卡";
            
        case 6:
            return @"待生效";
        case 7:
            return @"未付款";
        default:
            return @"";
            
    }
}
@end
