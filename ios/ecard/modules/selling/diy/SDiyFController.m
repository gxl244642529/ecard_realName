//
//  SDiyFController.m
//  ecard
//
//  Created by randy ren on 15-2-13.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "SDiyFController.h"
#import "SaleUtil.h"
#import "SellingModel.h"

#import "SCardCell.h"
#import "ECardTaskManager.h"

#import "NetworkImage.h"
#define BOTTOM_HEIGHT 127

@interface SDiyFController ()
{
    NetworkImage* _imageView;
    ArrayJsonTask* _task;
    UIScrollView* _scrollView;
    __weak NSArray* _arr;
    __weak UIView* _lastSelected;
}
@end

@implementation SDiyFController

-(void)dealloc{
    _imageView = NULL;
    _scrollView = NULL;
    _task = NULL;
    [SVProgressHUD dismiss];
}
- (void)viewDidLoad
{
    [super viewDidLoad];
    [self setTitle:@"选择版面"];
    CGRect rect = [UIScreen mainScreen].bounds;
    CGRect frame = CGRectMake(0, 0, rect.size.width, rect.size.height-65);
    // Initialization code
    //世界显示的
    CGFloat width = self.view.frame.size.width-20;
    CGFloat height = (float)CARD_HEIGHT /  CARD_WIDTH  * width;
    
    _imageView = [[NetworkImage alloc]initWithFrame:CGRectMake(0,0,width,height)];
    _imageView.center = CGPointMake(self.view.bounds.size.width/2, frame.origin.y + (frame.size.height - BOTTOM_HEIGHT)/2);
    _imageView.layer.cornerRadius = 10;
    _imageView.layer.masksToBounds = TRUE;
    [self.view addSubview:_imageView];
    _imageView.backgroundColor = [[UIColor lightGrayColor]colorWithAlphaComponent:0.2];
    
    _task = [[JsonTaskManager sharedInstance]createArrayJsonTask:@"s_diy_pages2" cachePolicy:DMCachePolicy_NoCache isPrivate:false ];
    [_task setClass:[SDiyPagesVo class]];
    [_task setListener:self];
    [_task execute];
    [SVProgressHUD showWithStatus:@"加载数据..."];
    _scrollView = [[UIScrollView alloc]initWithFrame:CGRectAlignBottom(frame,BOTTOM_HEIGHT)];
    [self.view addSubview:_scrollView];
    
    Control_AddTarget([self createTitleButton:@"保存"],onSave);
}

-(void)onSave:(id)sender{
    if(_lastSelected){
        NSInteger index = _lastSelected.tag;
        SDiyPagesVo* data = _arr[index];
               
        [self.delegate editFComplete:data];
    }
    
    [self finish];
    
}

-(void)task:(NSObject<IJsonTask> *)task result:(NSArray *)result position:(NSInteger)position isLastPage:(BOOL)isLastPage{
    _arr = result;
    int i=0;
    [SVProgressHUD dismiss];
    DiyVo* data = self.data;
    for( SDiyPagesVo* vo in result) {
        SCardCell* cell = [ViewUtil createViewFormNibName:@"SCardCell" owner:self];//
        cell.tag = i;
        UITapGestureRecognizer* gest =[[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(onTap:)];
        [cell addGestureRecognizer:gest];
        cell.frame =CGRectMake(154*i++, 0, 153, 127);
        cell.price.text = vo.stringPrice;
        [ECardTaskManager setImageSrcDirect:cell.image src:vo.thumb];
        [_scrollView addSubview:cell];
        if(data.ID==vo.ID){
            _lastSelected = cell;
            _lastSelected.backgroundColor = [[UIColor lightGrayColor]colorWithAlphaComponent:0.2];
            [_imageView load:vo.img];
        }
        
    }
    _scrollView.contentSize = CGSizeMake(154*result.count, 0);
}

-(void)onTap:(UITapGestureRecognizer*)sender{
    sender.view.alpha = 0.2;
    [UIView animateWithDuration:0.3
                     animations:^{
                          sender.view.alpha = 1;
                     }
     ];
    if(_lastSelected){
        _lastSelected.backgroundColor = [UIColor whiteColor];
    }
    _lastSelected = sender.view;
    sender.view.backgroundColor = [[UIColor lightGrayColor]colorWithAlphaComponent:0.2];
    
    NSInteger index = sender.view.tag;
    SDiyPagesVo* data = _arr[index];
    if(data.store <= 0){
        [SVProgressHUD showErrorWithStatus:@"本卡已经没有库存了,请选择其他卡片或者联系客服"];
    }
    [_imageView load:data.img];
    
}

-(void)task:(id)task error:(NSString *)errorMessage isNetworkError:(BOOL)isNetworkError{
    [SVProgressHUD showErrorWithStatus:errorMessage];
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
