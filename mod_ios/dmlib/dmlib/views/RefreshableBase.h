//
//  RefreshableBase.h
//  DMLib
//
//  Created by randy ren on 16/1/14.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DMApiJob.h"
#import "DMList.h"
#import "IOnItemClickListener.h"
#import "IDataAdapterListener.h"
#import "DMDataAdapter.h"
#import "DMNetState.h"
#import "DMControllerDelegate.h"


@interface RefreshableBase : NSObject<DMJobDelegate,DMApiCtrl,LoadingStateDelegate,DMControllerDelegate>
{
    __weak __kindof UIScrollView* _scrollView;
}

-(void)from:(id<DMList>)list;
-(void)reloadWithState;

-(DMApiJob*)apiJob;

@property (nonatomic,assign) NSInteger server;

//是否分页
@property (nonatomic,assign) BOOL paged;
//是否可以刷新
@property (nonatomic,assign) BOOL refreshable;
-(DMDataAdapter*)adapter;
-(instancetype)initWithScrollView:(UIScrollView*)scrollView adapter:(DMDataAdapter*)adapter bundle:(NSBundle*)bundle;
-(DMNetState*)netState;

//结束刷新,强制调用,必须要告诉有没有结果
-(void)endRefresh:(BOOL)hasResult;
@end

