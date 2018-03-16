//
//  UIPopup.m
//  ecard
//
//  Created by randy ren on 14-7-17.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "UIPopup.h"
#import <ecardlib/ecardlib.h>
@interface UIPopup()
{
    
    TouchableView* _background;
}
@end

@implementation UIPopup
-(void)dealloc{
    _contentView = NULL;
    _background = NULL;
    _title = NULL;
}
-(void)tapTableview:(id)sender{
    if(self.delegate){
        [self.delegate popupView:self contentView:_contentView result:UIPopupResultCancel];
    }
    [self onCancel];
    
    [self removeFromSuperview];
}
-(void)onCancel{
    
    
}
-(void)onOk{
    
}
-(void)createBackgroundView
{
	// adding some styles to background view (behind table alert)
	self.frame = [[UIScreen mainScreen] bounds];
	self.backgroundColor = [UIColor clearColor];
    
    _background = [[TouchableView alloc]initWithFrame:self.frame];
    [self addSubview:_background];
    [_background setTarget:self withAction:@selector(tapTableview:)];
    
    _background.backgroundColor = [[UIColor blackColor] colorWithAlphaComponent:0.0];
	_background.opaque = NO;
	// adding it as subview of app's UIWindow
	UIWindow *appWindow = [[UIApplication sharedApplication] keyWindow];
	[appWindow addSubview:self];
	
	// get background color darker
	[UIView animateWithDuration:0.2 animations:^{
		_background.backgroundColor = [[UIColor blackColor] colorWithAlphaComponent:0.25];
	}];
}


-(void)fadeBackround{
    [UIView animateWithDuration:0.2 animations:^{
		_background.alpha = 0;
	}];
}
-(void)createControls{
    
}
- (void)show
{
    [self createBackgroundView];
    [self createControls];
    [self fadeIn];
}
-(id)initWithTitle:(NSString*)title{
    if(self=[super init]){
        _title = title;
    }
    return self;
}
- (void)fadeIn
{
}
-(void)setContentView:(UIView*)contentView{
    _contentView = contentView;
    _height = contentView.frame.size.height;
}


@end
