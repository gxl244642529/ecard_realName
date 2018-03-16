
//
//  DMFixConstraint.m
//  ecard
//
//  Created by randy ren on 16/1/14.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "DMFixConstraint.h"

@implementation DMFixConstraint
-(void)awakeFromNib{
  [super awakeFromNib];
    self.constant = self.constant * [UIScreen mainScreen].bounds.size.width / 320 ;
}
@end
