//
//  DMStateTabView.h
//  ecard
//
//  Created by randy ren on 16/1/23.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>


#import "DMNetState.h"
#import "DMTabDelegate.h"
#import "DMControllerDelegate.h"
#import "IOnItemClickListener.h"
//加载的内容为同一个接口

@protocol DMStateDataSource <NSObject>
//获取下标
-(NSInteger)getDataIndex:(id)data;

@end


IB_DESIGNABLE
@interface DMStateTabView : UIScrollView<IOnItemClickListener,  DMControllerDelegate, LoadingStateDelegate,UIScrollViewDelegate,DMTabDelegate,DMTab>


-(void)setOnitemClickListener:(id<IOnItemClickListener>)listener;

//类别,c,t,ct
@property (nonatomic,copy) IBInspectable NSString* type;
@property (nonatomic,assign) IBInspectable NSInteger tabCount;
//如果对方可以切换,则可以使用这个
@property (nonatomic,assign) IBInspectable NSInteger targetTabTag;


@property (nonatomic,retain) IBInspectable  NSString* loadingView;
@property (nonatomic,retain) IBInspectable  NSString* networkErrorView;
@property (nonatomic,retain) IBInspectable  NSString* noResultView;

//调用的api
@property (nonatomic,copy) IBInspectable NSString* api;
//服务器下标
@property (nonatomic,assign) IBInspectable NSInteger server;
//点击以后的controller
@property (nonatomic,copy) IBInspectable  NSString* controllerName;
//是否可以刷新
@property (nonatomic,assign) IBInspectable  BOOL refreshable;
//实体类名称
@property (nonatomic,copy) IBInspectable  NSString* entityName;

@property (nonatomic,copy) NSString* tableHeader;
//自动执行
@property (nonatomic,assign) IBInspectable BOOL autoExecute;
//使用缓存
@property (nonatomic,assign) IBInspectable BOOL useCache;

//调用的bundle
@property (nonatomic,copy) IBInspectable NSString* bundleName;
//注册的cell
@property (nonatomic,copy ) IBInspectable NSString* cellName;
//行高度
@property (nonatomic,assign) IBInspectable NSInteger rowHeight;
//每一行的数量,只对collection有效
@property (nonatomic,assign) IBInspectable NSInteger rowCount;


@property (nonatomic,weak) id<DMStateDataSource> dataSource;


@property (nonatomic,weak) id<DMTabDelegate> tabDelegate;


-(void)reloadWithStatus;

@end
