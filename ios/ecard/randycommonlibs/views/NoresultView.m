//
//  NoresultView.m
//  randycommonlibs
//
//  Created by randy ren on 14-9-21.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "NoresultView.h"
#import <ecardlib/ecardlib.h>
#import "LibConfig.h"

@implementation NoresultView

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
         self.backgroundColor = [LibConfig defaultPageColor];
        
        UILabel* label = [ViewUtil createLabel:@"没有搜索结果" parent:self];
        label.tag = 1;
        
        label = [ViewUtil createLabel:@"点击刷新" parent:self];
        label.tag = 2;
        
    }
    return self;
}

-(void)layoutSubviews{
    [super layoutSubviews];
    [self viewWithTag:1].center = CGPointMake(self.bounds.size.width/2, self.bounds.size.height/2 - 15);
    [self viewWithTag:2].center = CGPointMake(self.bounds.size.width/2, self.bounds.size.height/2 + 15);
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
