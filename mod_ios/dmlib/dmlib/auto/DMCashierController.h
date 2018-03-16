//
//  DMCashierController.h
//  DMLib
//
//  Created by randy ren on 16/2/21.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMViewController.h"


@protocol DMCachierDelegate <NSObject>

-(void)onCashierCancel;

@end

@interface DMCashierController : DMViewController

@end
