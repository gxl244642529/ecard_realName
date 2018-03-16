//
//  NetLoadingState.h
//  ecard
//
//  Created by randy ren on 15/12/8.
//  Copyright © 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "LoadingState.h"


extern NSString* NetStateType_Loading;
extern NSString* NetStateType_NoResult;
extern NSString* NetStateType_Error;



@interface NetLoadingState : NSObject

-(id)initWithParentView:(UIView*)parentView;


-(void)registerState:(NSString*)viewName forState:(NSString*)type;

-(void)onSuccess:(BOOL)hasResult;
-(void)onError:(NSString*)errorMessage isNetworkError:(BOOL)isNetworkError;

-(void)removeFromSuperView;

-(void)onRefresh;
-(void)onNoResult;

-(void)refreshRect;

@property (nonatomic,weak) id<LoadingStateDelegate> delegate;
@end
