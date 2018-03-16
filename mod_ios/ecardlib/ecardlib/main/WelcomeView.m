//
//  WelcomeView.m
//  ecard
//
//  Created by randy ren on 15/4/24.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "WelcomeView.h"
#import "CommonMacro.h"
#import "ViewUtil.h"

@interface WelcomeView ()
{
    
    ViewPager* _viewPager;
    ScrollCtrl* _scroll;
}

@end

@implementation WelcomeView

-(void)dealloc{
    _viewPager = nil;
    _scroll = nil;
}

-(id)initWithFrame:(CGRect)frame{
    if(self = [super initWithFrame:frame]){
        self.backgroundColor = RGB(f0,f0,f0);
        _viewPager = [[ViewPager alloc]initWithFrame:CGRectMake(0, 0, frame.size.width, frame.size.height-20)];
        [self addSubview:_viewPager];
        [_viewPager setDataSource:self];
        
        _scroll = [[ScrollCtrl alloc]initWithFrame:CGRectMake(0, frame.size.height-20, frame.size.width, 20)];
        [self addSubview:_scroll];
        [_scroll setItems:4];
        [_scroll setCurrentItem:0];
    }
    return self;
}
/**
 视图数量
 */
-(NSInteger)getPageCount{
    return 4;
}
/**
 创建视图
 */
-(UIView*)instantiateItem:(NSInteger)index parent:(UIView*)parent frame:(CGRect)frame{
    UIImageView* imageView = [[UIImageView alloc]initWithFrame:frame];
    imageView.contentMode = UIViewContentModeScaleAspectFit;
    imageView.image = [UIImage imageNamed:[NSString stringWithFormat:@"gard%d",(int)index+1]];
    if(index==3){
        if(![imageView viewWithTag:1000]){
            imageView.userInteractionEnabled = YES;
            UIButton* btn = [[UIButton alloc]initWithFrame:CGRectMake((frame.size.width - 130)/2, frame.size.height - 20 - 30, 130, 38)];
            [btn setTitle:@"立即体验" forState:UIControlStateNormal];
            [ViewUtil setButtonBg:btn];
            [imageView addSubview:btn];
            btn.tag = 1000;
            Control_AddTarget(btn, onGoto);
        }
    }
    return imageView;
}

-(void)onGoto:(id)sender{
    [self.delegate onWelcomeComplete];
}


-(void)viewPager:(UIView *)view didSelectedIndex:(NSInteger)index{
    [_scroll setCurrentItem:index];
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
