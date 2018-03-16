//
//  SCardCell.m
//  ecard
//
//  Created by randy ren on 15-1-15.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "SCardCell.h"

@implementation SCardCell

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
    }
    return self;
}

-(void)awakeFromNib
{
  [super awakeFromNib];
    self.image.layer.cornerRadius= 5;//(值越大，角就越圆)
    self.image.layer.masksToBounds= YES;
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
