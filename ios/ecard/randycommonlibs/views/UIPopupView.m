//
//  UIPopupView.m
//  ecard
//
//  Created by randy ren on 14-7-16.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "UIPopupView.h"
#import "Bg.h"
#import <ecardlib/ecardlib.h>

@interface UIPopupView()
{
    Bg* _bg;
    UILabel* _label;
    
}

@end

@implementation UIPopupView
-(void)dealloc{
    _bg = NULL;
    _label = NULL;
}


-(void)createControls{
    
    CGRect rect = CGRectMake((self.bounds.size.width - kTableAlertWidth)/2, (self.bounds.size.height - _height)/2, kTableAlertWidth, _height + 40);
    _bg = [[Bg alloc]initWithFrame:rect];
    [_bg repaint];
    [self addSubview:_bg];
    
    CGRect rectIn = CGRectMake(10, 10, rect.size.width - 20, rect.size.height - 20);
    
    _label = [[UILabel alloc]initWithFrame:CGRectMake(rectIn.origin.x, rectIn.origin.y, rectIn.size.width, 20)];
    _label.backgroundColor = [UIColor clearColor];
	_label.textColor = [UIColor blackColor];
	_label.font = [UIFont boldSystemFontOfSize:14.0];
    [_bg addSubview:_label];
    _label.textAlignment = NSTextAlignmentCenter;
    _label.text = _title;
    
    _contentView.frame = CGRectMake(10, 30, rect.size.width-20, _height);
    
    [_bg addSubview:_contentView];

}


- (void)fadeIn
{
    _bg.transform = CGAffineTransformMakeScale(1.3, 1.3);
    _bg.alpha = 0;
    [UIView animateWithDuration:.35 animations:^{
        _bg.alpha = 1;
        _bg.transform = CGAffineTransformMakeScale(1, 1);
    }];
}

-(void)removeFromSuperview{
    [UIView animateWithDuration:.35 animations:^{
        _bg.transform = CGAffineTransformMakeScale(0.1, 0.1);
        _bg.alpha = 0.0;
    } completion:^(BOOL finished) {
        if (finished) {
            [super removeFromSuperview];
        }
    }];
    
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
