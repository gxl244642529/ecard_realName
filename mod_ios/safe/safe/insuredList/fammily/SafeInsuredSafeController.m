//
//  SafeInsuredSafeController.m
//  ecard
//
//  Created by 任雪亮 on 16/3/9.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "SafeInsuredSafeController.h"
#import "SafeTypeVo.h"
#import "SafeUtil.h"
#import "SafeCardDetailController.h"
#import "SafeInsuredDetailController.h"
#import "SafeInsuredDetailController.h"
#import "SafeInsuredShowController.h"
@interface SafeInsuredSafeController ()
@property (weak, nonatomic) IBOutlet DMTableView *tableView;

@end

@implementation SafeInsuredSafeController
@synthesize data = _data;


INIT_BUNDLE_CONTROLLER(SafeInsuredSafeController, safebundle.bundle)


- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:_data.title];
    [_tableView setOnItemClickListener:self];
    [_tableView put:@"typeId" value:_data.typeId];
    [_tableView execute];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
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


/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
