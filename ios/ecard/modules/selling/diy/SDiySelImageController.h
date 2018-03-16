//
//  SDiySelImageController.h
//  ecard
//
//  Created by randy ren on 15/4/13.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "MyViewController.h"
#import "PullToRefreshGridTableView.h"

@interface SDiySelImageController : MyViewController<IDataAdapterListener>


@property (nonatomic,copy,readwrite) void(^selComplete)(NSString* image,NSString* ID);

@end
