//
//  DMOrderConfirmController.m
//  ecard
//
//  Created by randy ren on 16/1/30.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "DMOrderConfirmController.h"

@interface DMOrderConfirmController ()

@end

@implementation DMOrderConfirmController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}


-(void)dealloc{
    _payModel = nil;
}

-(void)createPayModel{
    if(!_payModel){
        _payModel = [[DMJobManager sharedInstance]createPayModel:@"safe" supportTypes:@[
                                                                                        
                                                                                   WRAP_INTEGER( DMPAY_WEIXIN )
                                                                                        
                                                                                    ]];
        _payModel.delegate = self;
    }
}



- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
-(void)pay:(DMPayModel*)pay didPaySuccess:(id)data{
    //这里应该解析数据,并展示结果页面
    UIViewController* prevController = [CommonUtil findPrevController:self];
    [self.navigationController popToViewController:prevController animated:NO];
    DMViewController* controller= [[_payResultController alloc]init];
    controller.data = data;
    [prevController.navigationController pushViewController:controller animated:YES];
}

-(BOOL)payCancel:(DMPayModel *)pay{
    UIViewController* prevController = [CommonUtil findPrevController:self];
    [self.navigationController popToViewController:prevController animated:YES];
    
    return YES;
}


-(void)pay:(DMPayModel *)pay getOrderInfoError:(NSString *)error isNetworkError:(BOOL)isNetworkError{
    [Alert alert:error];
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
