//
//  RadioGroup.m
//  ecard
//
//  Created by randy ren on 15/7/9.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "RadioGroup.h"



@interface RadioGroup()
{
    NSArray* _values;
}

@end

@implementation RadioGroup



-(void)setValues:(NSArray*)arr{
    _values = arr;
}
-(void)setValue:(NSString*)value{
    int index = 0;
    for (NSString* str in _values) {
      UIButton* button = self.subviews[index];
        if([str isEqualToString:value]){
            button.selected = YES;
        }else{
            button.selected = NO;
        }
        index++;
    }
}

-(NSString*)value{
    int index = 0;
    for (UIButton* button in self.subviews) {
        if(button.selected){
            return _values[index];
        }
        index ++;
    }
    
    return nil;
}


-(void)dealloc{
    _values = nil;
}

-(void)awakeFromNib{
  [super awakeFromNib];
    NSInteger tag = 0;
    for (UIButton* button in self.subviews) {
        button.tag = tag;
        [button addTarget:self action:@selector(onTap:) forControlEvents:UIControlEventTouchUpInside];
        tag ++;
    }
}

-(void)onTap:(UIButton*)button{
    NSInteger index = 0;
    for (UIButton* btn in self.subviews) {
        if(btn == button){
            btn.selected = YES;
            [_delegate radioGroup:self selectChanged: _values[index]];
        }else{
            btn.selected = NO;
        }
        index ++;
    }
}



@end
