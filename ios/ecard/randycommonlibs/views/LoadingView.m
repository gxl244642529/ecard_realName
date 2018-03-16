//
//  LoadingView.m
//  randycommonlibs
//
//  Created by randy ren on 14-7-22.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "LoadingView.h"
#import "LibConfig.h"

@implementation LoadingView

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        self.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
        // Initialization code
        self.backgroundColor =[LibConfig defaultPageColor];
        UIActivityIndicatorView* view = [[UIActivityIndicatorView alloc]initWithFrame:CGRectMake((self.bounds.size.width-32)/2, (self.bounds.size.height-32)/2, 32, 32)];
        view.tag = 1;
        [self addSubview:view];
        [view setActivityIndicatorViewStyle:UIActivityIndicatorViewStyleGray];
        [view startAnimating];
        

    }
    return self;
}


-(void)layoutSubviews{
    [super layoutSubviews];
    [self viewWithTag:1].center = CGPointMake(self.bounds.size.width/2, self.bounds.size.height/2);
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

@end
