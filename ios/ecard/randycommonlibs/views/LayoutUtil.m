//
//  LayoutUtil.m
//  ecard
//
//  Created by randy ren on 15-1-23.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "LayoutUtil.h"

@implementation LayoutUtil
+(void)layoutView:(VLayout*)layout{
    NSInteger padding = layout.padding;
    NSInteger spacing = layout.spacing;
    NSInteger pos = padding;
    for (UIView* view in layout.subviews) {
        if(view.hidden){
            view.frame = CGRectMake(padding, padding, view.frame.size.width, view.frame.size.height);
        }else{
            view.frame = CGRectMake(padding, pos, view.frame.size.width, view.frame.size.height);
            pos +=spacing + view.frame.size.height;
            
        }
    }
    pos += padding;
    CGRect frame = layout.frame;
    CGRect newFrame=CGRectMake(frame.origin.x, frame.origin.y, frame.size.width, pos);
    layout.frame =newFrame;
}
@end
