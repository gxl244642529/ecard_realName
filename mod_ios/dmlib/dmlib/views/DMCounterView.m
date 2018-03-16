//
//  SafeCounterView.m
//  ecard
//
//  Created by randy ren on 16/1/28.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "DMCounterView.h"
#import "DMMacro.h"

@interface DMCounterView()
{
    __weak UILabel* _text;
    NSInteger _current;
    NSInteger _max;
}

@end

@implementation DMCounterView

-(void)awakeFromNib{
    NSInteger index = 0;
    for (UIView*  view in self.subviews) {
        if([view isKindOfClass:[UIButton class]]){
            UIButton* btn = self.subviews[index];
             Control_AddTarget(btn, onSub);
            _text = self.subviews[index+1];
            btn = self.subviews[index+2];
            
           
            Control_AddTarget(btn, onAdd);
            break;
        }
        ++index;
    }
   
    _current= 1;
   
}
-(void)setCount:(NSInteger)count{
    _current = count;
    _text.text = [NSString stringWithFormat:@"%d",(int)count];
}

-(void)onAdd:(id)sender{
    if(_current<_max){
        ++_current;
        _text.text = [NSString stringWithFormat:@"%d",(int)_current];
        [_delegate onCountChanged:_current];
    }
    
}

-(void)onSub:(id)sender{
    if(_current>1){
        --_current;
        _text.text = [NSString stringWithFormat:@"%d",(int)_current];
        [_delegate onCountChanged:_current];
    }
}


-(id)val{
    return nil;
}
-(void)setVal:(id)val{
    _max = [val integerValue];
}
-(NSInteger)count{
    return _current;
}

@end
