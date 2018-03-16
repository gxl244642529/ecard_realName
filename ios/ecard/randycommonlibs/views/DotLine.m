//
//  DotLine.m
//  ecard
//
//  Created by randy ren on 16/1/4.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "DotLine.h"

@implementation DotLine
-(void)awakeFromNib{
    [super awakeFromNib];
    UIImage *backgroundImage = [UIImage imageNamed:@"recharge_line_bg.png"];
    UIColor *color = [[UIColor alloc] initWithPatternImage:backgroundImage];
    [self setBackgroundColor:color];
}


@end
