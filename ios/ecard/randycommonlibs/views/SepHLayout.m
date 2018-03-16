//
//  SepHLayout.m
//  ecard
//
//  Created by randy ren on 15-2-6.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "SepHLayout.h"

@implementation SepHLayout

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
    }
    return self;
}

-(void)layoutSubviews{
    [super layoutSubviews];
    
    CGRect rect = self.frame;
    __block NSArray* arr = self.subviews;
    float everage = rect.size.width / arr.count;
    int index = 0;
    for (UIView* view in arr) {
        view.frame = CGRectMake(everage*index++, 0, everage,rect.size.height);
    }
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
