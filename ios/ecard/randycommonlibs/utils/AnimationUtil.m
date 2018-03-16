//
//  AnimationUtil.m
//  ecard
//
//  Created by randy ren on 14-9-25.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "AnimationUtil.h"

@implementation AnimationUtil
+(void)flyViewUpDown:(UIView*)view enter:(BOOL)enter delegate:(id)delegate{
    CATransition *animation = [CATransition animation];
    //animation.delegate = self;
    animation.duration = 0.2;
    animation.timingFunction = UIViewAnimationCurveEaseInOut;
    animation.delegate = delegate;
    if(enter)
    {
        animation.type = kCATransitionMoveIn;
        animation.subtype = kCATransitionFromTop;
    }else{
        animation.type = kCATransitionReveal;
        animation.subtype = kCATransitionFromBottom;
    }
    
    
    [[view layer] addAnimation:animation forKey:@"animation"];
}
+(void)flyView:(UIView*)view enter:(BOOL)enter delegate:(id)delegate
{
    CATransition *animation = [CATransition animation];
    //animation.delegate = self;
    animation.duration = 0.2;
    animation.timingFunction = UIViewAnimationCurveEaseInOut;
    animation.delegate = delegate;
    if(enter)
    {
        animation.type = kCATransitionMoveIn;
        animation.subtype = kCATransitionFromRight;
    }else{
        animation.type = kCATransitionReveal;
        animation.subtype = kCATransitionFromLeft;
    }
    
    
    [[view layer] addAnimation:animation forKey:@"animation"];
    
}


@end
