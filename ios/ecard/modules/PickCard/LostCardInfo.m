//
//  LostCardInfo.m
//  ecard
//
//  Created by randy ren on 14-5-18.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "LostCardInfo.h"
#import <DMLib/dmlib.h>
@implementation LostCardInfo

-(void)dealloc{
    self.phone = NULL;
}

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        // Initialization code
    }
    return self;
}
- (IBAction)onCall:(id)sender {
    [CommonUtil makePhoneCall:self.phone parent:self];
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
