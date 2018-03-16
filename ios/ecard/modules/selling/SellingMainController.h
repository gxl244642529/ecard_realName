//
//  SellingMainController.h
//  ecard
//
//  Created by randy ren on 15/3/28.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "MyViewController.h"
#import "MenuGroup.h"
#import "PullToRefreshTableView.h"

@interface SellingMainController : MyViewController<MenuViewDelegate,IDataAdapterListener,IOnItemClickListener>

+(SellingMainController*)current;
@end
