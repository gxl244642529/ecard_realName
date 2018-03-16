//
//  SafeCardDetailController.m
//  ecard
//
//  Created by randy ren on 16/1/24.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "SafeCardDetailController.h"

#import "SafeDetailVo.h"
#import "TTTAttributedLabel.h"
#import "SafeUtil.h"
#import "SafeCardBuyController.h"

@interface SafeCardDetailController ()
{
    
    __weak IBOutlet UILabel *_txtPrice;
    
    __weak IBOutlet PageButton *btnBuy;
    __weak IBOutlet DMCheck *_btnCheck;
    __weak IBOutlet TermLabel *_term;
    __weak SafeDetailVo* _detailVo;
}
@end

@implementation SafeCardDetailController

@synthesize data = _data;
INIT_BUNDLE_CONTROLLER(SafeCardDetailController, safebundle.bundle)

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:_data.title];
}


API_JOB_SUCCESS(i_safe, detail,SafeDetailVo*){
    [SafeUtil setTerm:_term noticeUrl:result.noticeUrl protocalUrl:result.protocolUrl];
    btnBuy.enabled = YES;
    _detailVo = result;
    
    if(_data.ticket){
        _txtPrice.text = @"免费";
    }
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
- (IBAction)onBuy:(id)sender {
    if(![DMAccount isLogin]){
        [DMAccount callLoginController:nil];
        return;
    }
    
    if(!_btnCheck.selected){
        [Alert alert:@"请先接受协议"];
        return;
    }
    //开始购买
    Push_ViewController_Data(SafeCardBuyController, _data);
    // [SafeUtil startBuy:self listVo:_data detailVo:_detailVo];
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
