//
//  DMButtonGroup.m
//  DMLib
//
//  Created by randy ren on 16/1/23.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMButtonGroup.h"
#import "DMMacro.h"


@interface DMButtonGroup()
{
    NSInteger _selectIndex ;
    NSMutableArray* _buttons;
    //内部的tab
    __weak id<DMTab> _innerTab;
    NSArray* _buttonValues;
}

@end

@implementation DMButtonGroup


-(void)dealloc{
    _buttons = nil;
}
-(id)val{
    if(_selectIndex<0){
        return nil;
    }
    return _buttonValues[_selectIndex];
}


-(void)setVal:(id)val{
    NSInteger index = 0;
    for(NSString* str in _buttonValues){
        if( [str isEqualToString:val]){
            [self setCurrentIndex:index animated:NO];
            break;
        }
        ++index;
    }
}

-(void)awakeFromNib{
    [super awakeFromNib];
    _selectIndex = -1;
    _buttons = [[NSMutableArray alloc]initWithCapacity:self.subviews.count];
    NSInteger index = 0;
    for(UIView* view in self.subviews){
        if([view isKindOfClass:[UIButton class]]){
            Control_AddTarget((UIButton*)view, onSelect);
            view.tag = index++;
            [_buttons addObject:view];
        }
    }
    
    if(_targetTabTag>0){
        //找到
        UIView* target = [self.superview viewWithTag:_targetTabTag];
        if([target conformsToProtocol:@protocol(DMTabDelegate)]){
            _tabDelegate = (id<DMTabDelegate>)target;
        }
    }
    
    if(_innerTabTag > 0){
        id tab =[self.superview viewWithTag:_innerTabTag];
        if( [tab conformsToProtocol:@protocol(DMTab) ]){
            _innerTab = tab;
        }
    }
    
    if(_values){
        _buttonValues = [_values componentsSeparatedByString:@","];
    }
    
    if(_selectable){
       [self setCurrentIndex:_initSelected animated:NO];
        
    }
    
    
}

-(void)tab:(id)tab didSelectIndex:(NSInteger)index{
    [self setCurrentIndex:index animated:YES];
}

-(void)onSelect:(UIButton*)view{
    if(_selectable){
        if( _selectIndex >= 0){
            UIButton* button = _buttons[_selectIndex];
            [button setSelected:FALSE];
        }
        _selectIndex = view.tag;
        [view setSelected:YES];
    }
    [_tabDelegate tab:self didSelectIndex:view.tag];
    if(_innerTab){
        [_innerTab setCurrentIndex:view.tag animated:YES];
    }
}

-(void)setCurrentIndex:(NSInteger)index animated:(BOOL)animated{
    if( _selectIndex >= 0){
        UIButton* button = _buttons[_selectIndex];
        [button setSelected:FALSE];
    }
    _selectIndex = index;
    [_buttons[_selectIndex] setSelected:YES];
    if(_innerTab){
        [_innerTab setCurrentIndex:index animated:animated];
    }
}

@end
