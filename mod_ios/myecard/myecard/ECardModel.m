//
//  ECardModel.m
//  ecard
//
//  Created by randy ren on 16/1/31.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "ECardModel.h"
#import <DMLib/DMLib.h>
@implementation ECardModel
+(void)bindBarcode:(NSString*)barcode{
    DMApiJob* job = [[DMJobManager sharedInstance]createJsonTask:@"ecard/bindBarcode" cachePolicy:DMCachePolicy_NoCache server:1];
    [job put:@"barcode" value:barcode];
    job.waitingMessage = @"请稍等...";
    [job execute];
}
+(void)unbind:(NSString*)cardId{
    DMApiJob* job = [[DMJobManager sharedInstance]createJsonTask:@"ecard/unbind" cachePolicy:DMCachePolicy_NoCache server:1];
    [job put:@"cardId" value:cardId];
    job.waitingMessage = @"请稍等...";
    [job execute];
}
//修改
+(void)edit:(NSString*)cardId name:(NSString*)name{
    DMApiJob* job = [[DMJobManager sharedInstance]createJsonTask:@"ecard/unbind" cachePolicy:DMCachePolicy_NoCache server:1];
    [job put:@"cardId" value:cardId];
    [job put:@"name" value:name];
    job.waitingMessage = @"请稍等...";
    [job execute];
}
@end
