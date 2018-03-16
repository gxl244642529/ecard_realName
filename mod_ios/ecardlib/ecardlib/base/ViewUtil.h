//
//  ViewUtil.h
//  randycommonlibs
//
//  Created by randy ren on 14-9-22.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
@interface ViewUtil : NSObject

+(BOOL)isLongScreen;

+(__kindof UIView*)findView:(UIView*)view class:(Class)clazz;
+(void)createLoading:(NSInteger)tag container:(UIView*)container;

+(UILabel*)createLabel:(NSString*)label parent:(UIView*)parentView;
+(UILabel*)createLabel:(NSString*)label parent:(UIView*)parentView size:(float)size bold:(BOOL)bold gray:(BOOL)gray;


+(id)createViewFormNibName:(NSString*)nibName owner:(id)owner;
+(UIButton*)createButton:(CGRect)rect normal:(NSString*)normal high:(NSString*)high label:(NSString*)label;


+(UIButton*)createButton:(CGRect)rect label:(NSString*)label normalColor:(UIColor*)normalColor highColor:(UIColor*)highColor;

+(UIButton*)createTabButton:(CGRect)rect normal:(NSString*)normal high:(NSString*)high label:(NSString*)label colorNormal:(UIColor*)colorNormal colorHigh:(UIColor*)colorHigh;

+(void)setButtonBg:(UIButton*)button;
+(void)setNineBg:(UIView*)view image:(NSString*)image;
@end
