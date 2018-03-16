//
//  SOrderCell.m
//  ecard
//
//  Created by randy ren on 15-1-22.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "SOrderCell.h"

@implementation SOrderCell

- (void)awakeFromNib
{
  [super awakeFromNib];
    self.image.layer.cornerRadius = 5;
    self.image.layer.masksToBounds = YES;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
