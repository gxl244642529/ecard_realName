//
//  StateList.h
//  libs
//
//  Created by randy ren on 16/1/9.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "DMList.h"
#import "IOnItemClickListener.h"
#import "IDataAdapterListener.h"


typedef enum : NSUInteger {
    StateListType_TableView,
    StateListType_CollectionView,
    StateListType_TableCollectionView,
} StateListType;

#import "DMApiJob.h"

IB_DESIGNABLE
@interface DMStateList : UIView<DMList>

//类型 table/collection
@property (nonatomic,copy) IBInspectable NSString* type;





@property (nonatomic,copy) IBInspectable NSString* loadingView;
@property (nonatomic,copy) IBInspectable NSString* networkErrorView;
@property (nonatomic,copy) IBInspectable NSString* noResultView;

//调用的api
@property (nonatomic,copy) IBInspectable NSString* api;


//服务器下标
@property (nonatomic,assign) IBInspectable NSInteger server;


@property (nonatomic,copy) IBInspectable NSString* sectionHeader;
//是否分页
@property (nonatomic,assign) IBInspectable BOOL paged;
//实体类名称
@property (nonatomic,copy) IBInspectable NSString* entityName;
//是否可以刷新
@property (nonatomic,assign) IBInspectable BOOL refreshable;
//点击以后的controller
@property (nonatomic,copy) IBInspectable NSString* controllerName;

@property (nonatomic,copy) IBInspectable NSString* tableHeader;
@property (nonatomic,assign) IBInspectable BOOL autoExecute;
@property (nonatomic,assign) IBInspectable BOOL useCache;


//注册的cell
@property (nonatomic,copy ) IBInspectable NSString* bundleName;

//注册的cell
@property (nonatomic,copy ) IBInspectable NSString* cellName;
//行高度
@property (nonatomic,assign) IBInspectable NSInteger rowHeight;
//每一行的数量,只对collection有效
@property (nonatomic,assign) IBInspectable NSInteger cols;

@property (nonatomic,assign) IBInspectable BOOL maskHeader;


//是否反选,当详细controller退出的时候
@property (nonatomic,assign) IBInspectable BOOL deselectWhenBack;


-(instancetype)initWithFrame:(CGRect)frame type:(StateListType)type;
-(__kindof UIView*)tableHeaderView;
-(void)deselectRowAtIndexPath:(NSIndexPath*)indexPath animated:(BOOL)animated;
-(void)setCellSelectionStyle:(UITableViewCellSeparatorStyle)stype;
-(void)setOnItemClickListener:(NSObject<IOnItemClickListener>*)listener;
-(void)setDataListener:(NSObject<IDataAdapterListener>*)listener;
-(void)registerCell:(NSString*)nibName rowHeight:(NSInteger)rowHeight bundle:(NSBundle*)bundle;
-(void)setCellBackgroundColor:(UIColor*)color;
-(void)put:(NSString*)key value:(id)value;
-(void)execute;
-(void)reloadWithState;

@end
