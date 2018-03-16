//
//  SafeInsuredDetailController.m
//  ecard
//
//  Created by randy ren on 16/1/26.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "SafeInsuredDetailController.h"
#import "SafeBuyInsuredController.h"
#import "SafePayResultController.h"
#import "SafeBuyInsuredController.h"
#import "SafeBuyFamilly.h"
#import "SafeUtil.h"
#import "SafeBuyChildrenController.h"
@interface SafeInsuredDetailController ()
{
    
    __weak IBOutlet DMDetailView *_detail;
    __weak IBOutlet UILabel *_txtPrice;
    __weak IBOutlet SafeCounterView *_counter;
    __weak IBOutlet DMCheck *_btnCheck;
    __weak IBOutlet TermLabel *_term;
    __weak IBOutlet PageButton *_btnBuy;
    __weak SafeDetailVo* _detailVo;
}
@end

@implementation SafeInsuredDetailController

@synthesize data=_data;
INIT_BUNDLE_CONTROLLER(SafeInsuredDetailController, safebundle.bundle)

-(void)onCountChanged:(NSInteger)count{
    _txtPrice.text = [NSString stringWithFormat:@"¥%.2f",_detailVo.priceVal * count] ;
}



- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:_data.title];
    _counter.delegate = self;
}
API_JOB_SUCCESS(i_safe, detail,SafeDetailVo*){
    [SafeUtil setTerm:_term noticeUrl:result.noticeUrl protocalUrl:result.protocolUrl];
    _btnBuy.enabled = YES;
    _detailVo = result;
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
    _data.count = _counter.count;
    if([_data.inId isEqualToString:@"5440"]){
         Push_ViewController_Data(SafeBuyChildrenController, _data);
    }else{
         Push_ViewController_Data(SafeBuyFamilly, _data);
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
