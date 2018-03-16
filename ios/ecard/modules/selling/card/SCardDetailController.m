//
//  SCardDetailController.m
//  ecard
//
//  Created by randy ren on 15/3/29.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//


#import <ecardlib/ecardlib.h>

#import "SCardDetailController.h"
#import "SellingModel.h"
#import "AddToCartView.h"

#import "CardModel.h"
#import "CartTitle.h"
#import "WebViewController.h"
#import "AppDelegate.h"
#import "NetworkImage.h"
#import "ShareModel.h"


@interface SCardDetailController ()
{
    ObjectJsonTask* _task;
    ObjectJsonTask* _favTask;
    //详情信息
    NSDictionary* _loadedData;
    
    __weak IBOutlet ItemView *_share;
    ShareModel* _shareModel;
    NSInteger _store;
    
    NSArray* _images;
}
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *cardHeight;
@property (weak, nonatomic) IBOutlet ItemView *btnDetail;
@property (weak, nonatomic) IBOutlet UILabel *txtInfo;
@property (weak, nonatomic) IBOutlet ScrollCtrl *scrollCtrl;
@property (weak, nonatomic) IBOutlet ViewPager *viewPager;
@property (weak, nonatomic) IBOutlet UILabel *txtPrice;
@property (weak, nonatomic) IBOutlet UILabel *txtCount;
@property (weak, nonatomic) IBOutlet UIButton *btnBuy;
@property (weak, nonatomic) IBOutlet UILabel *txtTitle;
@property (weak, nonatomic) IBOutlet ItemView *btnCart;
@property (weak, nonatomic) IBOutlet ItemView *btnCiollect;
@property (weak, nonatomic) IBOutlet UIActivityIndicatorView *_inder;
@property (weak, nonatomic) IBOutlet UIImageView *_favIconOff;

@property (weak, nonatomic) IBOutlet UIScrollView *scrollView;


@end

@implementation SCardDetailController
-(void)dealloc{
    _images = nil;
    _favTask = nil;
    _shareModel = nil;
    _loadedData = nil;
    _task = nil;
    
    [SVProgressHUD dismiss];
}
-(id)init{
    if(self=[super initWithNibName:@"SCardDetailController" bundle:nil]){
        
    }
    return self;
}

-(void)updateViewConstraints{
    [super updateViewConstraints];
    
    _cardHeight.constant = ([UIScreen mainScreen].bounds.size.width - 20 ) * CARD_HEIGHT / CARD_WIDTH;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    dispatch_async(dispatch_get_main_queue(), ^{
        
        [self setTitle:@"售卡详情"];
        
        
        
        [_share setTarget:self withAction:@selector(onShare:)];
        _shareModel = [[ShareModel alloc]init];
        
        
        
        SCardListVo* data = self.data;
        _task = [[ECardTaskManager sharedInstance]createObjectJsonTask:@"s_card_detail2" cachePolicy:DMCachePolicy_TimeLimit];
        
        [_task setDataID:[NSNumber numberWithInteger:data.ID]];
        [_task setListener:self];
        [_task execute];
        
        
        
        _txtTitle.text = data.title;
        _txtPrice.text = [data priceString];
        
        
        
        Control_AddTarget(_btnBuy, onBuy);
        [_btnCart setLoginTarget:self withAction:@selector(onBtnCart:)];
        [_btnCiollect setLoginTarget:self withAction:@selector(onCollect:)];
        [_btnDetail setTarget:self withAction:@selector(onDetail:)];
        
        
        self._inder.hidden = YES;
        [_btnBuy setEnabled:NO];
        [CartTitle createWidthViewController:self];
        __weak ObjectJsonTask* __task = _task;
     /*   [_scrollView addHeaderWithCallback:^{
            [__task reload];
        }];
*/
    });
}

-(void)onShare:(id)sender{
    
    [_shareModel share:@"在e通卡APP买卡款式多，还包邮！你也来试试" view:self.view];
    
}


