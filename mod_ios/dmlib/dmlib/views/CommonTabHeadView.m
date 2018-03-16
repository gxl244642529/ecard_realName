//
//  CommonTabHeadView.m
//  ecard
//
//  Created by randy ren on 16/1/24.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "CommonTabHeadView.h"
#import "DMTabIndicatorView.h"

@interface CommonTabHeadView()
{
    DMTabIndicatorView* _indicator;
    NSMutableArray<UIButton*>* _buttons;
}

@end

@implementation CommonTabHeadView

-(void)dealloc{
    _buttons = nil;
    _indicator = nil;
}

-(void)awakeFromNib{
   
    
    NSArray* arr = [_titles componentsSeparatedByString:@","];
    _buttons = [[NSMutableArray alloc]initWithCapacity:arr.count];
    _indicator = [[DMTabIndicatorView alloc]initWithColor:[UIColor redColor] count:arr.count];
    [self addSubview:_indicator];
    _indicator.tag = 100;
    self.innerTabTag = 100;
    for(NSString* str in arr){
        UIButton* btn = [[UIButton alloc]init];
        [self addSubview:btn];
        [btn.titleLabel setFont:[UIFont systemFontOfSize:13]];
        [btn setTitleColor:[UIColor lightGrayColor] forState:UIControlStateNormal];
        [btn setTitleColor:[UIColor redColor] forState:UIControlStateSelected];
        [btn setTitle:str forState:UIControlStateNormal];
        [_buttons addObject:btn];
    }
    
    
    [super awakeFromNib];
}

-(void)layoutSubviews{
    [super layoutSubviews];
    NSInteger everageWidth = self.frame.size.width/_buttons.count;
    NSInteger i = 0;
    for(UIButton* btn in _buttons){
        btn.frame = CGRectMake(everageWidth*i, 0, everageWidth, self.frame.size.height-1);
        ++i;
    }
    _indicator.frame = CGRectMake(0, self.frame.size.height-1, self.frame.size.width, 1);
}

@end
