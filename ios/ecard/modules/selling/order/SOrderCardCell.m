//
//  SOrderCardCell.m
//  ecard
//
//  Created by randy ren on 15/4/1.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "SOrderCardCell.h"

@implementation SOrderCardCell

- (void)awakeFromNib {
  [super awakeFromNib];
    // Initialization code
    self.image.layer.cornerRadius = 3;
    self.image.layer.masksToBounds = YES;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
