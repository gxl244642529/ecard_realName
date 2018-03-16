//
//  DMLoadingView.m
//  DMLib
//
//  Created by randy ren on 16/1/23.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMLoadingView.h"
#import "DMColorConfig.h"

@implementation DMLoadingView
- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        UIActivityIndicatorView* view = [[UIActivityIndicatorView alloc]initWithFrame:CGRectMake((self.bounds.size.width-32)/2, (self.bounds.size.height-32)/2, 32, 32)];
        view.tag = 1;
        [self addSubview:view];
        [view setActivityIndicatorViewStyle:UIActivityIndicatorViewStyleGray];
        [view startAnimating];
        self.backgroundColor = [UIColor whiteColor];
    }
    return self;
}


-(void)layoutSubviews{
    [super layoutSubviews];
    [self viewWithTag:1].center = CGPointMake(self.bounds.size.width/2, self.bounds.size.height/2);
}

@end
