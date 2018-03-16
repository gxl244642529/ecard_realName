//
//  DMScrollView.m
//  ecard
//
//  Created by randy ren on 16/1/13.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "DMScrollView.h"

@implementation DMScrollView

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/
- (id)initWithCoder:(NSCoder *)aDecoder {
    self = [super initWithCoder:aDecoder];
    if (self) {
     self.delaysContentTouches = NO; // iterate over all the UITableView's subviews
    for (id view in self.subviews) { // looking for a UITableViewWrapperView
        if ([NSStringFromClass([view class]) isEqualToString:@"UITableViewWrapperView"]) { // this test is necessary for safety and because a "UITableViewWrapperView" is NOT a UIScrollView in iOS7
            if([view isKindOfClass:[UIScrollView class]]) { // turn OFF delaysContentTouches in the hidden subview
                UIScrollView *scroll = (UIScrollView *) view;
                scroll.delaysContentTouches = NO;
            }
            break;
        }
    }
    }
    return self;
}
- (BOOL)touchesShouldCancelInContentView:(UIView *)view {
    if ([view isKindOfClass:[UIButton class]]) { return YES; }
    return [super touchesShouldCancelInContentView:view];
}
@end
