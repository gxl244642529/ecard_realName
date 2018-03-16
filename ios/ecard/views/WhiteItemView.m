//
//  WhiteItemView.m
//  ecard
//
//  Created by randy ren on 15-1-20.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "WhiteItemView.h"

@implementation WhiteItemView


-(id)initWithCoder:(NSCoder *)aDecoder{
    if(self=[super initWithCoder:aDecoder]){
        _itemNormalColor = [UIColor whiteColor];
    }
    return self;
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
