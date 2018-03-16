//
//  UILineLabel.m
//  ecard
//
//  Created by 任雪亮 on 16/3/21.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "UILineLabel.h"

@implementation UILineLabel

-(void)setText:(NSString *)text{
       NSDictionary *attribtDic = @{NSStrikethroughStyleAttributeName: [NSNumber numberWithInteger:NSUnderlineStyleSingle]};
    NSMutableAttributedString *attribtStr = [[NSMutableAttributedString alloc]initWithString:text attributes:attribtDic];
    self.attributedText=attribtStr;
}

@end
