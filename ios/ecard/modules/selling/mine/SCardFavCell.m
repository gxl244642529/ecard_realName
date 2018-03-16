//
//  SCardFavCell.m
//  ecard
//
//  Created by randy ren on 15/4/9.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "SCardFavCell.h"

@implementation SCardFavCell

-(void)awakeFromNib{
    [super awakeFromNib];
    self.image.layer.cornerRadius = 5;
    self.image.layer.masksToBounds = YES;
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end
