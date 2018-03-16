
//
//  MineItemView.m
//  ecard
//
//  Created by randy ren on 15-1-22.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "MineItemView.h"

@interface MineItemView()
{
    UIButton* _count;
}

@end

@implementation MineItemView
-(void)dealloc{
    _count = NULL;
}
-(void)setCount:(NSInteger)count{
    if(count>0){
        [_count setTitle:[NSString stringWithFormat:@"%ld",(long)count] forState:UIControlStateNormal];
        _count.hidden = NO;
    }else{
        _count.hidden = YES;
    }
    
}
-(void)awakeFromNib{
  [super awakeFromNib];
    _count = (UIButton*)[self viewWithTag:1];
    _count.userInteractionEnabled = NO;
    _count.hidden = YES;
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
