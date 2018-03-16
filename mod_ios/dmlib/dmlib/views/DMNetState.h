//
//  DMNetState.h
//  DMLib
//
//  Created by randy ren on 16/1/23.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <UIKit/UIKit.h>




@protocol LoadingStateDelegate <NSObject>
-(void)onLoadingRefresh:(id)sender;
@end



@protocol LoadingState <NSObject>

@property (nonatomic,weak) id<LoadingStateDelegate> loadingDelegate;

@end

@protocol DMErrorView <LoadingState>

-(void)setError:(NSString*)error isNetworkError:(BOOL)isNetworkError;

@end


@interface DMNetState : NSObject<LoadingStateDelegate>



@property (nonatomic,copy) NSString* loading;
@property (nonatomic,copy) NSString* error;
@property (nonatomic,copy) NSString* noResult;

-(id)initWithView:(UIView*)view bundle:(NSBundle*)bundle;

-(void)onSuccess:(BOOL)hasResult;
-(void)onError:(NSString*)errorMessage isNetworkError:(BOOL)isNetworkError;

-(void)removeFromSuperView;

-(void)onRefresh;
-(void)onNoResult;

-(void)layoutSubViews:(CGRect)frame;

@property (nonatomic,weak) id<LoadingStateDelegate> delegate;
@end
