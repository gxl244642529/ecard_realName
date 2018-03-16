//
//  TextAdjView.h
//  ecard
//
//  Created by randy ren on 15-2-9.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ColorSelectView.h"

@interface TextAdjView : UIView
@property (weak, nonatomic) IBOutlet UISlider *sSize;
@property (weak, nonatomic) IBOutlet UISlider *sh;
@property (weak, nonatomic) IBOutlet UISlider *sv;
@property (weak, nonatomic) IBOutlet ColorSelectView *select;

@end
