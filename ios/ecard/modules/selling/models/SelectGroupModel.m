//
//  SelectGroupModel.m
//  ecard
//
//  Created by randy ren on 15-1-31.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "SelectGroupModel.h"
#import <ecardlib/ecardlib.h>

@interface SelectGroupModel()
{
    NSInteger _lastSelectedTag;
    __weak UIView* _parentView;
}

@end

@implementation SelectGroupModel



-(id)initWithParent:(UIView*)view{
    if(self=[super init]){
        _parentView = view;
        for(TouchableView<ISelectable>* v in view.subviews){
            [v setTarget:self withAction:@selector(onClickView:)];
        }
    }
    return self;
}

-(void)onClickView:(TouchableView<ISelectable>*)sender{
    if(sender.tag != _lastSelectedTag){
        if(_lastSelectedTag>0){
            TouchableView<ISelectable>* v= (TouchableView<ISelectable>*)[_parentView viewWithTag:_lastSelectedTag];
            [v setSelected:NO];
        }
        TouchableView<ISelectable>* v=(TouchableView<ISelectable>*)[_parentView viewWithTag:sender.tag];
        [v setSelected:YES];
        _lastSelectedTag = sender.tag;
        if(self.delegate){
            [self.delegate onSelChanged:self selectedView:v index:[_parentView.subviews indexOfObject:v]];
        }
    }
    
    
}

-(void)setSelectTag:(int)tag{
    for(TouchableView<ISelectable>* v in _parentView.subviews){
        if(tag==v.tag){
            [self onClickView:v];
            break;
        }
    }

}


@end
