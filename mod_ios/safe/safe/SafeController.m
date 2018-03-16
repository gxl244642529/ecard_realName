

//
//  SafeController.m
//  ecard
//
//  Created by randy ren on 16/1/16.
//  Copyright © 2016年 citywithincity. All rights reserved.
//
#import <ecardlib/ecardlib.h>
#import "SafeController.h"
#import "SafeHeaderView.h"
#import "SafeVo.h"
#import "SafeUtil.h"
#import "UIButton+NetIcon.h"
#import "SafeTypeVo.h"
#import "SafeListController.h"
#import "MySafeController.h"
#import "SafeInsuredSafeController.h"


@interface SafeController ()
{
    __weak IBOutlet DMTableView *_tableView;
    __weak DMButtonGroup* _group;
}
@end

@implementation SafeController


INIT_BUNDLE_CONTROLLER(SafeController, safebundle.bundle)


- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"保险"];
    self.view.backgroundColor = RGB_WHITE(f2);
    
    SafeHeaderView* header = _tableView.realHeaderView;
    _group = header.buttonGroup;
    _group.tabDelegate = self;
    
    [_tableView put:@"typeId" value:@"1"];
    [_tableView execute];
    UIBarButtonItem* item =  [DMViewController createTextItem:@"我的保单" controllerClass:[MySafeController class] parent:self];
    item.tintColor = [UIColor redColor];
    self.navigationItem.rightBarButtonItem = item;
}




-(void)tab:(id)tab didSelectIndex:(NSInteger)index{
    
    //这里的typeId,取数据
    
    SafeTypeVo* dic = [[SafeTypeVo alloc]init];
    UIButton* button = _group.subviews[index];
    dic.title = button.titleLabel.text;
    dic.typeId = button.viewName;
    dic.type = [dic.typeId integerValue];
    if(dic.type==1){
        Push_ViewController_Data(SafeListController, dic);
    }else{
        Push_ViewController_Data(SafeInsuredSafeController, dic);
    }
    
}

-(void)onClick:(UIButton*)sender{
    //这里发出
    [_tableView put:@"type_id" value:[NSString stringWithFormat:@"%ld",(long)sender.tag]];
    [_tableView execute];
    
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
