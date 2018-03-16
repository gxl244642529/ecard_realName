//
//  ECardPayController.h
//  ecardlib
//
//  Created by 任雪亮 on 16/10/18.
//  Copyright © 2016年 任雪亮. All rights reserved.
//

#import <dmlib/dmlib.h>




@interface ECardPayController : DMViewController


@property (weak,nonatomic) id<DMPayResultDelegate> payDelegate;

@end
