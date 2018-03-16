//
//  ViewFactory.h
//  ecard
//
//  Created by randy ren on 15/3/22.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import "IOnRefreshListener.h"


/**
预留
 */
@protocol LoadingView <NSObject>

@end



@protocol ErrorView <NSObject>

/**
 设置错误信息
 */
-(void)setErrorMessage:(NSString*)error isNetworkError:(BOOL)isNetworkError;

//刷新监听
@property (nonatomic,retain) NSObject<IOnRefreshListener>* onRefreshListener;

@end


@protocol NoResultView <NSObject>


@end


@protocol ViewFactory <NSObject>


/**
  获取
 */
+(id<ViewFactory>)get:(NSString*)key;
/**
 注册
 */
+(void)registerFactory:(NSString*)key factory:(id<ViewFactory>)factory;


/**
 加载视图
 */
-(UIView<LoadingView>*)createLoadingView:(CGRect)rect;


-(UIView<ErrorView>*)createErrorView:(CGRect)rect;


-(UIView<NoResultView>*)createNoResultView:(CGRect)rect;



@end
