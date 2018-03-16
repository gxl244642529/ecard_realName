//
//  CartController.m
//  ecard
//
//  Created by randy ren on 15/3/31.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "CartController.h"
#import "SCartCell.h"
#import "LoadingView.h"
#import "SCartNoCardView.h"
#import <ecardlib/ecardlib.h>
#import "SCollectionController.h"
#import "AddToCartView.h"
#import "CardModel.h"
#import "LoadingErrorView.h"
@interface CartController ()
{
    LoadingView* _loadingView;
    SCartNoCardView* _noCartView;
    ObjectJsonTask* _deleteTask;
    ObjectJsonTask* _updateTask;
    LoadingErrorView* _loadingError;
    __weak CartVo* _updateCartVo;
}
@property (weak, nonatomic) IBOutlet ItemView *btnDel;
@property (weak, nonatomic) IBOutlet UILabel *txtPrice;
@property (weak, nonatomic) IBOutlet PullToRefreshTableView *tableView;
@end

@implementation CartController


-(void)dealloc{
    _loadingError = nil;
    _updateTask = nil;
    _noCartView = nil;
    _loadingView = nil;
    _deleteTask = nil;
}

-(void)updateViewConstraints{
    [super updateViewConstraints];
    if(_loadingView){
        _loadingView.frame = ConentViewFrame;
    }
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"购物车"];
    [_btnDel setTarget:self withAction:@selector(onDel:)];
   
    [_tableView setEnableState:NO];
    [_tableView registerCell:@"SCartCell"];
    [_tableView setOnItemClickListener:self];
    [_tableView setDataListener:self];
    
    [[CartModel sharedInstance]createListTask];
    [_tableView setTask:[CartModel sharedInstance].listTask];
    
    [[CartModel sharedInstance]getList:self];
    
    
    _loadingView = [[LoadingView alloc]initWithFrame:ConentViewFrame];
    [self.view addSubview:_loadingView];
    
    [self updateTotalPrice];
}


-(void)onItemClick:(UIView*)parent data:(NSObject*)data index:(NSInteger)index{
    
    
    
}

-(void)onUpdateCart:(AddToCartView*)sender{
    
    //修改
    //调用接口
    if(!_updateTask){
        _updateTask = [[ECardTaskManager sharedInstance]createObjectJsonTask:@"s_cart_update" cachePolicy:DMCachePolicy_NoCache];
        [_updateTask setWaitMessage:@"正在修改..."];
        __weak PullToRefreshTableView* __tableView = _tableView;
        _updateTask.successListener = ^(id result){
            [SVProgressHUD dismiss];
            [__tableView.task clearCache];
          [__tableView onLoadingRefresh:nil];
        };
        _updateTask.errorListener = ^(NSString* err,BOOL isNet){
            [SVProgressHUD showErrorWithStatus:err];
        };
    }
    
    [_updateTask put:@"id" value:_updateCartVo.cartID];
    [_updateTask put:@"recharge" value:[NSNumber numberWithInteger:[sender getRecharge]]];
    [_updateTask put:@"count" value:[NSNumber numberWithInteger:[sender getCount]]];
    [_updateTask execute];

}


-(void)onChangeCart:(UIView*)sender{
    CartVo* data = [CartModel sharedInstance].list[sender.tag];
    _updateCartVo = data;
    [AddToCartView show:self count:data.count recharge:data.recharge target:self selector:@selector(onUpdateCart:) title:@"修改" data:data store:data.store];
}

