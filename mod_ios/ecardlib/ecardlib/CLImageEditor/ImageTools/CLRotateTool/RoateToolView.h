//
//  RoateToolView.h
//  ecard
//
//  Created by randy ren on 15/5/7.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ItemView.h"
@interface RoateToolView : UIView
@property (weak, nonatomic) IBOutlet UISlider *slider;
@property (weak, nonatomic) IBOutlet ItemView *btn1;
@property (weak, nonatomic) IBOutlet ItemView *btn2;
@property (weak, nonatomic) IBOutlet ItemView *btn3;

@end
