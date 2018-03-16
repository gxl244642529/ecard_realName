//
//  PullToRefreshCollectionView.h
//  ecard
//
//  Created by randy ren on 15/3/29.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>


#import "IArrayRequestResult.h"
#import "ArrayJsonTask.h"
#import "LoadingState.h"
#import "IPullTorefreshListener.h"
@interface PullToRefreshCollectionView : UICollectionView<IArrayRequestResult,LoadingStateDelegate>

-(void)registerCell:(NSString*)nibName;
-(ArrayJsonTask*)task;
-(void)setTask:(ArrayJsonTask*)task;
-(void)setOnItemClickListener:(NSObject<IOnItemClickListener>*)listener;

-(void)setDataListener:(NSObject<IDataAdapterListener>*)listener;
-(void)setPullToRefreshListener:(NSObject<IPullTorefreshListener>*)listener;

-(void)setLoadingState;

-(void)setEnableState:(BOOL)enabled;
@end
