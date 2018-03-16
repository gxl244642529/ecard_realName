//
//  SellingMainController.m
//  ecard
//
//  Created by randy ren on 15/3/28.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "SellingMainController.h"
#import <ecardlib/ecardlib.h>
#import "MenuGroup.h"
#import "ECardTaskManager.h"
#import "SellingModel.h"
#import "MenuUtil.h"
#import "PullToRefreshCollectionView.h"
#import "SaleUtil.h"
#import "SCardCell.h"
#import "SDiyView.h"
#import "SCardDetailController.h"
#import "CartTitle.h"
#import "CartController.h"
#import "SMyView.h"
#import "SaleUtil.h"
#import "UserTermView.h"
#define KEY @"UserTermView_1"

__weak SellingMainController* __controller;


@interface SellingMainController ()
{
    MenuGroup* _group;
    ArrayJsonTask* _menuTask;
    NSInteger _order;
    NSString* _type;
    NSInteger _lastItem;
    SMyView* _myView;
    BOOL _isShown;
    
    SDiyView* _diyView;
}
@property (weak, nonatomic) IBOutlet UIView *bottomView;
@property (weak, nonatomic) IBOutlet UIView *shopView;
@property (weak, nonatomic) IBOutlet ItemView *menu1;
@property (weak, nonatomic) IBOutlet ItemView *menu2;
@property (weak, nonatomic) IBOutlet PullToRefreshCollectionView *collectionView;

@end

@implementation SellingMainController


INIT_CONTROLLER(SellingMainController)

+(SellingMainController*)current{
    return __controller;
}
-(void)dealloc{
    _menuTask = nil;
    _group = nil;
    _diyView = nil;
    __controller = nil;
    _myView = nil;
}

-(void)onUpdateMenu{
    if(_type && _order >= 0){
        [_collectionView setLoadingState];
        [_collectionView.task put:@"type" value:_type];
        [_collectionView.task put:@"order" value:[NSNumber numberWithInteger:_order]];
        [_collectionView.task setPosition:Start_Position];
        [_collectionView.task execute];
        
    }
}

-(void)backToPrevious:(id)sender{
    [super backToPrevious:sender];
    [PopupWindow hide];
}

-(void)onClickMenu:(MenuData*)menuData index:(NSInteger)index{
    if(index == 0){
        _type = menuData.path;
    }else{
        _order = menuData.ID;
    }
    [self onUpdateMenu];
    
}

-(void)viewDidAppear:(BOOL)animated{
    if(!_group){
        _group = [[MenuGroup alloc]initWithFrame:CGRectMake(0, 65+37, self.view.frame.size.width, self.view.frame.size.height - 65 - 37) parent:self.view];
        _group.delegate = self;
        [_group createSingleMenu:_menu1];
        [_group createSingleMenu:_menu2];
        __weak MenuGroup* __group = _group;
        _menuTask.successListener = ^(NSArray* result,NSInteger position,BOOL isLastPage){
  
            MenuData* data = [[MenuData alloc]init];
            data.title = @"全部";
            data.realTitle = @"全部";
            data.ID= 0 ;
            data.path = @"";
            data.selected = true;
            NSMutableArray* arr = [[NSMutableArray alloc]initWithArray:result];
            [arr insertObject:data atIndex:0];
            [__group setData:arr index:0];
            [__group setData:[MenuUtil createOrderArray] index:1];
            
        };
        __weak PullToRefreshCollectionView* __collectionView = _collectionView;
        __weak ArrayJsonTask* __menuTask = _menuTask;
        _menuTask.errorListener = ^(NSString* error,BOOL isNetworkError){
            [__collectionView task:__menuTask error:error isNetworkError:isNetworkError];
        };
        [_menuTask execute];

    }
}

- (void)viewDidLoad {
    [super viewDidLoad];
    __controller = self;
    _order = -1;
    _type = @"1";
    _lastItem = -1;
    [self setTitle:@"买卡"];
    
    dispatch_async(dispatch_get_main_queue(), ^{
        
        NSInteger index = 0;
        for (ItemView* view in _bottomView.subviews) {
            view.tag = index++;
            [view setTarget:self withAction:@selector(onTab:)];
        }
        [self onTab:_bottomView.subviews[0]];
        
        _menuTask = [[ECardTaskManager sharedInstance]createArrayJsonTask:@"s_card_type2" cachePolicy:DMCachePolicy_TimeLimit isPrivate:NO];
        [_menuTask setClass:[STypeVo class]];
        
        ArrayJsonTask* task = [[ECardTaskManager sharedInstance]createArrayJsonTask:@"s_card_list2" cachePolicy:DMCachePolicy_TimeLimit isPrivate:NO];
        [task setCondition:@[@"type",@"order"]];
        [_collectionView setTask:task];
        [_collectionView registerCell:@"SCardCell"];
        [_collectionView setOnItemClickListener:self];
        _collectionView.collectionViewLayout = [SaleUtil getLayout];
        [_collectionView setDataListener:self];
        [task setClass:[SCardListVo class]];
        
        [SaleUtil setRoot:self];
        
        [CartTitle createWidthViewController:self];
    });
    
   
}

