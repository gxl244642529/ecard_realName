//
//  TouchableView.h
//  eCard
//
//  Created by randy ren on 14-1-26.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface TouchableView : UIView
{
    BOOL _isTouched;
}
-(void)setTarget :(id)target withAction:(SEL)selector;
-(void)setLoginTarget:(UIViewController*)target withAction:(SEL)selector;
-(void)setLoginTarget:(id)target parent:(UIViewController*)parent withAction:(SEL)selector;
@end
