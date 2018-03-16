//
//  ToggleButton.m
//  ecard
//
//  Created by randy ren on 15-1-30.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "ToggleButton.h"

@interface ToggleButton()
{
    UIImageView* _image;
    UILabel* _label;
    
    UIColor* _normalColor;
    UIColor* _highColor;
    
    BOOL _selected;
}

@end

@implementation ToggleButton
-(NSString*)label{
    return _label.text;
}
-(void)awakeFromNib{
    [super awakeFromNib];
    
    
    
    _label = (UILabel*)[self viewWithTag:1];
    _image = (UIImageView*)[self viewWithTag:2];
    if(_selected){
        [_label setTextColor:_highColor];
    }else{
        [_label setTextColor:_normalColor];
    }
    [_image setHighlighted:_selected];
    
    
}

-(void)dealloc{
    _image = NULL;
    _label = NULL;
}

-(void)setSelected:(BOOL)selected{
    _selected = selected;
    if(_label){
        if(selected){
            [_label setTextColor:_highColor];
        }else{
            [_label setTextColor:_normalColor];
        }
        [_image setHighlighted:selected];

    }
    }

/**
 *  设置文字颜色
 *
 *  @param color       <#color description#>
 *  @param highlighted <#highlighted description#>
 */
-(void)setTextColor:(UIColor*)color highlighted:(UIColor*)highlighted{
    _normalColor = color;
    _highColor = highlighted;
}

@end
