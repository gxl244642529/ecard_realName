//
//  MySafeOrderController.m
//  ecard
//
//  Created by 任雪亮 on 16/3/14.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "MySafeOrderController.h"
#import "TermLabel.h"
#import "SafeUtil.h"
#import "MySafeDetailController.h"
#import "SafeModel.h"

@interface MySafeOrderController ()
@property (weak, nonatomic) IBOutlet TermLabel *term;
@property (weak, nonatomic) IBOutlet DMFixTableView *tableView;
@property (weak, nonatomic) IBOutlet UIView *addressView;
@property (weak, nonatomic) IBOutlet UIView *carInfo;

@end

@implementation MySafeOrderController

@synthesize data=_data;
INIT_BUNDLE_CONTROLLER(MySafeOrderController, safebundle.bundle)
- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"我的保单"];
    [SafeUtil setTermForResult:_term noticeUrl:_data.noticeUrl protocalUrl:_data.protocolUrl];
    
    if(!_data.carNo){
        _carInfo.hidden = YES;
        [_carInfo findHeight].constant = 0;
    }
    
    if(!_data.address){
        _addressView.hidden = YES;
        [_addressView findHeight].constant = 0;
    }
    
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (IBAction)onShow:(id)sender {
    Push_ViewController(MySafeDetailController);
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
