//
//  DMPhoneButton.m
//  DMLib
//
//  Created by randy ren on 16/1/25.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMPhoneButton.h"

#import "DMMacro.h"
#import "CommonUtil.h"


@implementation DMPhoneButton


-(void)awakeFromNib{
    [super awakeFromNib];
    [self addTarget:self action:@selector(onClick) forControlEvents:UIControlEventTouchUpInside];
}

-(void)onClick{
       [CommonUtil makePhoneCall:self.titleLabel.text parent:self];
}

@end
