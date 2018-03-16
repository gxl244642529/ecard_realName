//
//  TabItemGroup.m
//  ecard
//
//  Created by randy ren on 15-1-14.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "TabItemGroup.h"
#import "ISelectable.h"
#import "TouchableImageView.h"
@interface TabItemGroup()
{

    NSArray* _items;
    __weak NSObject<TabItemGroupDelegate>* _delegate;
    __weak TouchableImageView<ISelectable>* _lastItem;
}
@end

@implementation TabItemGroup
-(void)dealloc{
    _items=NULL;
}
-(id)initWithTabItems:(NSArray*)items{
    
    if(self=[super init]){
        NSInteger index=1000;
        for (TouchableImageView<ISelectable>* view in items) {
            [view setTag:index++];
            [view setTarget:self withAction:@selector(onClickItem:)];
        }
        _items = items;
    }
    
    return self;
    
}

-(void)setDelegate:(NSObject<TabItemGroupDelegate>*)delegate{
     _delegate = delegate;
}
-(void)setHidden:(BOOL)hidden{
    for (TouchableImageView<ISelectable>* view in _items) {
        view.hidden = hidden;
    }

}
-(void)setCurrentItem:(NSInteger)index{
    if(index<0)index=0;
    if(index>_items.count-1)index=_items.count-1;
    [self onClickItem:_items[index]];
}

-(void)onClickItem:(TouchableImageView<ISelectable>*)view{
    
        if(![_delegate tableGroup:self didClickIndex:view.tag-1000]){
            if(_lastItem != view){
            if(_lastItem){
                [_lastItem setSelected:NO];
            }
            [_delegate tableGroup:self didSelectIndex:view.tag-1000];
            [view setSelected:YES];
            _lastItem = view;
            }
        }
}

@end
