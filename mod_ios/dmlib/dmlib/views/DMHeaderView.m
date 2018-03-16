//
//  DMHeaderView.m
//  DMLib
//
//  Created by randy ren on 16/1/24.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMHeaderView.h"

@interface DMHeaderView(){
    NSInteger _lastHeight;
    __weak UIView* _contentView;
    __weak UIView* _container;
}

@end
@implementation DMHeaderView


-(id)initWithFrame:(CGRect)frame contentView:(UIView*)contentView{
    if(self = [super initWithFrame:frame]){
        _container = contentView;
        _contentView = contentView.subviews[0];
        _lastHeight = _contentView.frame.size.height;
        contentView.frame = frame;
        [self addSubview:contentView];
    }
    return self;
}

-(void)layoutSubviews{
    [super layoutSubviews];
    NSInteger height = _contentView.frame.size.height;
    CGRect rect = CGRectMake(0, 0, self.frame.size.width, height);
    _container.frame = rect;
    self.frame = rect;
    if(height != _lastHeight){
        //需要重新设置高度
        _lastHeight = height;
        [_tableView setTableHeaderView:self];
    }

   
}

@end
