//
//  PopupView.m
//  ecard
//
//  Created by randy ren on 15-1-30.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "PopupView.h"
#import "TouchableView.h"
#import <dmlib/dmlib.h>
@interface PopupView()
{
    TouchableView* _background;
    UIView* _contentView;
    __weak id _target;
    SEL _selector;
}

@end

@implementation PopupView
-(void)dealloc{
    _contentView = NULL;
    _background = NULL;
}
-(id)initWithTarget:(id)target selector:(SEL)sel{
    if(self=[super init]){
        _target = target;
        _selector = sel;
    }
    return self;
}
-(void)tapTableview:(id)sender{
    
    [self removeFromSuperview];
}
-(void)removeFromSuperview{
    _contentView.transform = CGAffineTransformTranslate(_contentView.transform,0, 0);
    
    [self fadeBackround];
    [UIView animateWithDuration:.35 animations:^{
        _contentView.transform = CGAffineTransformTranslate(_contentView.transform,0, _contentView.frame.size.height);
        
    } completion:^(BOOL finished) {
        PerformSelector(_target, _selector, self);
        [super removeFromSuperview];
    }];
}
-(void)fadeBackround{
    [UIView animateWithDuration:0.2 animations:^{
		_background.alpha = 0;
	}];
}
-(void)animationContent:(UIView*)contentView{
    contentView.transform = CGAffineTransformTranslate(contentView.transform,0, contentView.frame.size.height);
    [UIView animateWithDuration:.35 animations:^{
        contentView.transform = CGAffineTransformTranslate(contentView.transform,0, -contentView.frame.size.height);
    }];
}

-(void)show:(UIView*)contentView{
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
    contentView.center = CGPointMake(self.frame.size.width/2, self.frame.size.height-contentView.frame.size.height/2);
    [self addSubview:contentView];
    _contentView = contentView;
    [self animationContent:contentView];
    

}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

@end
