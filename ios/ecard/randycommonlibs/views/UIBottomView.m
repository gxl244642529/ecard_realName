//
//  UIBottomView.m
//  ecard
//
//  Created by randy ren on 14-7-16.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "UIBottomView.h"
#import <ecardlib/ecardlib.h>
@interface UIBottomView()
{

    UILabel* _label;
    UIView* _container;
    UIButton* _btn;
}

@end
@implementation UIBottomView

-(void)dealloc{
    _label = NULL;
    _container = NULL;
}


-(void)onOk:(id)sender{
    [self onOk];
    if(self.delegate){
        [self.delegate popupView:self contentView:_contentView result:UIPopupResultOk];
    }
    
    [self removeFromSuperview];
}

-(void)createControls{
    CGRect rect = [UIScreen mainScreen].bounds;
    //边距20 上面 20 下面20
    int self_height = _height + 60;
    
    _container = [[UIView alloc]initWithFrame:CGRectMake(0, rect.size.height - self_height, rect.size.width, self_height)];
    [self addSubview:_container];
    _container.backgroundColor = [UIColor whiteColor];
    
    _label = [[UILabel alloc]initWithFrame:CGRectMake(10 , 10,rect.size.width-20, 20)];
    _label.backgroundColor = [UIColor clearColor];
	_label.textColor = [UIColor blackColor];
	_label.font = [UIFont boldSystemFontOfSize:14.0];
    _label.textAlignment = NSTextAlignmentCenter;
    _label.text = _title;
    [_container addSubview:_label];
    _contentView.frame = CGRectMake(10, 30, rect.size.width-40, _height);
    
    _btn = [[UIButton alloc]initWithFrame:CGRectMake(10, self_height-30, rect.size.width-20, 20)];
    [_btn setTitle:@"确定" forState:UIControlStateNormal];
    [_container addSubview:_btn];
    [_btn addTarget:self action:@selector(onOk:) forControlEvents:UIControlEventTouchUpInside];
    [_btn setTitleColor:[UIColor darkGrayColor] forState:UIControlStateNormal];
    
    [_container addSubview:_contentView];
}

-(void)removeFromSuperview{
    _container.transform = CGAffineTransformTranslate(_container.transform,0, 0);
    
    [self fadeBackround];
    
    [UIView animateWithDuration:.35 animations:^{
        _container.transform = CGAffineTransformTranslate(_container.transform,0, _container.frame.size.height);
        
    } completion:^(BOOL finished) {
        if (finished) {
            [super removeFromSuperview];
        }
    }];
}

- (void)fadeIn
{
    _container.transform = CGAffineTransformTranslate(_container.transform,0, _container.frame.size.height);
    [UIView animateWithDuration:.35 animations:^{
        _container.transform = CGAffineTransformTranslate(_container.transform,0, -_container.frame.size.height);
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
