//
//  SAddrListController.h
//  ecard
//
//  Created by randy ren on 15-1-8.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "MyViewController.h"
#import "BaseViewController.h"
#import "IPullTorefreshListener.h"

#import "PullToRefreshTableView.h"
@interface SAddrListController : BaseViewController<IPullTorefreshListener,IOnItemClickListener,IRequestResult,UIAlertViewDelegate>

@end
