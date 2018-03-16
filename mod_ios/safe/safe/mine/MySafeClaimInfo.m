//
//  MySafeClaimInfo.m
//  ecard
//
//  Created by 任雪亮 on 16/3/23.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "MySafeClaimInfo.h"



//"delivery_addr":"福建省厦门市思明区fgg","delivery_tel":"15259298519","express_company":null,"delivery_no":null,"name":""
@implementation MySafeClaimInfo
-(void)fromJson:(NSDictionary *)json{
    _addr = json[@"delivery_addr"];
    _phone = json[@"delivery_tel"];
    _name = json[@"name"];
    
    _devCode = [json getString:@"delivery_no"];
    if(!_devCode){
        _devCode = @"审核中，请耐心等待...";
    }
    _devCom = [json getString:@"express_company"];
    if(!_devCom){
        _devCom = @"审核中，请耐心等待...";
    }
}
@end
