//
//  LoadingErrorView.m
//  randycommonlibs
//
//  Created by randy ren on 14-9-21.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "LoadingErrorView.h"
#import "LibConfig.h"
#import <ecardlib/ecardlib.h>

@implementation LoadingErrorView

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
        self.backgroundColor = [LibConfig defaultPageColor];
        
    }
    return self;
}
-(void)showError:(BOOL)isNetworkError{
    CGPoint center = CGPointMake(self.frame.size.width/2, self.frame.size.height/2);
    if(isNetworkError){
        
        UILabel* label = [ViewUtil createLabel:@"数据获取失败" parent:self size:14 bold:YES gray:NO];
        label.center = CGPointMake(center.x, center.y+20);
        [self addSubview:label];
        label = [ViewUtil createLabel:@"请检查网络后点击重新加载" parent:self size:12 bold:NO gray:YES];
        label.center = CGPointMake(center.x, center.y+40);
        [self addSubview:label];
        
        UIImageView* imageView = [[UIImageView alloc]initWithImage:[UIImage imageNamed:@"ic_network_error.png"]];
        imageView.center =CGPointMake(center.x, center.y-40);
        [self addSubview:imageView];

    }else{
        UIImageView* imageView = [[UIImageView alloc]initWithImage:[UIImage imageNamed:@"ic_load_error.png"]];
        imageView.tag = 1;
        imageView.center =CGPointMake(center.x, center.y-40);
        [self addSubview:imageView];

    }
}
-(void)layoutSubviews{
    [super layoutSubviews];
    UIView* view = [self viewWithTag:1];
    if(view)
        view.center = CGPointMake(self.bounds.size.width/2, self.bounds.size.height/2);
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