ON_EVENT(onCollect){
    if(!_favTask){
        _favTask = [[ECardTaskManager sharedInstance]createObjectJsonTask:@"s_fav_add3" cachePolicy:DMCachePolicy_NoCache];
        __weak SCardDetailController* __self = self;
        SCardListVo* data = self.data;
        [_favTask put:@"id" value:[NSNumber numberWithInteger:data.ID]];
        _favTask.successListener=^(id result){
            __self._favIconOff.hidden = NO;
            __self._inder.hidden = YES;
            [SVProgressHUD showSuccessWithStatus:@"收藏成功"];
        };
        _favTask.errorListener = ^(NSString* err,BOOL isNet){
            __self._favIconOff.hidden = NO;
            __self._inder.hidden = YES;
            [SVProgressHUD showSuccessWithStatus:err];
        };
        
    }
    [self._inder startAnimating];
    self._inder.hidden = NO;
    self._favIconOff.hidden = YES;
    [_favTask execute];

    
    
}

-(void)onDetail:(id)sender{
    SCardListVo* data = self.data;
  
  
  DMWebViewController* c = [[DMWebViewController alloc]initWithTitle:@"卡片详情" url:
                            [NSString stringWithFormat:@"%@/index.php/api2/s_card_info2/index/%ld",
                             [DMConfigReader getString:@"servers" subkey:@"php"],data.ID]];
  
  [self.navigationController pushViewController:c animated:YES];
  
    
}


-(void)onBuy:(id)sender{
     [[ECardTaskManager sharedInstance]checkLogin:self loginSuccess:@selector(onLoginBuy:)];
}

-(void)onLoginBuy:(id)sender{
    [AddToCartView show:self count:0 recharge:0 target:self selector:@selector(onBuyDirect:) title:@"购买" data:self.data store: _store ];
}

-(void)onBuyDirect:(AddToCartView*)sender{
    
   
    [[SellingModel sharedInstance]onBuyDirect:self data:self.data count:[sender getCount] recharget:[sender getRecharge]];
}


-(void)onAddToCart:(AddToCartView*)sender{
    
    [[CartModel sharedInstance]addToCart:self.data count:[sender getCount] recharge:[sender getRecharge] successListener:^{
        
    }];
}

-(void)onBtnCart:(id)sender{
    [AddToCartView show:self count:0 recharge:0 target:self selector:@selector(onAddToCart:) title:@"购买" data:self.data store:_store];
}


/**
 视图数量
 */
-(NSInteger)getPageCount{
    return _images.count;
}
/**
 创建视图
 */
-(UIView*)instantiateItem:(NSInteger)index parent:(UIView*)parent frame:(CGRect)frame{
    NetworkImage* imageView = [[NetworkImage alloc]initWithFrame:frame];
    imageView.backgroundColor = [[UIColor lightGrayColor]colorWithAlphaComponent:0.3];
    imageView.layer.cornerRadius = 8;
    imageView.layer.masksToBounds = YES;
    id image = _images[index];
    [imageView load:image];
    
    
    return imageView;
}

-(void)viewPager:(UIView *)view didSelectedIndex:(NSInteger)index{
    [_scrollCtrl setCurrentItem:index];
}

-(void)task:(id)task result:(id)result{
    
    [_btnBuy setEnabled:YES];
  //  [_scrollView headerEndRefreshing];
    
    _loadedData = result;
    _txtCount.text = [NSString stringWithFormat:@"%@人购买",[result valueForKey:@"SALED"]];
    _store = [[result valueForKey:@"STOCK"]integerValue];
    _images = result[@"list"];
    [_scrollCtrl setItems:_images.count];
    [_scrollCtrl setCurrentItem:0];
    [_viewPager setDataSource:self];
    _txtInfo.numberOfLines = 0;
    _txtInfo.text = result[@"TIP"];
    [_txtInfo sizeToFit];
    
    [self updateViewConstraints];
}

-(void)task:(id)task error:(NSString *)errorMessage isNetworkError:(BOOL)isNetworkError{
  //  [_scrollView headerEndRefreshing];

    [SVProgressHUD showErrorWithStatus:errorMessage];
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
