//
//  RadioButtonGroup.h
//  ecard
//
//  Created by randy ren on 14-5-18.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@interface RadioButtonGroup : NSObject
{
    NSMutableArray* _buttons;
    UIButton* _selected;
}

-(void)addButton:(UIButton*)button;

-(void)setSelected:(UIButton*)button;
-(void)setSelectedIndex:(NSInteger)index;
-(NSInteger)getSelected;

@end
