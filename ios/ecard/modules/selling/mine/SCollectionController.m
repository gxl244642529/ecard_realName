//
//  SCollectionController.m
//  ecard
//
//  Created by randy ren on 15-1-8.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "SCollectionController.h"
#import "SellingModel.h"
#import "SaleUtil.h"
#import "OnClickListenerExt.h"
#import "SCardDetailController.h"
#import "JsonTaskManager.h"
#import "SCardCell.h"
#import "SCardFavCell.h"



@interface SCollectionController ()
{
    ArrayJsonTask* _task;
    ObjectJsonTask* _delTask;
    BOOL _editMode;
    __weak UIButton* _editButton;
    PullToRefreshGridTableView* _tableView;
    
}
@end

@implementation SCollectionController

-(void)dealloc{
    _tableView = nil;
    _task = NULL;
    _delTask = nil;
}
- (void)viewDidLoad
{
    [super viewDidLoad];
    [self setTitle:@"我的收藏"];
  self.view.backgroundColor =RGB_WHITE(ff);
    _task = [[JsonTaskManager sharedInstance]createArrayJsonTask:@"s_fav_list3" cachePolicy:DMCachePolicy_NoCache isPrivate:YES];
    [_task setClass:[SCardListVo class]];
    
    _tableView = [[PullToRefreshGridTableView alloc]initWithFrame:self.view.bounds];
    [self.view addSubview:_tableView];
    [_tableView setEnableState:YES];
    [_tableView setCols:2];
    [_tableView setDataListener:self];
    [_tableView setOnItemClickListener:[OnClickListenerEx parent:self clsRef:[SCardDetailController class]]];
    _tableView.rowHeight = 127 +  (87 * [UIScreen mainScreen].bounds.size.width / 320) - 87;
    [_tableView registerCell:@"SCardFavCell"];
    
    [_tableView setTask:_task];
    [_task execute];
 
    
    _editMode = NO;
    UIButton* editButton = [self createTitleButton:@"编辑"];
    Control_AddTarget(editButton, onEdit);
    _editButton = editButton;
}


-(void)onLoadingRefresh:(id)sender{
    [_task reload];
}

-(void)onEdit:(id)sender{

    _editMode = !_editMode;
    [_tableView reloadData];
    
    if(_editMode){
        [_editButton setTitle:@"完成" forState:UIControlStateNormal];
    }else{
        [_editButton setTitle:@"编辑" forState:UIControlStateNormal];
    }
}

-(void)onInitializeView:(UIView*)parent cell:(SCardFavCell*)cell data:(SCardListVo*)data index:(NSInteger)index{
    if(_editMode){
        cell.close.hidden = NO;
    }else{
        cell.close.hidden = YES;
    }
    cell.close.tag = index;
    Control_AddTarget(cell.close, onDel);
    [JsonTaskManager setImageSrcDirect:cell.image src:data.thumb];
    cell.price.text = [data priceString];
}

-(void)onDel:(UIView*)view{
    SCardListVo* data = [_tableView.adapter getItem:view.tag];
    if(!_delTask){
        _delTask = [[ECardTaskManager sharedInstance]createObjectJsonTask:@"s_fav_del3" cachePolicy:DMCachePolicy_NoCache];
    }
    [_delTask put:@"id" value:[NSNumber numberWithInteger:data.ID]];
    [_delTask setWaitMessage:@"正在删除"];
    __weak PullToRefreshGridTableView* __tableView =_tableView;
    __weak ArrayJsonTask* __task = _task;
    _delTask.successListener=^(id result){
        
        [__tableView.adapter removeItem:data];
        [__tableView reloadData];
        [__task clearCache];
        [SVProgressHUD dismiss];
        
        if([__tableView.adapter getCount]==0){
            [__tableView onNoResult];
        }
        
    };
    _delTask.errorListener = ^(NSString* err,BOOL isNet){
        [SVProgressHUD showErrorWithStatus:err];
    };
    [_delTask execute];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
