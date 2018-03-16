//
//  RechargePayHandler.m
//  ecard
//
//  Created by 任雪亮 on 16/3/3.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "RechargePayHandler.h"
#import "RechargeSuccessController.h"
#import "RechargePayAction.h"
#import "RechargeSuccessController.h"
// WRAP_INTEGER(DMPAY_UMA),
@implementation RechargePayHandler
-(id)initWithParent:(UIViewController*)parent{
    if(self = [super initWithParent:parent payAction:[[RechargePayAction alloc]init] module:@"recharge"
                       supportTypes:@[ WRAP_INTEGER(DMPAY_WEIXIN),WRAP_INTEGER(DMPAY_ETONGKA),
                                      WRAP_INTEGER(DMPAY_CMB)]]){
        
        self.paySuccessAction = PaySuccessAction_BacktoPreviousController;
        self.payResultController = [RechargeSuccessController class];
        
    }
    return self;

    
}
@end
