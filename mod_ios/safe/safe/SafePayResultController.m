//
//  SafePayResultController.m
//  ecard
//
//  Created by randy ren on 16/1/28.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "SafePayResultController.h"
#import "TTTAttributedLabel.h"
#import "SafeUtil.h"
#import "TermLabel.h"
#import "MySafeController.h"


@interface SafePayResultController ()


@property (weak, nonatomic) IBOutlet TermLabel *term;

@end

@implementation SafePayResultController

@synthesize data=_data;


INIT_BUNDLE_CONTROLLER(SafePayResultController, safebundle.bundle)
    
- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"支付结果"];
    [SafeUtil setTermForResult:_term noticeUrl:_data.noticeUrl
         protocalUrl:_data.protocolUrl];
    
}

-(void)finish{
    MySafeController* c = [CommonUtil findController:self.navigationController clazz:MySafeController.class];
    if(c){
        [c updateListView];
    }
    [super finish];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (IBAction)onGotoMySafe:(id)sender {
    
    
    //如果有的话
    MySafeController* c = [CommonUtil findController:self.navigationController clazz:MySafeController.class];
    if(c){
        [c updateListView];
        [self finish];
    }else{
        UINavigationController* c = self.navigationController;
        [c popViewControllerAnimated:NO];
        [c pushViewController:[[MySafeController alloc]init] animated:YES];
    }
    
   
    
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
