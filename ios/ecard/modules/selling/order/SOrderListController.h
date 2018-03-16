//
//  SOrderListController.h
//  ecard
//
//  Created by randy ren on 15-1-8.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "MyViewController.h"
#import "SellingModel.h"
#import "PullToRefreshTableView.h"
#import "SellingPayController.h"
@interface SOrderListController : MyViewController<IPullTorefreshListener,UIAlertViewDelegate,PaySuccessDelegate,IOnItemClickListener,IArrayRequestResult>

@property (nonatomic) int state;


@end
