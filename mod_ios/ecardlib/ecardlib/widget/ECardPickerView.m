//
//  ECardPickerView.m
//  ecard
//
//  Created by randy ren on 16/1/31.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "ECardPickerView.h"
#import "BusinessCaller.h"

@interface ECardPickerView()
{
    __weak UITextField* _cardId;
    __weak UIViewController* _currentController;
}

@end

@implementation ECardPickerView


-(void)awakeFromNib{
    [super awakeFromNib];
    _currentController = [self findViewController];
    for(__kindof UIView* view in self.subviews){
        if([view isKindOfClass:[UITextField class]]){
            _cardId = view;
        }else if([view isKindOfClass:[UIButton class]]){
            Control_AddTarget(view, onClick);
        }
    }
    
}

-(void)onClick:(id)sender{
    [[_currentController.view findFirstResponsder]resignFirstResponder];
    //选择e通卡
   [BusinessCaller selectECard:_currentController delegate:self];
}

-(void)didSelectECard:(ECard*)ecard{
    
    //想
    _cardId.text = ecard.cardId;
    if(_delegate){
        [_delegate didSelectECard:ecard];
    }
}
@end
