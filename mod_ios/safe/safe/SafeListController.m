//
//  SafeListController.m
//  ecard
//
//  Created by randy ren on 16/1/24.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "SafeListController.h"
#import "SafeTypeVo.h"
#import "SafeUtil.h"
#import "SafeCardDetailController.h"
#import "SafeInsuredDetailController.h"
#import "SafeInsuredDetailController.h"
#import "SafeInsuredShowController.h"



@interface SafeListController ()
{
    
    IBOutlet DMTableView *_tableView;
}
@end

@implementation SafeListController

@synthesize data = _data;


INIT_BUNDLE_CONTROLLER(SafeListController, safebundle.bundle)


- (void)viewDidLoad {
    [super viewDidLoad];
    /*
    _data = [[SafeTypeVo alloc]init];
    _data.title = @"";
    _data.type = 3;
    _data.typeId = @"3";
    */
    
    
    [self setTitle:_data.title];
    [_tableView setOnItemClickListener:self];
   // [_tableView setListener:self];
    [_tableView put:@"typeId" value:_data.typeId];
    [_tableView execute];
    
    //insuredDetail
}



-(void)onItemClick:(UIView*)parent data:(SafeVo*)data index:(NSInteger)index{
    //safeCardDetail
    //insuredDetail
    Class controllerClass;
    if(data.type == SafeType_Card){
        //卡宝先
        controllerClass = [SafeCardDetailController class];
    }else {
        if(data.isSale){
            controllerClass = [SafeInsuredDetailController class];
        }else{
            controllerClass = [SafeInsuredShowController class];
        }
    }
    
    DMViewController* controller = [[controllerClass alloc]init];
    controller.data = data;
    
    [self.navigationController pushViewController:controller animated:YES];
    
}


-(void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender{
    [super prepareForSegue:segue sender:sender];
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
