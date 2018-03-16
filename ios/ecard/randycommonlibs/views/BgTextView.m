//
//  BgTextView.m
//  ecard
//
//  Created by randy ren on 14-9-25.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "BgTextView.h"
#import <ecardlib/ecardlib.h>
@interface BgTextView()
{
    UILabel* _label;
}

@end

@implementation BgTextView
-(void)dealloc{
    _label = NULL;
}

-(id)initWidth:(NSInteger)width text:(NSString*)text{
    if(self=[super initWithFrame:CGRectZero]){
        _label = [ViewUtil createLabel:text parent:self];
        _label.frame = CGRectMake(5, 5, width-10, 0);
        [_label sizeToFit];
        self.bounds = CGRectMake(0, 0, width, _label.frame.size.height+10);
        [self repaint];
    }
    return self;
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
