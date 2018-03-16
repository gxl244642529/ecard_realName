//
//  BaseTableCell.m
//  eCard
//
//  Created by randy ren on 14-1-28.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "BaseTableCell.h"
#import <ecardlib/ecardlib.h>

@implementation BaseTableCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        // Initialization code
    }
    return self;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated
{
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

-(void)awakeFromNib{
  [super awakeFromNib];
    self.selectedBackgroundView=[[UIView alloc]initWithFrame:self.bounds];
    [self.selectedBackgroundView setBackgroundColor:[ColorUtil itemHighlightColor]];
}

@end
