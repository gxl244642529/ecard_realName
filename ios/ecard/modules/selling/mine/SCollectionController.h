//
//  SCollectionController.h
//  ecard
//
//  Created by randy ren on 15-1-8.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "MyViewController.h"
#import "PullDownMenu.h"
#import "ArrayJsonTask.h"
#import "IPullTorefreshListener.h"
#import "IArrayJsonTask.h"
#import "CollectionGroup.h"
#import "PullToRefreshGridTableView.h"
#import "NetState.h"

@interface SCollectionController : MyViewController<IDataAdapterListener,LoadingStateDelegate>


@end
