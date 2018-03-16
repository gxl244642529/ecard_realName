//
//  PopupView.h
//  ecard
//
//  Created by randy ren on 15-1-30.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface PopupView : UIView

-(id)initWithTarget:(id)target selector:(SEL)sel;

-(void)show:(UIView*)contentView;
@end
