//
//  SDiyView.m
//  ecard
//
//  Created by randy ren on 15-1-8.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "SDiyView.h"
#import "OnClickListenerExt.h"
#import "MyDiyCell.h"
#import "FileArrayJsonTask.h"
#import "SDiyController.h"
#import "ECardTaskManager.h"
#import "SCardCell.h"
#import "CardModel.h"
#import "SaleUtil.h"
#import <ecardlib/ecardlib.h>
#import "PullToRefreshCollectionView.h"
#import "SOnlineDiyCell.h"
#import "SDiyDetailController.h"
#import "SizeUtil.h"
#import "NetworkImage.h"

#define BUTTON_WIDTH 110
#define SEGMENT_HEGIHT 39
#define SEGMENT_WITH 220


@interface SDiyView()
{
    __weak id _deleteVo;
    FileArrayJsonTask* _file;
    __weak IBOutlet UICollectionView* _collectionView;
    __weak IBOutlet PullToRefreshCollectionView *_onlineListView;
    __weak IBOutlet UIButton *btn0;
    __weak IBOutlet UIScrollView *_scrollView;
    __weak IBOutlet UIButton *btn1;
    __weak IBOutlet NSLayoutConstraint *viewwidth;
}

@end


@implementation SDiyView

-(void)updateConstraints{
    [super updateConstraints];
    viewwidth.constant = [UIScreen mainScreen].bounds.size.width;
}


-(void)dealloc{
    _file = NULL;
    _collectionView = NULL;
}


-(void)awakeFromNib{
  [super awakeFromNib];
    btn1.tag = 1;
    Control_AddTarget(btn0, onTabButton);
    Control_AddTarget(btn1, onTabButton);
    btn0.selected = YES;
    _scrollView.pagingEnabled= YES;
    _scrollView.delegate = self;
    
    _file = [[FileArrayJsonTask alloc]init];
    [_file setClass:[DiyVo class]];
    

    [_collectionView setBackgroundColor:[UIColor clearColor]];
    [_collectionView registerNib:[UINib nibWithNibName:@"SMyDiyAddCell" bundle:nil] forCellWithReuseIdentifier:@"Empty"];
    [_collectionView registerNib:[UINib nibWithNibName:@"MyDiyCell" bundle:nil] forCellWithReuseIdentifier:@"Cell"];
    
    [_file getList];
    _collectionView.delegate = self;
    _collectionView.dataSource = self;

    
    
}
-(void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView{
    CGFloat current =scrollView.contentOffset.x;
    if(current==0){
        [self onTabButton:btn0];
    }else{
        [self onTabButton:btn1];
    }
}

-(void)onItemClick:(UICollectionView*)parent data:(NSDictionary*)data index:(NSInteger)index{
    
    SOnlineDiyCell* child = (SOnlineDiyCell*)[parent cellForItemAtIndexPath:[NSIndexPath indexPathForItem:index inSection:0]];
    CGRect rect = [child convertRect:child.image.frame toView:self.parent.view];
  
    NetworkImage* imageView = [[NetworkImage alloc]initWithFrame:rect];
    
    dispatch_async(dispatch_get_main_queue(), ^{
        [imageView load: [Constants formatUrl: data[@"IMG_Z"]]];
    });
    
    imageView.layer.masksToBounds = YES;
    imageView.layer.cornerRadius = 3;
    
    [imageView setImage:child.image.image];
    [PopupWindow show:imageView beforeState:^(UIView *contentView) {
       contentView.transform = CGAffineTransformIdentity;
    } endState:^(UIView *contentView) {
        
        CGFloat dx = ([UIScreen mainScreen].bounds.size.height - 30) / rect.size.width;
        CGFloat dy = ([UIScreen mainScreen].bounds.size.width-20)  / rect.size.height;
        
        dx = MIN(dx, dy);
        //计算变形后的左上角的位置
        CGPoint center = CGPointMake(rect.origin.x + rect.size.width/2, rect.origin.y + rect.size.height/2);
        //计算
        CGAffineTransform t = CGAffineTransformMakeTranslation([UIScreen mainScreen].bounds.size.width/2 - center.x, [UIScreen mainScreen].bounds.size.height/2 - center.y);
        t= CGAffineTransformRotate(t, M_PI/2);
        t=CGAffineTransformScale(t, dx, dx);
        contentView.transform = t;
       } frame:[UIScreen mainScreen].bounds];
  
    
}

-(void)onInitializeView:(UIView*)parent cell:(SOnlineDiyCell*)cell data:(NSDictionary*)data index:(NSInteger)index{
    [[JsonTaskManager sharedInstance]setImageSrc:cell.image src:data[@"THUMB"]];
    cell.title.text = data[@"TITLE"];
}

-(void)onTabButton:(UIView*)sender{
    btn0.selected = sender == btn0;
    btn1.selected = sender == btn1;
    [_scrollView setContentOffset: CGPointMake(sender == btn0 ? 0 : _scrollView.frame.size.width, 0) animated:YES];
    
    if(sender==btn1){
        if(!_onlineListView.task){
            ArrayJsonTask* task = [[ECardTaskManager sharedInstance]createArrayJsonTask:@"s_diy_list" cachePolicy:DMCachePolicy_TimeLimit isPrivate:NO];
          [task put:@"last" value:@0];
            [_onlineListView setTask:task];
            [_onlineListView registerCell:@"SOnlineDiyCell"];
            [_onlineListView setOnItemClickListener:self];
            _onlineListView.collectionViewLayout = [SaleUtil getLayout];
            [_onlineListView setDataListener:self];
            
            [task setPosition:Start_Position];
            [task execute];
            
        }

    }
   
}

//集合代理-每一部分数据项
- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section{
    
    return 1 + [_file getCount];
}

//Cell
- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *cellIdentifier = @"Cell";
    static NSString * empty = @"Empty";
    if(indexPath.row==0){
        UICollectionViewCell *cell=[collectionView dequeueReusableCellWithReuseIdentifier:empty forIndexPath:indexPath];
        return cell;
    }else{
        MyDiyCell *cell=[collectionView dequeueReusableCellWithReuseIdentifier:cellIdentifier forIndexPath:indexPath];
        cell.tag =indexPath.row-1;
        [cell.btnDelete setTarget:self withAction:@selector(onDeleteItem:)];
        DiyVo* myDiyVo = [_file getItem:indexPath.row-1];
        [JsonTaskManager setImagePath:myDiyVo.thumb image:cell.image];
        return cell;
    }
    
}



-(void)onDeleteItem:(MyDiyCell*)cell{
    NSInteger index = cell.tag;
    DiyVo* data = [_file getItem:index];
     [Alert confirm:@"确实要删除此定制吗，此操作不可恢复?" title:@"删除确认" delegate:self];
    _deleteVo = data;

  
}


- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    if(buttonIndex==1){
        
        [_file removeObject:_deleteVo];
        [self reloadData];
    }
}

-(void)reloadData{
    [_collectionView reloadData];
}

//代理－选择行的触发事件
- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath
{
    //NSLog(@"cell selected at index path %ld", (long)indexPath.row);
    NSInteger index = indexPath.row;
    if(index>0){
        
        [_file setCurrent:[_file getItem:indexPath.row-1]];
        
    }else{
        [_file createNewAsCurrent];
    }
    //进入定制
    SDiyController* controller = [[SDiyController alloc]init];
   [controller setModel:_file];
    [self.parent.navigationController pushViewController:controller animated:YES];
   // [_parent.navigationController pushViewController:controller animated:YES];
}


/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

@end
