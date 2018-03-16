//
//  SafeInsuredContainer.m
//  ecard
//
//  Created by 任雪亮 on 16/3/8.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "SafeInsuredContainer.h"
#import <DMLib/dmlib.h>
@implementation SafeInsuredContainer

-(void)setHidden:(BOOL)hidden{
    NSLayoutConstraint* c = [self findHeight];
    if(hidden){
        c.constant = 0;
    }else{
        DMFixTableView* view1 = self.subviews[1];
        UIView* view2 = self.subviews[2];
        if(_isMutil){
            c.constant = view1.expHeight +view2.height + 16;
            view2.hidden = NO;
        }else{
            c.constant = view1.expHeight;
            view2.hidden = YES;
        }
       
    }
    [super setHidden:hidden];
}

@end
