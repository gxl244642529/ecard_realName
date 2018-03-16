//
//  DMTableView.h
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
#import "DMControllerDelegate.h"

#import "DMValue.h"
@interface DMTableView : UITableView<DMJobDelegate,DMList,DMApiCtrl,IOnItemClickListener,DMValue>

@property (nonatomic,copy) IBInspectable NSString* loadingView;
@property (nonatomic,copy) IBInspectable NSString* networkErrorView;
@property (nonatomic,copy) IBInspectable NSString* noResultView;

//调用的api
@property (nonatomic,copy) IBInspectable NSString* api;
//服务器下标
@property (nonatomic,assign) IBInspectable NSInteger server;
//是否分页
@property (nonatomic,assign) IBInspectable BOOL paged;
//实体类名称
@property (nonatomic,copy) IBInspectable NSString* entityName;
//是否可以刷新
@property (nonatomic,assign) IBInspectable BOOL refreshable;
//点击以后的controller
@property (nonatomic,copy) IBInspectable NSString* controllerName;
@property (nonatomic,copy) IBInspectable NSString* segueName;

@property (nonatomic,copy) IBInspectable NSString* tableHeader;
//自动执行
@property (nonatomic,assign) IBInspectable BOOL autoExecute;

//是否遮掉表头,在有表头的情况下才有效
@property (nonatomic,assign) IBInspectable BOOL maskHeader;
@property (nonatomic,assign) IBInspectable BOOL useCache;

//是否反选,当详细controller退出的时候
@property (nonatomic,assign) IBInspectable BOOL deselectWhenBack;
@property (nonatomic,copy) IBInspectable NSString* bundleName;
@property (nonatomic,copy) IBInspectable NSString* cellName;

//间隔大小
@property (nonatomic,assign) IBInspectable NSInteger space;
-(DMDataAdapter*)adapter;
-(DMApiJob*)apiJob;

-(__kindof UIView*)realHeaderView;

-(void)setOnItemClickListener:(NSObject<IOnItemClickListener>*)listener;
-(void)setListener:(NSObject<IDataAdapterListener>*)listener;

-(void)reloadWithStatus;

@end
