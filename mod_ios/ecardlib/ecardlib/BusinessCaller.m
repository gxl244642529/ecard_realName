//
//  BusinessCaller.m
//  ecardlib
//
//  Created by 任雪亮 on 16/9/24.
//  Copyright © 2016年 任雪亮. All rights reserved.
//

#import "BusinessCaller.h"

#import "ECardPayCashierController.h"

@implementation BusinessCaller

+(void)selectECard:(UIViewController*)parent delegate:(id<ECardSelectDelegate>)delegate{
    Class clazz = NSClassFromString(@"ECardController");
    DMViewController<ECardPicker>* controller = [[clazz alloc]init];
    controller.delegate = delegate;
    [parent.navigationController pushViewController:controller animated:YES];
}


+(void)callCashier:(UIViewController*)parent{
    [parent.navigationController pushViewController:[[ECardPayCashierController alloc]init] animated:YES];
}

@end
