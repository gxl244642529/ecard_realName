//
//  StateList.m
//  libs
//
//  Created by randy ren on 16/1/9.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMStateList.h"
#import "DMServers.h"
#import "DMDataAdapter.h"
#import "TableDataAdapter.h"
#import "DMCollectionDataAdapter.h"
#import "MJRefresh.h"
#import "OnItemClickOpenCtroller.h"
#import "DMJobManager.h"
#import "OnItemClickOpenCtroller.h"
#import "DMCellDataSetter.h"
#import "DMPage.h"
#import "RefreshableBase.h"
#import "DMCollectionDataAdapter.h"
#import "GridTableDataAdapter.h"
#import "DMViewUtil.h"
#import "DMMacro.h"
#import "DMHeaderView.h"
#import "DMViewDecoder.h"
#import "UIView+ViewNamed.h"

@interface DMStateList()
{
    __kindof UIScrollView* _scrollView;
    __weak DMDataAdapter* _adapter;
    DMCellDataSetter* _setter;
    RefreshableBase* _refresh;
   // DMNetState* _state;
}

@end

@implementation DMStateList

-(void)dealloc{
  //  _state = nil;
    _scrollView = nil;
    _setter = nil;
    _refresh = nil;
    _loadingView = nil;
    _networkErrorView = nil;
    _noResultView = nil;
    
    _type = nil;
    _api = nil;
    _entityName = nil;
    
    _cellName = nil;
    _controllerName = nil;

}
-(void)reloadWithState{
   // [_state onRefresh];
    
    [_refresh reloadWithState];
    
}
-(void)awakeFromNib{
    [super awakeFromNib];
   
    NSBundle* bundle = CREATE_BUNDLE_WHEN_NOT_NULL(_bundleName);
    
    if([_type  isEqual: @"t"]){
        _scrollView = [[UITableView alloc]initWithFrame:self.bounds];
        TableDataAdapter* adapter = [[TableDataAdapter alloc]init];
        ((UITableView*)_scrollView).rowHeight = _rowHeight;
        [adapter setScrollView:_scrollView];
        _refresh = [[RefreshableBase alloc]initWithScrollView:_scrollView adapter:adapter bundle:bundle];
        [adapter registerCell:_cellName bundle:bundle];
    }else if([_type  isEqual: @"c"]){
        _scrollView = [[UICollectionView alloc]initWithFrame:self.bounds];
        _refresh = [[RefreshableBase alloc]initWithScrollView:_scrollView adapter:[[DMCollectionDataAdapter alloc]init] bundle:bundle];
    }else{
        _scrollView = [[UITableView alloc]initWithFrame:self.bounds];
        GridTableDataAdapter* adapter;
        if(_sectionHeader){
            UIView* sectionHeaderView = [DMViewUtil  createViewFormNibName:_sectionHeader bundle: bundle];
            GridTableDataAdapterWithSectionHeader* headerAdapter = [[GridTableDataAdapterWithSectionHeader alloc]init];
            [headerAdapter setSectionHeader:sectionHeaderView];
            adapter = headerAdapter;
        }else{
            adapter= [[GridTableDataAdapter alloc]init];
        }
        
        [adapter setCols:_cols];
        if(_rowHeight>0){
            ((UITableView*)_scrollView).rowHeight = _rowHeight;
        }
       
        [adapter setScrollView:_scrollView];
        _refresh = [[RefreshableBase alloc]initWithScrollView:_scrollView adapter:adapter bundle:bundle];
        [adapter registerCell:_cellName bundle:bundle];
    }
    
    
    if(_tableHeader){
        if([_scrollView isKindOfClass:[UITableView class]]){
            UIView* realHeader = [DMViewUtil createViewFormNibName:_tableHeader bundle:bundle owner:self];
            DMHeaderView* header = [[DMHeaderView alloc]initWithFrame:CGRectMake(0, 0, self.frame.size.width,[DMViewUtil measureViewHeight: realHeader.subviews[0]]) contentView:realHeader];
            header.tableView =_scrollView;
            [(UITableView*)_scrollView setTableHeaderView:header];
        }
    }
    
    [self addSubview:_scrollView];
 //   _state = [[DMNetState alloc]initWithView:_scrollView bundle:bundle];
    //这里复制参数
    [_refresh from:self];
    _adapter = _refresh.adapter;
    
    _setter = [DMCellDataSetter createWithView:self cellName:_cellName];
    
    [_adapter setListener:_setter];

}


-(__kindof UIView*)tableHeaderView{
    return ((UITableView*)_scrollView).tableHeaderView.subviews[0];
}

-(void)put:(NSString*)key value:(id)value{
    [_refresh put:key value:value];
}
-(void)execute{
    [_refresh execute];
}


-(void)layoutSubviews{
    [super layoutSubviews];
    if(_tableHeader && !_maskHeader){
        //将表头挡住
        CGRect frame = self.bounds;
        UITableView* table = _scrollView;
        CGFloat hHeight = table.tableHeaderView.frame.size.height;
        [_refresh.netState layoutSubViews:CGRectMake(frame.origin.x, frame.origin.y+ hHeight, frame.size.width, frame.size.height-hHeight)];
    }else{
        [_refresh.netState layoutSubViews:self.bounds];
    }
    _scrollView.frame = self.bounds;
}

-(void)deselectRowAtIndexPath:(NSIndexPath*)indexPath animated:(BOOL)animated{
    [((UITableView*)_scrollView)deselectRowAtIndexPath:indexPath animated:animated];
}

-(void)setDataListener:(NSObject<IDataAdapterListener>*)listener{
    [_adapter setListener:listener];
}
-(void)setCellSelectionStyle:(UITableViewCellSeparatorStyle)stype{
    [((UITableView*)_scrollView)setSeparatorStyle:stype];
}
-(void)registerCell:(NSString*)nibName rowHeight:(NSInteger)rowHeight  bundle:(NSBundle*)bundle{
     ((UITableView*)_scrollView).rowHeight = rowHeight;
    [((TableDataAdapter*)_adapter) registerCell:nibName bundle:CREATE_BUNDLE_WHEN_NOT_NULL(_bundleName)];
}
-(void)setOnItemClickListener:(NSObject<IOnItemClickListener>*)listener{
    [_adapter setOnItemClickListener:listener];
}
-(void)setCellBackgroundColor:(UIColor*)color{
    [_adapter setCellBackgroundColor:color];
}
-(instancetype)initWithFrame:(CGRect)frame type:(StateListType)type{
    if(self = [super initWithFrame:frame]){
        frame = CGRectMake(0, 0, frame.size.width, frame.size.height);
        switch (type) {
            case StateListType_TableView:
            {
                _scrollView = [[UITableView alloc]initWithFrame:frame];
                
            }
                break;
            case StateListType_CollectionView:
            {
                _scrollView = [[UICollectionView alloc]initWithFrame:frame];
            }
                break;
            default:
            {
                _scrollView = [[UITableView alloc]initWithFrame:frame];
            }
                break;
        }
        [self addSubview:_scrollView];
    }
    return self;
}


@end
