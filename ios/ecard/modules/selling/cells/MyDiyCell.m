//
//  MyDiyCell.m
//  ecard
//
//  Created by randy ren on 15-1-14.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "MyDiyCell.h"

@implementation MyDiyCell


-(void)awakeFromNib{
  [super awakeFromNib];
    self.image.layer.cornerRadius = 5;
    self.image.layer.masksToBounds = YES;
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
