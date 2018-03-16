//
//  DataAdapter.m
//  randycommonlibs
//
//  Created by randy ren on 14-9-21.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "DMDataAdapter.h"
#import "OnItemClickListener.h"


@interface DMDataAdapter()
{
   
    NSObject<IOnItemClickListener>* _onItemClickListener;
}

@end

@implementation DMDataAdapter

-(id)init{
    if(self = [super init]){
        _cellBackgroundColor = [UIColor clearColor];
    }
    return  self;
}

-(void)setCellBackgroundColor:(UIColor*)color{
    _cellBackgroundColor = color;
}
-(void)dealloc{
    _data = NULL;
    _onItemClickListener = NULL;
    _cellBackgroundColor = nil;
    _currentIndex = nil;
}


-(void)registerCell:(NSString*)cellName height:(NSInteger)height  bundle:(NSBundle*)bundle{
    
}

-(void)insertItem:(id)data atIndex:(NSInteger)index{
    [_data insertObject:data atIndex:index];
}
-(void)removeItem:(id)item{
    [_data removeObject:item];
}
-(void)removeAtIndex:(NSInteger)index{
    [_data removeObjectAtIndex:index];
}
-(NSArray*)array{
    return _data;
}
-(NSInteger)getCount{
    return _data.count;
}
-(id)getItem:(NSInteger)index{
    return _data[index];
}
-(void)addData:(NSArray*)data{
    if(!_data){
        _data = [[NSMutableArray alloc]initWithArray:data];
    }else{
        [_data addObjectsFromArray:data];
    }
    [self notifyDataChanged];
}
-(void)setData:(NSArray*)data{
    _data = [[NSMutableArray alloc]initWithArray:data];
    [self notifyDataChanged];
}
-(void)setListener:(NSObject<IDataAdapterListener>*)listener{
    _listener = listener;
}
-(void)setScrollView:(UIScrollView*)scrollView{
    _scrollView = scrollView;
}
-(void)notifyDataChanged{
    
}
-(void)didSelectItem:(NSInteger)index{
    [_onWeakItemClickListener onItemClick:_scrollView data:_data[index] index:index];
}

-(void)setOnItemClickListener:(NSObject<IOnItemClickListener>*)listener{
    if( [listener isKindOfClass:[OnItemClickListener class]]){
        //保持引用
        _onItemClickListener = listener;
    }
    _onWeakItemClickListener = listener;
}
@end
