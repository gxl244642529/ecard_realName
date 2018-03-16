//
//  DataAdapter.h
//  randycommonlibs
//
//  Created by randy ren on 14-9-21.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "IOnItemClickListener.h"
#import "IDataAdapterListener.h"


/**
 
 tableView
 collectionView
 //其他
 
 detailview
 
 //相同点:
 上拉下拉
 loading状态
 
 */


@interface DMDataAdapter<DataType> : NSObject
{
    __weak NSObject<IOnItemClickListener>* _onWeakItemClickListener;
     NSMutableArray* _data;
    __weak NSObject<IDataAdapterListener>* _listener;
    __weak __kindof UIScrollView* _scrollView;
    UIColor* _cellBackgroundColor;
}

-(NSInteger)getCount;
-(DataType)getItem:(NSInteger)index;
-(void)removeAtIndex:(NSInteger)index;
-(void)setScrollView:(UIScrollView*)scrollView;
-(void)setListener:(NSObject<IDataAdapterListener>*)listener;
-(void)addData:(NSArray*)data;
-(void)setData:(NSArray*)data;
-(void)setOnItemClickListener:(NSObject<IOnItemClickListener>*)listener;
-(void)removeItem:(id)item;
-(NSArray*)array;
-(void)insertItem:(id)data atIndex:(NSInteger)index;

-(void)registerCell:(NSString*)cellName  height:(NSInteger)height bundle:(NSBundle*)bundle;

/**
 需要重载
 */
-(void)notifyDataChanged;
-(void)didSelectItem:(NSInteger)index;
-(void)setCellBackgroundColor:(UIColor*)color;

@property (nonatomic,retain) NSIndexPath* currentIndex;


@end
