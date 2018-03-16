//
//  DMBorderView.m
//  ecard
//
//  Created by randy ren on 16/1/30.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "DMBorderView.h"

@implementation DMBorderView

-(void)setBorderColor:(UIColor *)borderColor{
    
    self.layer.borderColor = borderColor.CGColor;
    self.layer.borderWidth = 1;
    _borderColor = borderColor;
}


-(void)setCornerRadius:(NSInteger)cornerRadius{
    self.layer.cornerRadius = cornerRadius;
    self.layer.masksToBounds = YES;
    
    _cornerRadius = cornerRadius;
}


@end
