//
//  SAddrCell.m
//  ecard
//
//  Created by randy ren on 15-1-9.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "SAddrCell.h"

@implementation SAddrCell

- (void)awakeFromNib
{
  [super awakeFromNib];
    // Initialization code
    self.selectionStyle = UITableViewCellSelectionStyleNone;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
