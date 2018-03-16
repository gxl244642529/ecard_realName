//
//  AdjView.h
//  ecard
//
//  Created by randy ren on 15-2-9.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface AdjView : UIView
@property (weak, nonatomic) IBOutlet UISlider *saturation;
@property (weak, nonatomic) IBOutlet UISlider *brightness;
@property (weak, nonatomic) IBOutlet UISlider *contrast;

@end
