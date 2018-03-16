//
//  StateList.h
//  ecard
//
//  Created by randy ren on 15/12/8.
//  Copyright © 2015年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>

#import <DMLib/dmlib.h>

#import "IArrayRequestResult.h"
#import "ArrayJsonTask.h"
#import "LoadingState.h"
#import "IPullTorefreshListener.h"
#import "NetLoadingState.h"


@interface StateList : UIView<IArrayRequestResult,LoadingStateDelegate>
-(void)registerView:(NSString*)nibName forState:(NSString*)state;
-(id)initWithFrame:(CGRect)frame type:(StateListType)type;
-(void)setCellBackgroundColor:(UIColor*)color;
-(void)registerCell:(NSString*)nibName rowHeight:(NSInteger)rowHeight;

-(UIScrollView*)scrollView;
-(void)setPullToRefreshListener:(NSObject<IPullTorefreshListener>*)listener;
-(ArrayJsonTask*)task;
-(void)setTask:(ArrayJsonTask*)task;
-(void)setOnItemClickListener:(NSObject<IOnItemClickListener>*)listener;
-(void)setDataListener:(NSObject<IDataAdapterListener>*)listener;

-(void)deselectRowAtIndexPath:(NSIndexPath*)indexPath animated:(BOOL)animated;

-(void)setCellSelectionStyle:(UITableViewCellSeparatorStyle)stype;

@end
