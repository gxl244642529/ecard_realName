//
//  ToggleButtonGroup.m
//  ecard
//
//  Created by randy ren on 15-1-30.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "ToggleButtonGroup.h"
#import "ToggleButton.h"
#import "CommonMacro.h"
static NSInteger _selectTag;
@interface ToggleButtonGroup()
{
    
}

@end

@implementation ToggleButtonGroup

-(void)awakeFromNib{
    [super awakeFromNib];
    for(ToggleButton* button in self.subviews){
        [button setTarget:self withAction:@selector(onSelect:)];
    }
}


/**
 *  设置文字颜色
 *
 *  @param color       <#color description#>
 *  @param highlighted <#highlighted description#>
 */
-(void)setTextColor:(UIColor*)color highlighted:(UIColor*)highlighted{
    for(ToggleButton* button in self.subviews){
        [button setTextColor:color highlighted:highlighted];
    }
    
    for(ToggleButton* button in self.subviews){
        if(button.tag == _selectTag){
            [button setSelected:YES];
            break;
        }
    }
}

-(NSInteger)getSelectTag{
    return _selectTag;
}
-(NSString*)getSelectLabel{
    NSInteger tag = [self getSelectTag];
    if(tag > 0 ){
        for(ToggleButton* button in self.subviews){
            if(button.tag == tag){
                return button.label;
            }
        }
    }
    return nil;
}
-(void)setSelectTag:(NSInteger)tag{
    for(ToggleButton* button in self.subviews){
        if(button.tag == tag){
            [self onSelect:button];
            break;
        }
    }
    
}
/**
 *
 *  @param view <#view description#>
 */
-(void)onSelect:(ToggleButton*)view{
    if( _selectTag > 0){
       ToggleButton* button = (ToggleButton*)[self viewWithTag:_selectTag];
        [button setSelected:FALSE];
    }
    _selectTag = view.tag;
    [view setSelected:YES];
}
/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

@end
