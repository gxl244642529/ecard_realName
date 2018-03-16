//
//  TabIndicator.m
//  ecard
//
//  Created by randy ren on 16/1/4.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "TabIndicator.h"

@interface TabIndicator()
{
    UIView* _view;
    NSInteger _items;
}

@end

@implementation TabIndicator

-(void)dealloc{
    _view = nil;
}

-(id)initWithFrame:(CGRect)frame{
    if(self = [super initWithFrame:frame]){
        
        _view  = [[UIView alloc]initWithFrame:CGRectMake(0, 0, 0, 0)];
        [self addSubview:_view];
        _view.backgroundColor = [UIColor redColor];
        _items  = 3;
    }
    return self;
}

-(void)setItems:(NSInteger)items{
    _items = items;
    _view.frame = CGRectMake(0, 0, self.frame.size.width/items, self.frame.size.height);
}

-(void)setCurrentItem:(NSInteger)index{
    _view.frame=CGRectMake(index*self.frame.size.width/_items, 0, self.frame.size.width/_items, self.frame.size.height);
}

-(void)setCurrentItem:(NSInteger)index animation:(BOOL)animation{
    [UIView animateWithDuration:0.3 animations:^{
         _view.frame=CGRectMake(index*self.frame.size.width/_items, 0, self.frame.size.width/_items, self.frame.size.height);
    }];
   
}
@end
