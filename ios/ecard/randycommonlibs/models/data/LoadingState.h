//
//  LoadingState.h
//  randycommonlibs
//
//  Created by randy ren on 14-9-21.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>
//jiazai状态
#import <ecardlib/ecardlib.h>


@interface LoadingState : NSObject


-(id)initWithScrollView:(UIView*)scrollView;
-(id)initWithParentView:(UIView*)parentView;

-(void)onRefresh;
-(void)onSuccess:(BOOL)hasResult;
-(void)onError:(NSString*)errorMessage isNetworkError:(BOOL)isNetworkError;
-(void)setFrame:(CGRect)frame;


-(void)onReload;
-(void)setVisible:(BOOL)visible;
-(void)removeFromSuperView;

@property (nonatomic,weak) id<LoadingStateDelegate> delegate;

@end
