//
//  GridTableCell.m
//  randycommonlib
//
//  Created by randy ren on 14-10-22.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "GridTableCell.h"

@implementation GridTableCell

-(void)dealloc{
    self.contentView = NULL;
}

-(void)awakeFromNib{
  [super awakeFromNib];
    self.contentView = (TouchableView*)[self viewWithTag:1];
}

-(void)layoutSubviews{
    [super layoutSubviews];
    
}


@end
