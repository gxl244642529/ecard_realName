//
//  DMRateConstraint.m
//  ecard
//
//  Created by randy ren on 16/1/13.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "DMRateConstraint.h"

@implementation DMRateConstraint

-(void)awakeFromNib{
    
    self.constant = [UIScreen mainScreen].bounds.size.width * _rate;
}

@end