-(void)onAgree:(id)sender{
    UserTermView* contentViw = (UserTermView*)[PopupWindow contentView];
    if(contentViw.notShowNextTime.selected){
        NSUserDefaults* def = [NSUserDefaults standardUserDefaults];
        [def setValue:@1 forKey:KEY];
    }
    _isShown = YES;
    [PopupWindow hide];
    self.navigationItem.rightBarButtonItem.enabled = YES;
}

-(void)onItemClick:(UIView*)parent data:(SCardListVo*)data index:(NSInteger)index{
    SCardDetailController* controller = [[SCardDetailController alloc]init];
    controller.data = data;
    [self.navigationController pushViewController:controller animated:YES];
}

-(void)onInitializeView:(UIView*)parent cell:(SCardCell*)cell data:(SCardListVo*)data index:(NSInteger)index{
    [JsonTaskManager setImageSrcDirect:cell.image src:data.thumb];
    cell.price.text = [data priceString];
}

-(void)onTab:(UIView*)view{
    NSInteger tag = view.tag;
    
    if(tag == _lastItem)return;
    
    if(_lastItem>=0){
     
        UIView* lastView = _bottomView.subviews[_lastItem];
        
        UIImageView* icon = lastView.subviews[0];
        UILabel* label = lastView.subviews[1];
        
        [icon setHighlighted:NO];
        [label setHighlighted:NO];
    }
    
    UIImageView* icon = view.subviews[0];
    UILabel* label = view.subviews[1];
    
    [icon setHighlighted:YES];
    [label setHighlighted:YES];
    
    _lastItem = tag;
    
    switch (tag) {
        case 0:
            _shopView.hidden = NO;
            _diyView.hidden = YES;
            _myView.hidden = YES;
            break;
        case 1:
        {
            _shopView.hidden = YES;
            if(!_diyView){
                _diyView =  [ViewUtil createViewFormNibName:@"SDiyView" owner:self];
                _diyView.frame = _shopView.frame;
                _diyView.parent = self;
                [self.view addSubview:_diyView];
            }else{
                _diyView.hidden = NO;
            }
            _myView.hidden = YES;
            
            if(!_isShown){
                dispatch_async(dispatch_get_main_queue(), ^{
                    NSUserDefaults* def = [NSUserDefaults standardUserDefaults];
                    id agree = [def valueForKey:KEY];
                    if(!agree){
                        self.navigationItem.rightBarButtonItem.enabled = NO;
                        UIView* userTerm = [ViewUtil createViewFormNibName:@"UserTermView" owner:self];
                        userTerm.layer.masksToBounds = YES;
                        userTerm.layer.cornerRadius = 5;
                        CGRect rect = [UIScreen mainScreen].bounds;
                        userTerm.frame = CGRectMake(rect.origin.x+10, rect.origin.x + 10, rect.size.width-20, rect.size.height-20-65);
                        userTerm.transform = CGAffineTransformMakeScale(2, 2);
                        userTerm.alpha = 0.2;
                        UIButton* button = (UIButton*)[userTerm viewWithTag:1000];
                        Control_AddTarget(button, onAgree);
                        [ViewUtil setButtonBg:button];
                        [PopupWindow show:userTerm beforeState:^(UIView *contentView) {
                            userTerm.alpha = 0.2;
                            userTerm.transform = CGAffineTransformMakeScale(0.1, 0.1);
                        } endState:^(UIView *contentView) {
                            userTerm.alpha = 1;
                            userTerm.transform = CGAffineTransformIdentity;
                        } frame:ConentViewFrame].notAutoHide = YES;
                    }
                });

            }
            

        }
            break;
        case 2:
        {
            _shopView.hidden = YES;
            _diyView.hidden = YES;
            if(!_myView){
                _myView =  [ViewUtil createViewFormNibName:@"SMyView" owner:self]; //[[SDiyView alloc]initWithFrame:_shopView.frame parent:self];
                _myView.frame = _shopView.frame;
                _myView.parent = self;
                [self.view addSubview:_myView];
            }else{
                _myView.hidden = NO;
            }
            dispatch_async(dispatch_get_main_queue(), ^{
                [_myView onViewWillAppear];
            });
            
            
        }
            
            break;
        default:
            break;
    }
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    
    switch (_lastItem) {
        case 0:
            
            break;
        case 1:
            [_diyView reloadData];
            break;
        case 2:
            [_myView onViewWillAppear];
            break;
        default:
            break;
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
