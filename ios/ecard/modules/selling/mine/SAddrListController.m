//
//  SAddrListController.m
//  ecard
//
//  Created by randy ren on 15-1-8.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "SAddrListController.h"
#import "SEditAddrController.h"
#import "SAddrCell.h"
#import "SellingModel.h"
#import "SEditAddrController.h"

#import "SAddrManager.h"

#define BOTTOM_HEIGHT 50




@interface SAddrListController ()
{
    PullToRefreshTableView* _tableView;
    NSInteger deleteIndex;
}
@end

@implementation SAddrListController
-(void)dealloc{
    _tableView = NULL;
    [SVProgressHUD dismiss];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self setTitle:@"收货地址管理"];
    
    CGRect rect = self.view.bounds;
    
    _tableView = [[PullToRefreshTableView alloc]initWithFrame:rect];
    [self.view addSubview:_tableView];
    _tableView.rowHeight = 100;
    [_tableView registerCell:@"SAddrCell"];
    [_tableView setOnItemClickListener:self];
    [_tableView setPullToRefreshListener:self];
    [_tableView setEnableState:YES];
    UIButton* button = [self createTitleButton:@"新增"];
    Control_AddTarget(button, onAddAddr);
    [self onLoadData:Start_Position];
    
}

-(void)onLoadData:(NSInteger)position{
     [[SAddrManager sharedInstance]getList:_tableView controller:self];
}

-(void)onItemClick:(UIView*)parent data:(NSObject*)data index:(NSInteger)index{

    if([self isModal]){
         [self setResult:RESULT_OK data:data];
    }else{
        //编辑
        SEditAddrController* controller = [[SEditAddrController alloc]init];
        controller.mode = AddrEditMode_Edit;
        [self openControllerForResult:controller requestCode:REQUEST_EDIT data:data modal:NO];
    }
}
-(void)onControllerResult:(NSInteger)resultCode requestCode:(NSInteger)requestCode data:(NSObject *)data{
    if(RESULT_OK == resultCode){
        /*
        if(requestCode == REQUEST_ADD){
            
            [_tableView.adapter insertItem:data atIndex:0];
            [_tableView reloadData];
            
        }else if(requestCode == REQUEST_EDIT){
            [_tableView reloadData];
        }
        */
         [[SAddrManager sharedInstance]getList:_tableView  controller:self];
        
    }
}


ON_EVENT(onAddAddr){
    
    SEditAddrController* controller = [[SEditAddrController alloc]init];
    [self openControllerForResult:controller requestCode:REQUEST_ADD data:NULL modal:NO];
    
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)onInitializeView:(UIView*)parent cell:(SAddrCell*)cell data:(SAddrListVo*)data index:(NSInteger)index{
    
    cell.strName.text = data.name;
    cell.phone.text = data.phone;
    cell.addr.text = [data getDetalAddr];
    if(data.def){
        cell.addr.textColor = [UIColor redColor];
    }else{
        cell.addr.textColor = [UIColor darkGrayColor];
    }
    cell.btnEdit.tag = index;
    [cell.btnEdit setTarget:self withAction:@selector(onEdit:)];
    [cell.btnDelete setTag:index];
    [cell.btnDelete setTarget:self withAction:@selector(onDelete:)];
}


ON_VIEW_EVENT(onEdit){
    //编辑
    SAddrListVo* data = [_tableView.adapter getItem:sender.tag];
    SEditAddrController* controller = [[SEditAddrController alloc]init];
    controller.mode = AddrEditMode_Edit;
    [self openControllerForResult:controller requestCode:REQUEST_EDIT data:data modal:NO];
}

ON_VIEW_EVENT(onDelete){
    [Alert confirm:@"是否要删除此地址?" title:@"删除确认" delegate:self];
    deleteIndex = sender.tag;
}

// Called when a button is clicked. The view will be automatically dismissed after this call returns
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    if(buttonIndex==1){
        NSInteger index =deleteIndex;
        SAddrListVo* data = [_tableView.adapter getItem:index];
        [SVProgressHUD showWithStatus:@"正在删除..."];
        [[SAddrManager sharedInstance]deleteData:data listener:self];
    }
}
-(void)task:(NSObject<IJsonTask> *)task result:(id)result{
    [SVProgressHUD showSuccessWithStatus:@"删除成功"];
    //删除对应的
    [_tableView.adapter removeAtIndex:deleteIndex];
    [_tableView reloadData];
    //[[SAddrManager sharedInstance]getList:_tableView controller:self];
}
-(void)task:(NSObject<IJsonTask> *)task error:(NSString *)errorMessage isNetworkError:(BOOL)isNetworkError{
    [SVProgressHUD showErrorWithStatus:errorMessage];
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
