//
//  SMyDiyAddCell.m
//  ecard
//
//  Created by randy ren on 15-1-15.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "SMyDiyAddCell.h"

@implementation SMyDiyAddCell

-(void)awakeFromNib{
    [super awakeFromNib];
    self.image.layer.cornerRadius = 5;
    self.image.layer.masksToBounds = YES;
    
    self.imageMask.layer.cornerRadius = 5;
    self.imageMask.layer.masksToBounds = YES;
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
