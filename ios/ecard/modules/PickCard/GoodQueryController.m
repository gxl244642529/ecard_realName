//
//  GoodQueryController.m
//  ecard
//
//  Created by randy ren on 15/4/7.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "GoodQueryController.h"
#import "PickCardCell.h"
#import "LostCardCell.h"
#import "OnClickListenerExt.h"
#import "LostCardDetailController.h"

@interface GoodQueryController ()
{
    PullToRefreshTableView* _tableView1;
    PullToRefreshTableView* _tableView2;
    
}
@end

@implementation GoodQueryController

-(void)dealloc{
    _tableView1 = nil;
    _tableView2 = nil;
}




- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"信息查询"];
    
    TabPane2* tab = [ViewUtil createViewFormNibName:@"TabPane2" owner:self];
    [self.view addSubview:tab];
    tab.frame = [UIScreen mainScreen].bounds;
    [tab setTitles:@[@"拾卡",@"失卡"]];
    [tab setDelegate:self];
    
}

-(UIView*)tabInitItem:(NSInteger)index frame:(CGRect)frame{
    if(index==0){
        _tableView1 = [[PullToRefreshTableView alloc]initWithFrame:frame];
        _tableView1.rowHeight = 57;
        [_tableView1 registerCell:@"PickCardCell"];
        [_tableView1 setDataListener:self];
        [_tableView1 setOnItemClickListener:[OnClickListenerEx parent:self nibName:@"PickCardDetailController"]];
        return _tableView1;
    }else{
         _tableView2 = [[PullToRefreshTableView alloc]initWithFrame:frame];
        _tableView2.rowHeight = 57;
        [_tableView2 registerCell:@"LostCardCell"];
        [_tableView2 setDataListener:self];
        [_tableView2 setOnItemClickListener:[OnClickListenerEx  parent:self nibName:@"LostCardDetailController"]];
        
        return _tableView2;
    }
    return nil;
}
-(void)onInitializeView:(UIView*)parent cell:(UIView*)view data:(NSDictionary*)data index:(NSInteger)index{
    
    if([view isKindOfClass:[PickCardCell class]]){
        
        PickCardCell* cell = (PickCardCell*)view;
        cell.cardNumber.text = Data_ValueForKey(CARD_ID);
        cell.time.text = Data_ValueForKey(TIME);
        
    }else{
        
        LostCardCell* cell = (LostCardCell*)view;
        cell.cardNumber.text = Data_ValueForKey(INFO);
        
        cell.info.text = Data_ValueForKey(LAST_MODIFIED);
        
    }
    

}
-(void)tabDidSelected:(NSInteger)index first:(BOOL)first{
    if(first){
        if(index==0){
            ArrayJsonTask* task = [[JsonTaskManager sharedInstance]createArrayJsonTask:@"pick_card_list" cachePolicy:DMCachePolicy_UseCacheFirst isPrivate:NO ];
            
            [_tableView1 setTask:task];
          [task put:@"last" value:@0];
            [task execute];
        }else{
              ArrayJsonTask*  task = [[JsonTaskManager sharedInstance]createArrayJsonTask:@"lost_card_list" cachePolicy:DMCachePolicy_UseCacheFirst isPrivate:NO ];
            [_tableView2 setTask:task];
          [task put:@"last" value:@0];

            [task execute];
        }
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
