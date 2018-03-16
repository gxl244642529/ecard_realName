//
//  MainButton.m
//  ecard
//
//  Created by randy ren on 16/1/13.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "MainButton.h"

@implementation MainButton


-(void)awakeFromNib{
  [super awakeFromNib];
    CGFloat w  = SCREEN_WIDTH * 0.1875;
    
    for (NSLayoutConstraint* c in self.constraints) {
        if(c.firstItem == self && (c.firstAttribute == NSLayoutAttributeHeight || c.firstAttribute == NSLayoutAttributeWidth)){
            c.constant =w;
        }
    }
    
    self.layer.masksToBounds = YES;
    self.layer.cornerRadius = w/2;
}

@end
