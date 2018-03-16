//
//  CheckBoxItemView.h
//  ecard
//
//  Created by randy ren on 15/4/15.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <ecardlib/ecardlib.h>

@class CheckBox;

@protocol CheckBoxDelegate <NSObject>

-(void)checkBox:(CheckBox*)checkBox valueChanged:(BOOL)value;

@end

@interface CheckBox: ItemView



-(void)setSelected:(BOOL)selected;
-(BOOL)selected;

@property (nonatomic,weak) id<CheckBoxDelegate> delegate;

@end
