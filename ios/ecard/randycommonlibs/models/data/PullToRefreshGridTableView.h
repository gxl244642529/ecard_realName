//
//  PullToRefreshGridTableView.h
//  ecard
//
//  Created by randy ren on 15/4/1.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>


#import "IArrayRequestResult.h"
#import "ArrayJsonTask.h"
#import "IPullTorefreshListener.h"
#import "LoadingState.h"
#import "NetState.h"


@interface PullToRefreshGridTableView : UITableView<LoadingStateDelegate,IArrayRequestResult>


-(void)setEnableState:(BOOL)enableState;
-(void)registerCell:(NSString*)nibName;
-(ArrayJsonTask*)task;
-(void)setTask:(ArrayJsonTask*)task;
-(void)setOnItemClickListener:(NSObject<IOnItemClickListener>*)listener;

-(void)setDataListener:(NSObject<IDataAdapterListener>*)listener;
-(void)setPullToRefreshListener:(NSObject<IPullTorefreshListener>*)listener;

-(DMDataAdapter*)adapter;

-(void)setLoadingState;

-(void)onNoResult;
-(void)setCols:(int)cols;


@end
