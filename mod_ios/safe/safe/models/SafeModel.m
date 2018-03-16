//
//  SafeModel.m
//  ecard
//
//  Created by randy ren on 16/1/27.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "SafeModel.h"
#import "SafePayResult.h"

@implementation SafeModel



-(void)getOrderInfo:(NSString*)orderId{
    DMApiJob* job = [self createApi:@"i_m_safe/detail"];
    [job put:@"orderId" value:orderId];
    job.clazz = [MySafeClaimInfo class];
    job.waitingMessage = @"正在查询详情...";
    [job execute];

}

-(void)submit:(NSMutableDictionary*)data button:(UIButton*)button{
    DMApiJob* job = [self createApi:@"i_safe/submitV2"];
    [job putAll:data];
    job.button = button;
    job.waitingMessage = @"正在提交资料...";
    job.crypt = CryptType_Both;
    [job execute];
    
}
-(void)submitInsured:(NSMutableDictionary*)data typeId:(NSString*)typeId inId:(NSString*)inId count:(NSInteger)count button:(UIButton*)button{
    DMApiJob* job = [self createApi:@"i_safe/submitInsured"];
    [job putAll:data];
    [job put:@"inId" value:inId];
    [job put:@"typeId" value:typeId];
    [job put:@"count" value:WRAP_INTEGER(count)];
    job.button = button;
    job.waitingMessage = @"正在提交资料...";
    job.crypt = CryptType_Both;
    [job execute];
}
-(void)getPayInfo:(MySafeVo*)data{
    DMApiJob* job = [self createApi:@"i_safe/payInfo"];
    [job put:@"piccOrderId" value:data.orderId];
    job.waitingMessage = @"正在获取订单信息...";
    job.crypt = CryptType_Both;
    [job execute];
}

-(void)canBuyCardSafe:(NSString*)cardId idCard:(NSString*)idCard ticket:(NSString*)ticket button:(UIButton*)button{
    DMApiJob* job = [self createApi:@"i_safe/checkData"];
    [job put:@"idCard" value:idCard];
    [job put:@"cardId" value:cardId];
    [job put:@"ticket" value:ticket];
    job.button = button;
    job.crypt = CryptType_Upload;
    job.waitingMessage = @"请稍等...";
    [job execute];

}


@end
