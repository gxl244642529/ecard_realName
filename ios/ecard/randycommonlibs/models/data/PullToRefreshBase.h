//
//  PullToRefreshBase.h
//  randycommonlibs
//
//  Created by randy ren on 14-9-22.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "PullToRefreshViewWrap.h"
#import "LoadingState.h"
#import "ArrayJsonTask.h"
#import <DMLib/dmlib.h>

#import "IPullTorefreshListener.h"


@interface PullToRefreshBase : NSObject<IPullToRefreshViewDelegate,IArrayRequestResult>
{
    UIScrollView* _scrollView;
    LoadingState* _state;
    DMDataAdapter* _adapter;
    
    PullToRefreshViewWrap* _wrap;
}

-(void)onLoadData:(NSInteger)position;
-(id)initWithTask:(ArrayJsonTask*)task;
-(id)initWithListener:(NSObject<IPullTorefreshListener>*)listener;
//刷新数据
-(void)refreshData;
-(JsonTask*)task;
-(UIScrollView*)scrollView;
@end
