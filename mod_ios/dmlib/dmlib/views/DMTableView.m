
//
//  DMTableView.m
//  DMLib
//
//  Created by randy ren on 16/1/14.
//  Copyright © 2016年 damai. All rights reserved.
//

#import "DMTableView.h"
#import "RefreshableBase.h"
#import "DMCellDataSetter.h"
#import "DMViewUtil.h"
#import "DMMacro.h"
#import "UIView+ViewNamed.h"
#import "DMNetState.h"
#import "TableDataAdapter.h"
#import "DMHeaderView.h"

@interface DMTableView (){
    RefreshableBase* _refresh;
    DMCellDataSetter* _setter;
    __weak UIViewController* _currentController;
}

@end

@implementation DMTableView
/*
- (id)initWithCoder:(NSCoder *)aDecoder {
    self = [super initWithCoder:aDecoder];
    if (self) {
        self.delaysContentTouches = NO; // iterate over all the UITableView's subviews
        for (id view in self.subviews) { // looking for a UITableViewWrapperView
            if ([NSStringFromClass([view class]) isEqualToString:@"UITableViewWrapperView"]) { // this test is necessary for safety and because a "UITableViewWrapperView" is NOT a UIScrollView in iOS7
                if([view isKindOfClass:[UIScrollView class]]) { // turn OFF delaysContentTouches in the hidden subview
                    UIScrollView *scroll = (UIScrollView *) view;
                    scroll.delaysContentTouches = NO;
                }
                break;
            }
        }
    }
    return self;
}

- (BOOL)touchesShouldCancelInContentView:(UIView *)view {
    if ([view isKindOfClass:[UIButton class]]) { return YES; }
    return [super touchesShouldCancelInContentView:view];
}*/


-(void)controllerWillFinish:(UIViewController*)controller{
    [self deselectRowAtIndexPath:self.adapter.currentIndex animated:YES];
}


-(DMApiJob*)apiJob{
    return _refresh.apiJob;
}
-(DMDataAdapter*)adapter{
    return _refresh.adapter;
}
-(void)cancel{
    [_refresh cancel];
}

-(void)dealloc{
    _setter = nil;
    _refresh = nil;
}


-(__kindof UIView*)realHeaderView{
    return self.tableHeaderView.subviews[0];
}

-(void)awakeFromNib{
    [super awakeFromNib];
    _currentController = [self findViewController];
    if(_tableHeader){
        UIView* realHeader = [DMViewUtil createViewFormNibName:_tableHeader bundle:CREATE_BUNDLE_WHEN_NOT_NULL(_bundleName) owner:self];
        DMHeaderView* header = [[DMHeaderView alloc]initWithFrame:CGRectMake(0, 0, self.frame.size.width, realHeader.subviews[0].frame.size.height) contentView:realHeader];
        header.tableView =self;
        [self setTableHeaderView:header];
    }
    
#ifdef DEBUG
    if(!self.viewName){
        NSLog(@"TableView %@ has no name",self);
    }
#endif
    
    NSBundle* bundle = CREATE_BUNDLE_WHEN_NOT_NULL(_bundleName);
    
     //alloc]initWithCellName:self.viewName];
    TableDataAdapter* adapter;
    if(_space){
        adapter = [[TableDataAdapterWithFooter alloc]init];
        [((TableDataAdapterWithFooter*)adapter) setSpace:_space];
    }else{
       adapter= [[TableDataAdapter alloc]init];
    }
    [adapter setScrollView:self];
    
        if(_cellName){
        [adapter registerCell:_cellName bundle:bundle];
    }
    if(_segueName){
        [adapter setOnItemClickListener:self];
    }
    
    _refresh = [[RefreshableBase alloc]initWithScrollView:self adapter:adapter bundle:bundle];
    //这里复制参数
    [_refresh from:self];
    
    dispatch_async(dispatch_get_main_queue(), ^{
        _setter = [DMCellDataSetter create:_currentController cellName:self.viewName];// [[DMCellDataSetter
        [adapter setListener:_setter];
    });

}

-(void)setOnItemClickListener:(NSObject<IOnItemClickListener> *)listener{
 
    [_refresh.adapter setOnItemClickListener:listener];
}

-(void)setListener:(NSObject<IDataAdapterListener>*)listener{
    dispatch_async(dispatch_get_main_queue(), ^{
        [_setter setListener:listener];
    });
}

-(id)val{
    
    return _refresh.adapter.array;
}
-(void)setVal:(id)val{
    NSArray* arr = val;
    [_refresh endRefresh:arr.count>0];
    [_refresh.adapter setData:arr];
}
-(void)onItemClick:(UIView*)parent data:(NSObject*)data index:(NSInteger)index{
    BEGIN_TRY
    [_currentController performSegueWithIdentifier:_segueName sender:data];
    BEGIN_CATCH
    NSLog(@"Push fail:%@ , %@",exception.reason,exception.callStackSymbols);
    END_CATCH
    
}
-(void)layoutSubviews{
    [super layoutSubviews];
    if(_tableHeader){
        UIView* view= self.tableHeaderView;
        view.frame = CGRectMake(view.left, view.top, self.frame.size.width, view.height);

    }
    if(_tableHeader && !_maskHeader){
        //将表头挡住
        CGRect frame = self.frame;
        CGFloat hHeight = self.tableHeaderView.frame.size.height;
        [_refresh.netState layoutSubViews:CGRectMake(frame.origin.x, frame.origin.y+ hHeight, frame.size.width, frame.size.height-hHeight)];
    }else{
        [_refresh.netState layoutSubViews:self.frame];
    }
}

-(void)reloadWithStatus{
    [_refresh.netState onRefresh];
    [_refresh reload];
}

-(void)put:(NSString*)key value:(id)data{
    [_refresh put:key value:data];
}
-(void)execute{
    [_refresh execute];
}
-(void)reload{
    [_refresh reload];
}
@end
