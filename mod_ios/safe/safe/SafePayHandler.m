//
//  SafePayHandler.m
//  ecard
//
//  Created by 任雪亮 on 16/3/9.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "SafePayHandler.h"
#import "SafePayResultController.h"
#import "SafePayAction.h"
#import "SafeCashierController.h"

@implementation SafePayHandler


-(id)initWithParent:(UIViewController *)parent{
 /*   if(self = [super initWithParent:parent payResultController:[SafePayResultController class] payAction:[[SafePayAction alloc]init] cachierController:[SafeCashierController class]]){
        
        
        self.paySuccessAction = PaySuccessAction_BacktoPreviousController;
        self.payCancelAction = PayCancelAction_BacktoCurrentController;
    }*/
    
    if(self = [super initWithParent:parent payAction:[[SafePayAction alloc]init] module:@"safe" supportTypes:@[WRAP_INTEGER( DMPAY_WEIXIN )]]){
        [self setCashierController:[SafeCashierController class]];
        [self setPayResultController:[SafePayResultController class]];
    }
    
    return self;
}
@end
