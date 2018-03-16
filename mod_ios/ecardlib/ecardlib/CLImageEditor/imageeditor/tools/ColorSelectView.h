//
//  ColorSelectView.h
//  ecard
//
//  Created by randy ren on 15-2-10.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol ColorSelectDelegate <NSObject>

-(void)onColorSelected:(UIColor*)color;

@end

@interface ColorSelectView : UIView

@property (nonatomic,weak) NSObject<ColorSelectDelegate>* delegate;

@end
