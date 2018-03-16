//
//  ViewUtil.h
//  libs
//
//  Created by randy ren on 16/1/9.
//  Copyright © 2016年 damai. All rights reserved.
//

#import <Foundation/Foundation.h>

#import <UIKit/UIKit.h>

@interface DMViewUtil : NSObject
+(id)createViewFormNibName:(NSString*)nibName  bundle:(NSBundle*)bundle owner:(id)owner;
+(id)createViewFormNibName:(NSString*)nibName  bundle:(NSBundle*)bundle;

//查找所有符合class的视图
+(NSMutableArray*)findAllViews:(UIView*)view class:(Class)clazz;

+(NSMutableArray*)findAllViews:(UIView*)view protocol:(Protocol*)protocol;
+(UILabel*)createLabel:(NSString*)label parent:(UIView*)parentView;
+(UILabel*)createLabel:(NSString*)label parent:(UIView*)parentView size:(float)size bold:(BOOL)bold gray:(BOOL)gray;

+(CGFloat)measureViewHeight:(UIView*)view;
+(CGFloat)measureViewHeight:(UIView*)view width:(CGFloat)width;
+(void)setViewData:(UIView*)view data:(id)data key:(NSString*)key;
+(void)setViewData:(UIView*)view data:(id)data;


+(UIView*)findViewByName:(NSString*)name view:(UIView*)view;


+(CGFloat)hideView:(UIView*)view;
+(void)showView:(UIView*)view height:(CGFloat)height;
@end
