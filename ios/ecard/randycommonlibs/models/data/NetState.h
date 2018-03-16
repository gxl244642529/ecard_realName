//
//  NetState.h
//  ecard
//
//  Created by randy ren on 15/4/9.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>

#import "LoadingState.h"

@interface NetState : NSObject

-(id)initWithScrollView:(UIView*)scrollView;

-(void)onSuccess:(BOOL)hasResult;
-(void)onError:(NSString*)errorMessage isNetworkError:(BOOL)isNetworkError;

-(void)removeFromSuperView;

-(void)onRefresh;
-(void)onNoResult;

-(void)refreshRect;

@property (nonatomic,weak) id<LoadingStateDelegate> delegate;
@end
