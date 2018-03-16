//
//  DMTabIndicatorView.m
//  DMLib
//
//  Created by randy ren on 16/1/24.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMTabIndicatorView.h"

@interface DMTabIndicatorView()
{
    UIView* _view;
    NSInteger _position;
}

@end

@implementation DMTabIndicatorView


-(id)initWithFrame:(CGRect)frame{
    if(self = [super initWithFrame:frame]){
         [self create];
    }
    return self;
}

-(id)init{
    if(self = [super init]){
        [self create];
    }
    return self;
}
-(id)initWithColor:(UIColor*)color count:(NSInteger)count{
    if(self = [super init]){
        _count = count;
        _color = color;
        [self create];
    }
    return self;
}
-(void)create{
    if(!_color){
        _color = [UIColor redColor];
    }
    _view  = [[UIView alloc]initWithFrame:CGRectMake(0, 0, 0, 0)];
    [self addSubview:_view];
    _view.backgroundColor = _color;

}

-(void)dealloc{
    _view = nil;
}

-(void)awakeFromNib{
    [self create];
}

-(void)layoutSubviews{
    [super layoutSubviews];
    CGFloat width = self.frame.size.width/_count;
    _view.frame = CGRectMake(width * _position, 0, width, self.frame.size.height);
}

-(void)setCurrentIndex:(NSInteger)index animated:(BOOL)animated{
    _position = index;
    if(_count==0)return;
    CGFloat width = self.frame.size.width/_count;
    if(animated){
        [UIView animateWithDuration:0.3 animations:^{
            _view.frame = CGRectMake(width * _position, 0, width, self.frame.size.height);
        }];
    }else{
         _view.frame = CGRectMake(width * _position, 0, width, self.frame.size.height);
    }
}

@end
