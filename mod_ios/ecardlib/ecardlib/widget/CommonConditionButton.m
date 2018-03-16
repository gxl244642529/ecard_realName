//
//  CommonConditionButton.m
//  ecard
//
//  Created by randy ren on 16/2/21.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "CommonConditionButton.h"

#import "CommonButton.h"

@implementation CommonConditionButton


-(void)awakeFromNib{
    [super awakeFromNib];
    [CommonButton createBackground:self];
}

@end
