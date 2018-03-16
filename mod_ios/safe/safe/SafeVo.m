//
//  SafeVo.m
//  ecard
//
//  Created by randy ren on 16/1/16.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "SafeVo.h"
#import "SafeUtil.h"

/**
 
 
 
 
 
 
 */

@implementation SafeVo


-(void)fromJson:(NSDictionary*)json{
    _title = json[@"title"];
    _inId = json[@"insurance_id"];
    _detail= json[@"compensate"];
    _image = [SafeUtil getUrl:json[@"icon_url"]];
    _price = [NSString stringWithFormat:@"¥%@",json[@"info"]];
    _orgPrice =[NSString stringWithFormat:@"¥%@",json[@"ori_price"]];
    _type = [json[@"promotion_type"]integerValue];
    _isSale = [json[@"on_sale"]boolValue];
    _typeId = json[@"type_id"];
    _type = [_typeId integerValue];
}

-(UIImage*)promotion{
    
    if(_type==1){
        return [UIImage imageNamed:@"ic_insurance_flag_hot"];
    }else if(_type==2){
        return [UIImage imageNamed:@"ic_insurance_flag_new"];
    }else{
        return nil;
    }

}

IMPLEMENT_HIDDEN_PROPERTY(price){
    return !_isSale;
}

@end
