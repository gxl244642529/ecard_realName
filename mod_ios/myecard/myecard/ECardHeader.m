//
//  ECardHeader.m
//  eCard
//
//  Created by randy ren on 14-3-5.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "ECardHeader.h"

@implementation ECardHeader

-(void)awakeFromNib{
    [super awakeFromNib];
     _timeLabel.layer.masksToBounds = YES;
    _timeLabel.layer.cornerRadius = 3;
   
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
