//
//  SPayCell.m
//  ecard
//
//  Created by randy ren on 15/4/2.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "SPayCell.h"

@implementation SPayCell


- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];
    [self.btnSelected setSelected:!selected];
}

@end
