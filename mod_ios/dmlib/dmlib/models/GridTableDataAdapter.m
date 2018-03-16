//
//  GridTableDataAdapter.m
//  ecard
//
//  Created by randy ren on 14-9-25.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "GridTableDataAdapter.h"
#import "DMMacro.h"
#import "GridTableViewCell.h"
#import "DMViewUtil.h"
#import "DMViewDecoder.h"


@interface GridTableDataAdapter()
{
   
    NSInteger _width;
   
    NSInteger _rows;
    NSInteger _total;
    
}
@end

@implementation GridTableDataAdapter


#pragma UITableViewDataSource

-(void)dealloc{
    _bundle = nil;
}

-(id)init{
    if(self=[super init]){
        _rows = 0;
    }
    return self;
}

-(void)setCols:(NSInteger)cols{
    _cols = cols;
    _width = [UIScreen mainScreen].bounds.size.width / cols;
    _total = _rows * _cols;
}

-(void)setData:(NSArray *)data{
    _rows = ceil((double)data.count/_cols);
    _total = _rows * _cols;
    [super setData:data];
}

-(void)addData:(NSArray *)data{
    _rows = ceil((double)(data.count+_data.count)/_cols);
    _total = _rows * _cols;
    [super addData:data];

}

-(void)removeItem:(id)item{
    [super removeItem:item];
    
    _rows = ceil((double)_data.count/_cols);
    _total = _rows * _cols;
}

-(void)registerCell:(NSString*)nibName bundle:(NSBundle*)bundle{
    UITableView* tableView = (UITableView*)_scrollView;
    [tableView setBackgroundColor:[UIColor clearColor]];
    [tableView registerClass:[GridTableViewCell class] forCellReuseIdentifier:@"Cell"];
    tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
    _cell = nibName;
    _bundle = bundle;
}



- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return _rows;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *cellIdentifier = @"Cell";
    NSInteger index = indexPath.row;
    GridTableViewCell *cell=[tableView dequeueReusableCellWithIdentifier:cellIdentifier];
    if(!cell.hasCreated){
        [cell createView:_cell bundle:_bundle  owner:self count:_cols];
        for(NSInteger i=0; i < _cols; ++i){
             UIView* view = cell.contentView.subviews[i];
            AddTapGestureRecognizer(view, onTapView:);
        }
    }
    for(NSInteger i=0; i < _cols; ++i){
        UIView* view = cell.contentView.subviews[i];
        NSInteger realIndex = index * _cols + i;
        view.tag = realIndex + 80000;
        if(realIndex > _data.count-1){
            //必须要隐藏
            view.hidden = YES;
        }else{
            view.hidden = NO;
             [_listener onInitializeView:_scrollView cell:view data:_data[realIndex] index:realIndex];
        }
    }
    return cell;
}

-(void)onTapView:(UITapGestureRecognizer*)sender{
    
    UIView* view = sender.view;
    NSInteger realIndex = view.tag - 80000;
    //这里需要
    [_onWeakItemClickListener onItemClick:view data:_data[realIndex] index:realIndex];
    
}
@end



////////////////////////////////
@interface GridTableDataAdapterWithSectionHeader()
{
    UIView* _sectionHeader;
    UIView* _tempCell;
    CGFloat _cellHeight;
}

@end



@implementation GridTableDataAdapterWithSectionHeader

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if(!_tempCell){
        _tempCell = [DMViewUtil createViewFormNibName:_cell bundle:_bundle];
        _cellHeight = [DMViewUtil measureViewHeight:_tempCell.subviews[0] width:SCREEN_WIDTH / _cols];
    }
    return _cellHeight;
}

-(void)dealloc{
    _sectionHeader = nil;
    _tempCell = nil;
}
-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section{
    return _sectionHeader.frame.size.height;
}
- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    return _sectionHeader;
}
-(void)setSectionHeader:(UIView*)header{
    _sectionHeader = header;
}
@end
