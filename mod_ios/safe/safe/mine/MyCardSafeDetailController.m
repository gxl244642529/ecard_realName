//
//  MyCardSafeDetailController.m
//  ecard
//
//  Created by randy ren on 16/2/3.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "MyCardSafeDetailController.h"
#import "SafeUtil.h"
#import "SafeCardClaimController.h"
#import "SafeModel.h"

/**
 "delivery_addr":"福建省厦门市思明区fgg","delivery_tel":"15259298519","express_company":null,"delivery_no":null,"name":""
 */

@interface MyCardSafeDetailController ()
{
    
    __weak IBOutlet NSLayoutConstraint *_bottomHeight;
    __weak IBOutlet UIView *cailmView;
    __weak IBOutlet UIImageView *_step1;
    
    __weak IBOutlet UIImageView *_step2;
    
    __weak IBOutlet UIImageView *_step3;
    __weak IBOutlet UIImageView *_headerImage;
    //理赔进度
    __weak IBOutlet UILabel *claimProgress;
    __weak IBOutlet UIImageView *statusImage;
    __weak IBOutlet NSLayoutConstraint *cailmViewHeight;
    __weak IBOutlet UIView *_cailmHeader;
    
    
    SafeModel* _safeModel;
}


@property (weak, nonatomic) IBOutlet TermLabel *term;
@property (weak, nonatomic) IBOutlet DMItem *showCard;
@property (weak, nonatomic) IBOutlet DMDetailView *detailView;

@property (weak, nonatomic) IBOutlet UIView *bottomView;


@property (weak, nonatomic) IBOutlet UILabel *name;
@property (weak, nonatomic) IBOutlet UILabel *phone;

@property (weak, nonatomic) IBOutlet UILabel *addr;
@property (weak, nonatomic) IBOutlet UILabel *devCode;
@property (weak, nonatomic) IBOutlet UILabel *devCom;

@end

@implementation MyCardSafeDetailController

@synthesize data=_data;

-(void)dealloc{
    _safeModel = nil;
}
INIT_BUNDLE_CONTROLLER(MyCardSafeDetailController, safebundle.bundle)
- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"卡保详情"];
    self.view.backgroundColor = RGB_WHITE(f2);
    [_showCard setTarget:self withAction:@selector(onShowCard:)];
    [SafeUtil setTermForResult:_term noticeUrl:_data.noticeUrl protocalUrl:_data.protocolUrl];
    _safeModel = [[SafeModel alloc]init];
    [self updateStatus:_data.status];
    
}

- (IBAction)onCailm:(id)sender {
    
  /*  if(_data.status==6){
        [Alert alert:@"您的保单还没有生效,请等待生效之后报失"];
        return;
    }*/
    __weak typeof (self) __self = self;
    [Alert confirm:@"报失成功后您的e通卡将无法再次使用。保险公司有权根据您的报失记录调整承保条件，同时保留对恶意骗保等违法行为的追责权利。\n确认报失吗？" title:@"温馨提示" confirmListener:^(NSInteger buttonIndex) {
        if(buttonIndex==1){
            [__self gotoCailm];
        }
    }];
}


-(void)gotoCailm{
    Push_ViewController_Data(SafeCardClaimController, _data);
}




-(void)updateStatus:(NSInteger)status{
    
    
    
    if([_data shouldHideLostButton]){
        _bottomHeight.constant = 0;
        _bottomView.hidden = YES;

    }
    
    if(status==0 || status==6){
        _cailmHeader.hidden = YES;
        cailmViewHeight.constant = 0;
        cailmView.hidden = YES;
        
    }else{
        switch (status) {
            case 1:
            {
                _step2.highlighted = YES;
                 cailmViewHeight.constant = 180;
                [_safeModel getOrderInfo:_data.orderId];
            }
               
                break;
            case 2:
            {
                _step2.highlighted = YES;
                cailmViewHeight.constant = 180;
                [_safeModel getOrderInfo:_data.orderId];
            }
                break;
            case 3:
            {
                _step3.highlighted = YES;
                cailmViewHeight.constant = 180;
                [_safeModel getOrderInfo:_data.orderId];
            }
               
                break;
            case 4:
            {
                cailmViewHeight.constant = 0;
                _cailmHeader.hidden = YES;
                cailmViewHeight.constant = 0;
                cailmView.hidden = YES;
            }
            default:
                break;
        }
        
    }
    
    
    if(status==0){
        statusImage.image = [UIImage imageNamed:@"safebundle.bundle/ic_insurance_in_security.png"];
    }else if(status==4){
        statusImage.image = [UIImage imageNamed:@"safebundle.bundle/ic_insurance_expires.png"];
    }else if(status==5){
        statusImage.image = [UIImage imageNamed:@"safebundle.bundle/ic_insurance_expires.png"];
    }


}


API_JOB_SUCCESS(i_m_safe, detail, MySafeClaimInfo*){
    
    
    _name.text = result.name;
    _phone.text = result.phone;
    _addr.text = result.addr;
    
    _devCode.text = result.devCode;
    _devCom.text = result.devCom;
    
}

-(void)onShowCard:(id)sender{
    
    [self.navigationController pushViewController:[[DMWebViewController alloc]initWithTitle:@"样卡展示" url: _data.sampleUrl] animated:YES];
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
