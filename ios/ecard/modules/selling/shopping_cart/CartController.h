//
//  CartController.h
//  ecard
//
//  Created by randy ren on 15/3/31.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "MyViewController.h"
#import "ECardTaskManager.h"
#import "PullToRefreshTableView.h"
#import "SCartCell.h"
#import "PopupWindow.h"
@interface CartController : MyViewController<IArrayRequestResult,IOnItemClickListener,IDataAdapterListener,ISelObserver>

@property (nonatomic,weak) UIViewController* shopController;
@end
