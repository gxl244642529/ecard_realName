//
//  FixListView.m
//  ecard
//
//  Created by randy ren on 15-1-24.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "FixListView.h"
#import <ecardlib/ecardlib.h>
@interface FixListView()
{
    __weak NSObject<IDataAdapterListener>* _listener;
    NSString* _nibName;
}

@end

@implementation FixListView


-(void)dealloc{
    _nibName = NULL;
}

-(void)setListener:(NSObject<IDataAdapterListener>*)listener{
    _listener =listener;
}

-(void)registerNib:(NSString*)nibName{
    _nibName  =nibName;
}

-(void)setData:(NSArray*)data{
    NSInteger count = data.count;
    NSArray* arr = self.subviews;
    NSInteger sCount = arr.count;
    for(NSInteger i=0; i < count ; ++i){
        UIView* v;
        if(i < sCount){
            v = arr[i];
        }else{
            v = [ViewUtil createViewFormNibName:_nibName owner:self];
            [self addSubview:v];
        }
        [_listener onInitializeView:self cell:v data:data[i] index:i];
    }
    for(NSInteger i = count; i < sCount; ++i){
        [arr[i] removeFromSuperview];
    }
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
