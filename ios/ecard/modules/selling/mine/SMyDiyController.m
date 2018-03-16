//
//  SMyDiyController.m
//  ecard
//
//  Created by randy ren on 15-1-8.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "SMyDiyController.h"
#import "SellingModel.h"
#import "SaleUtil.h"
#import "OnClickListenerExt.h"
#import "SCardCell.h"
@interface SMyDiyController ()
{
    CollectionGroup* _group;
    ArrayJsonTask* _task;
}
@end

@implementation SMyDiyController

-(void)dealloc{
    _group = NULL;
    _task = NULL;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self setTitle:@"我的DIY"];
    _task = [[JsonTaskManager sharedInstance]createArrayJsonTask:@"s_my_diy_list" cachePolicy:DMCachePolicy_TimeLimit isPrivate:YES ];
    [_task setListener:_group];
    [_task setClass:[SDiyListVo class]];
    
    _group =[[CollectionGroup alloc]initWithFrame:ConentViewFrame parent:self.view listener:self task:_task layout:[SaleUtil getLayout]];
    [[_group getAdapter]registerCell:@"SCardCell" bundle:nil];
  //  [[_group getAdapter]setOnItemClickListener:[OnClickListenerEx parent:self clsRef:[SdiyDetailController class]]];

}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void)onInitializeView:(UIView *)parent cell:(SCardCell*)cell data:(SDiyListVo*)data index:(NSInteger)index{
    
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
