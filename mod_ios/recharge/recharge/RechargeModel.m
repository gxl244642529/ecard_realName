//
//  RechargeModel.m
//  ecard
//
//  Created by randy ren on 16/2/23.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "RechargeModel.h"
#import <DMLib/DMLib.h>

@implementation RechargeModel
-(void)submit:(NSString*)cardId fee:(NSInteger)fee button:(UIView*)button{
    DMApiJob* job = [self createApi:@"recharge/submit"];
    job.crypt = CryptType_Both;
    [job put:@"cardId" value:cardId];
    [job put:@"fee" value:[NSNumber numberWithInteger:fee]];
    job.button = button;
    job.waitingMessage = @"请稍等...";
    [job execute];
}


-(void)invoce:(NSString*)id button:(UIView*)button{
    DMApiJob* job = [self createApi:@"recharge/invoce"];
    [job put:@"id" value:id];
    job.button = button;
    job.waitingMessage = @"请稍等...";
    [job execute];

}
/**
 退款
 */
-(void)refund:(NSString*)id button:(UIView*)button{
    DMApiJob* job = [self createApi:@"recharge/refund"];
    job.crypt = CryptType_Both;
    [job put:@"id" value:id];
    job.button = button;
    job.waitingMessage = @"请稍等...";
    [job execute];
}

@end
