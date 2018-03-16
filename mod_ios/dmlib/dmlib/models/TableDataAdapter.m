//
//  TableDataAdapter.m
//  randycommonlibs
//
//  Created by randy ren on 14-9-21.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "TableDataAdapter.h"
static NSString *cellIdentifier = @"Cell";
@interface TableDataAdapter()
{

   
}

@end

@implementation TableDataAdapter

-(id)init{
    if(self = [super init]){
        
    }
    return self;
}



-(void)notifyDataChanged{
    UITableView* tableView = (UITableView*)_scrollView;
    if(!tableView.dataSource){
        tableView.delegate = self;
        tableView.dataSource = self;
    }
    
    [tableView reloadData];
}


-(void)setLineStyle:(UITableViewCellSeparatorStyle)style{
    UITableView* tableView = (UITableView*)_scrollView;
    tableView.separatorStyle = style;
}
-(void)registerCell:(NSString*)nibName  bundle:(NSBundle*)bundle{
    UITableView* tableView = (UITableView*)_scrollView;
    UINib* nib= [UINib nibWithNibName:nibName bundle:bundle];
    [tableView registerNib:nib forCellReuseIdentifier:cellIdentifier];
    tableView.separatorStyle = UITableViewCellSeparatorStyleSingleLine;
    tableView.tableFooterView = [[UIView alloc]init];
   
}
-(void)registerCell:(NSString*)cellName height:(NSInteger)height   bundle:(NSBundle*)bundle{
     UITableView* tableView = (UITableView*)_scrollView;
    tableView.rowHeight = height;
    [self registerCell:cellName bundle:bundle];
}
#pragma UITableViewDataSource

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return _data.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    NSInteger index = indexPath.row;
    UITableViewCell *cell=[tableView dequeueReusableCellWithIdentifier:cellIdentifier];
    [_listener onInitializeView:_scrollView cell:cell data:_data[index] index:index];
    
    return cell;
}



#pragma UITableViewDelegate
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    NSInteger index = indexPath.row;
     self.currentIndex = indexPath;
    [self didSelectItem:index];
}

-(void)tableView:(UITableView*)tableView  willDisplayCell:(UITableViewCell*)cell forRowAtIndexPath:(NSIndexPath*)indexPath
{
    [cell setBackgroundColor:_cellBackgroundColor];
}
@end



///////

@interface TableDataAdapterWithSectionHeader()
{
     UIView* _sectionHeader;
}

@end


@implementation TableDataAdapterWithSectionHeader

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section{
    return _sectionHeader;
}
-(void)setSectionHeader:(UIView*)header{
    _sectionHeader = header;
}
@end


////////////////////////////////




@interface TableDataAdapterWithFooter()
{
    CGFloat _height;
        UITableViewCell* _tempCell;
}

@end

@implementation TableDataAdapterWithFooter
//- (CGFloat)tableView:(UITableView )tableView estimatedHeightForRowAtIndexPath:(NSIndexPath )indexPath
//计算高度

-(void)registerCell:(NSString *)nibName bundle:(NSBundle *)bundle{
    [super registerCell:nibName bundle:bundle];
    UITableView* tableView = _scrollView;
     tableView.rowHeight = UITableViewAutomaticDimension;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    
    if(!_tempCell){
        _tempCell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier];
        [_tempCell layoutIfNeeded];
        [_tempCell updateConstraintsIfNeeded];
        _height = [_tempCell.contentView systemLayoutSizeFittingSize:UILayoutFittingCompressedSize].height + 1;
    }
    
    return _height;
    
}
-(void)dealloc{
    _tempCell = nil;
}
- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *cellIdentifier = @"Cell";
    NSInteger index = indexPath.section;
    UITableViewCell *cell=[tableView dequeueReusableCellWithIdentifier:cellIdentifier];
    [_listener onInitializeView:_scrollView cell:cell data:_data[index] index:index];
    
    return cell;
}
- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    NSInteger index = indexPath.section;
    [self didSelectItem:index];
}
- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section{
    return _space;
}
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return _data.count;
}
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return 1;
}
@end
