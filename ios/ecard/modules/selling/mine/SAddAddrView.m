//
//  SAddAddrView.m
//  ecard
//
//  Created by randy ren on 15-1-9.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "SAddAddrView.h"

@implementation SAddAddrView

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
    }
    return self;
}

-(void)awakeFromNib{
  [super awakeFromNib];
    [self setPadding:5];
    [self setSpacing:5];
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