-(void)onInitializeView:(UIView*)parent cell:(SCartCell*)cell data:(CartVo*)data index:(NSInteger)index{
    cell.btnChange.tag = index;
    [cell.btnChange setTarget:self withAction:@selector(onChangeCart:)];
    [cell setData:data];
    [cell setDelegate:self];
}
-(void)task:(id)task result:(NSArray*)result position:(NSInteger)position isLastPage:(BOOL)isLastPage{
    [_loadingView removeFromSuperview];
    _loadingView = nil;
    if(position == Start_Position && result.count == 0){
       _noCartView= [ViewUtil createViewFormNibName:@"SCartNoCardView" owner:self];
        [self.view addSubview:_noCartView];
        [_noCartView.btnCart setTarget:self withAction:@selector(onGotoBuy:)];
        [_noCartView.btnCollect setTarget:self withAction:@selector(onViewCollection:)];
        _noCartView.frame = ConentViewFrame;
    }
    [_tableView task:task result:result position:position isLastPage:isLastPage];
    [self updateTotalPrice];
}


-(void)onGotoBuy:(id)sender{
    
    [self.navigationController popToViewController:_shopController animated:YES];
    
}

-(void)onViewCollection:(id)sender{
    [self.navigationController pushViewController:[[SCollectionController alloc]init] animated:YES];
}


-(void)onRefresh:(id)sender{
    [_loadingError removeFromSuperview];
    _loadingError = nil;
    
    _loadingView = [[LoadingView alloc]initWithFrame:ConentViewFrame];
    [self.view addSubview:_loadingView];
    
    [[CartModel sharedInstance].listTask reload];
}

-(void)task:(id)task error:(NSString *)errorMessage isNetworkError:(BOOL)isNetworkError{
    _loadingError = [[LoadingErrorView alloc]initWithFrame:ConentViewFrame];
    [self.view addSubview:_loadingError];
    [_loadingError setTarget:self withAction:@selector(onRefresh:)];
    if(_loadingView){
        [_loadingView removeFromSuperview];
        _loadingView = nil;
    }
}

//获取选中的数组
-(NSArray*)getSelectedArray{
    NSMutableArray* arr = [[NSMutableArray alloc]init];
    for (CartVo* data in _tableView.adapter.array) {
        if(data.selected){
            [arr addObject:data];
        }
    }

    return arr;
}

-(void)onDel:(id)sender{
    
    NSMutableArray* arr = [[NSMutableArray alloc]init];
    for (CartVo* data in _tableView.adapter.array) {
        if(data.selected){
            [arr addObject:data.cartID];
        }
    }

    
    if(arr.count>0){
        //调用接口
        if(!_deleteTask){
            _deleteTask = [[ECardTaskManager sharedInstance]createObjectJsonTask:@"s_cart_del" cachePolicy:DMCachePolicy_NoCache];
            [_deleteTask setWaitMessage:@"正在删除..."];
            __weak PullToRefreshTableView* __tableView = _tableView;
            _deleteTask.successListener = ^(id result){
                [SVProgressHUD dismiss];
                [__tableView.task clearCache];
              [__tableView onLoadingRefresh:nil];
            };
            _deleteTask.errorListener = ^(NSString* err,BOOL isNet){
                [SVProgressHUD showErrorWithStatus:err];
            };
        }
        
        
        [_deleteTask put:@"list" value:arr];
        [_deleteTask execute];
    }else{
        //
        [SVProgressHUD showErrorWithStatus:@"请选择要删除的卡片"];
        
    }

}

-(void)updateTotalPrice{
    float total = 0;
    for (CartVo* data in _tableView.adapter.array) {
        if(data.selected){
            total += data.totalPrice;
        }
    }
    
    _txtPrice.text = [NSString stringWithFormat:@"%.2f",total];
    
    
}

//选择状态改变
-(void)onSelectChange:(CartVo*)data{
    [self updateTotalPrice];
}

//需要充值
-(void)onRequestRechage:(CartVo*)data current:(SCartCell*)cell{
    
    
    
}
- (IBAction)onSubmit:(id)sender {
    
    //下单
    NSArray* arr = [self getSelectedArray];
    
    if(arr.count>0){
        //调用接口
        
        [[SellingModel sharedInstance]onBuyFromCart:self data:arr];
        
    }else{
        //
        [SVProgressHUD showErrorWithStatus:@"请选择要购买的卡片"];
        
    }

    
    
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
