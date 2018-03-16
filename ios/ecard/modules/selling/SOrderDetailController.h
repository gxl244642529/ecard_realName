//
//  SOrderDetailController.h
//  ecard
//
//  Created by randy ren on 15/4/2.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "MyViewController.h"
#import "ECardTaskManager.h"
#import "SellingPayController.h"
@interface SOrderDetailController : MyViewController<IRequestResult,IDataAdapterListener,UIAlertViewDelegate,PaySuccessDelegate>

@property (nonatomic,weak) NSObject<PaySuccessDelegate>* delegate;
@end
