//
//  DMPayAction.h
//  DMLib
//
//  Created by randy ren on 16/1/28.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "DMApiJob.h"
#import "DMPayHeader.h"
#import "DMModel.h"

@interface DMPayAction : DMModel

-(void)prePay:(NSInteger)type orderId:(NSString*)orderId delegate:(id<DMJobDelegate>)delegate;


-(void)prePay:(NSInteger)type api:(NSString*)api data:(NSDictionary*)data delegate:(id<DMJobDelegate>)delegate;


-(void)getOrderInfo:(NSString*)orderId info:(id)info delegate:(id<DMJobDelegate>)delegate;

@end
