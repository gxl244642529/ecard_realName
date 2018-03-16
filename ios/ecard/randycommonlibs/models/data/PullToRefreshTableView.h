//
//  PullToRefreshTableView.h
//  ecard
//
//  Created by randy ren on 15/3/27.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>


#import "IArrayRequestResult.h"
#import "ArrayJsonTask.h"
#import "LoadingState.h"
#import "IPullTorefreshListener.h"
//默认有加载更多
@interface PullToRefreshTableView : UITableView<IArrayRequestResult,LoadingStateDelegate>

-(void)setEnableState:(BOOL)enableState;
-(void)registerCell:(NSString*)nibName;
-(ArrayJsonTask*)task;
-(void)setTask:(ArrayJsonTask*)task;
-(void)setOnItemClickListener:(NSObject<IOnItemClickListener>*)listener;
-(void)setDataListener:(NSObject<IDataAdapterListener>*)listener;
-(void)setPullToRefreshListener:(NSObject<IPullTorefreshListener>*)listener;
-(void)setLineStyle:(UITableViewCellSeparatorStyle)style;
-(DMDataAdapter*)adapter;
-(void)setCellBackgroundColor:(UIColor*)color;


-(void)setLoadingState;
-(void)onLoadingRefresh:(id)sender;

@end
