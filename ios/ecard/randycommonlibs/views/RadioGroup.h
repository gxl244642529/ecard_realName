//
//  RadioGroup.h
//  ecard
//
//  Created by randy ren on 15/7/9.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <UIKit/UIKit.h>

@class RadioGroup;

@protocol RadioGroupDelegate <NSObject>

-(void)radioGroup:(RadioGroup*)group selectChanged:(NSString*)value;

@end

@interface RadioGroup : UIView

-(void)setValues:(NSArray*)arr;
-(void)setValue:(NSString*)value;
-(NSString*)value;

@property (weak,nonatomic) id delegate;

@end
