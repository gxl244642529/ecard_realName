//
//  DMList.h
//  DMLib
//
//  Created by randy ren on 16/1/14.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "DMApiView.h"

@protocol DMList <DMApiView>


@property (nonatomic,copy)  NSString* loadingView;
@property (nonatomic,copy)  NSString* networkErrorView;
@property (nonatomic,copy)  NSString* noResultView;
//点击以后的controller
@property (nonatomic,copy)  NSString* controllerName;
//是否可以刷新
@property (nonatomic,assign)  BOOL refreshable;
//是否分页
@property (nonatomic,assign)  BOOL paged;
//实体类名称
@property (nonatomic,copy)  NSString* entityName;

@property (nonatomic,copy) NSString* tableHeader;
//自动执行
@property (nonatomic,assign) BOOL autoExecute;
//使用缓存
@property (nonatomic,assign) BOOL useCache;


//是否反选,当详细controller退出的时候
@property (nonatomic,assign) BOOL deselectWhenBack;


@end

@interface DMList : NSObject



@end
