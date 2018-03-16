//
//  RechargeButton.m
//  ecard
//
//  Created by randy ren on 15/12/29.
//  Copyright © 2015年 citywithincity. All rights reserved.
//

#import "RechargeButton.h"

@implementation RechargeButton



-(void)awakeFromNib{
    self.layer.borderColor = self.borderColor.CGColor;
    self.layer.borderWidth = 1;
    self.layer.cornerRadius = self.cornerRadius;
}


-(void)setEnabled:(BOOL)enabled{
    [super setEnabled:enabled];
    self.layer.borderColor = enabled ? self.borderColor.CGColor : [UIColor lightGrayColor].CGColor;
}



@end
