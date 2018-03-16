//
//  SDiySelImageController.m
//  ecard
//
//  Created by randy ren on 15/4/13.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "SDiySelImageController.h"
#import "SImageCell.h"

@interface SDiySelImageController ()
{
    ArrayJsonTask* _task;
    PullToRefreshGridTableView* _tableView;
}
@end

@implementation SDiySelImageController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"选择图片"];
    
    _task = [[JsonTaskManager sharedInstance]createArrayJsonTask:@"s_image_list" cachePolicy:DMCachePolicy_NoCache isPrivate:YES];
    
    
    _tableView = [[PullToRefreshGridTableView alloc]initWithFrame:self.view.bounds];
    [self.view addSubview:_tableView];
    _tableView.rowHeight = 128;
    [_tableView registerCell:@"SImageCell"];
    [_tableView setDataListener:self];
    [_tableView setTask:_task];
    
    [_task setPosition:Start_Position];
    [_task execute];
    
}

-(void)onInitializeView:(UIView*)parent cell:(SImageCell*)cell data:(NSDictionary*)data index:(NSInteger)index{
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
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
