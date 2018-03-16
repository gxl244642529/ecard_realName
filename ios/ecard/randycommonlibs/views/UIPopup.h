//
//  UIPopup.h
//  ecard
//
//  Created by randy ren on 14-7-17.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>

@class UIPopup;

typedef enum _UIPopupResult{
    UIPopupResultOk,
    UIPopupResultCancel
} UIPopupResult;

@protocol UIPopupDelegate <NSObject>

-(void)popupView:(UIPopup*)popup contentView:(UIView*)contentView result:(UIPopupResult)result;

@end

@interface UIPopup : UIView
{
    UIView* _contentView;
     NSString* _title;
    int _height;
}
@property (weak,nonatomic) id<UIPopupDelegate> delegate;


-(void)setContentView:(UIView*)contentView;
-(id)initWithTitle:(NSString*)title;
//显示
-(void)show;


-(void)fadeBackround;

//需要重写
- (void)fadeIn;
-(void)createControls;

-(void)onCancel;
-(void)onOk;

@end
