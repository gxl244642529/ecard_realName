//
//  RadioButtonGroup.m
//  ecard
//
//  Created by randy ren on 14-5-18.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "RadioButtonGroup.h"
#import "CommonMacro.h"
@implementation RadioButtonGroup

-(id)init{
    if(self=[super init]){
        _buttons = NSMutableArray.new;
        _selected = NULL;
    }
    return self;
}

-(void)dealloc{
    _buttons = NULL;
    _selected=NULL;
}

-(void)setSelected:(UIButton*)button{
    for (UIButton* btn in _buttons) {
        if(btn==button){
            [self setCurrentSelected:button];
            break;
        }
    }
}
-(void)setSelectedIndex:(NSInteger)index{
    if(index<0 )return;
    if(index>_buttons.count-1)return;
    [self setCurrentSelected:_buttons[index]];
}


-(void)setCurrentSelected:(UIButton*)button{
    if(_selected!=button){
        if(_selected){
            _selected.selected=NO;
        }
        _selected = button;
        _selected.selected = YES;
    }
}

-(NSInteger)getSelected{
    if(!_selected)return -1;
    return [_buttons indexOfObject:_selected];
}

-(void)addButton:(UIButton*)button{
    if(!button){
        NSLog(@"no button");
        return;
    }
    [_buttons addObject:button];
    Control_AddTarget(button, onClick);
}

ON_EVENT(onClick){
    
    [self setSelected:sender];
    
}

@end
