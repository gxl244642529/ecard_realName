//
//  ECardPickerItem.m
//  ecard
//
//  Created by randy ren on 16/2/22.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "ECardPickerItem.h"


@interface ECardPickerItem()
{
    __weak UITextField* _cardId;
}

@end

@implementation ECardPickerItem


-(void)awakeFromNib{
    [super awakeFromNib];
    
    for(__kindof UIView* view in self.subviews){
        if([view isKindOfClass:[UITextField class]]){
            
            _cardId = view;
            break;
        }
    }
    _currentController = [self findViewController];
    [self setTarget:self withAction:@selector(onClick:)];
}

-(void)onClick:(id)sender{
    [[_currentController.view findFirstResponsder]resignFirstResponder];
    
    //选择e通卡
    [BusinessCaller selectECard:_currentController delegate:self];
}

-(void)didSelectECard:(ECard*)ecard{
    
    //想
    _cardId.text = ecard.cardId;
}
@end

