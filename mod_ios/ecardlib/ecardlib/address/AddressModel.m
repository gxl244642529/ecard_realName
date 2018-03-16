//
//  AddressModel.m
//  ecard
//
//  Created by randy ren on 16/2/25.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "AddressModel.h"

@implementation AddressModel
+(void)deleteAddress:(AddressVo*)address{
    DMApiJob* job = [[DMJobManager sharedInstance]createJsonTask:@"address/delete" cachePolicy:DMCachePolicy_NoCache server:1];
    job.waitingMessage = @"请稍等...";
    [job put:@"id" value:[NSNumber numberWithInteger:address.tyId]];
    [job execute];
}


+(void)queryTransFee:(AddressVo*)address{
    DMApiJob* job = [[DMJobManager sharedInstance]createJsonTask:@"order/transFee" cachePolicy:DMCachePolicy_NoCache server:1];
    [job put:@"addressId" value:[NSNumber numberWithInteger:address.id]];
    [job execute];
}

@end
