//
//  BusinessDetailController.m
//  ecard
//
//  Created by randy ren on 16/2/22.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "BusinessDetailController.h"


#import "BusinessDetailVo.h"

@interface BusinessDetailController ()
{
    BusinessDetailVo* _detail;
    
    
    __weak IBOutlet UILabel *_txtDetail;
    DMLocationInfo* _locationInfo;
}
@property (weak, nonatomic) IBOutlet DMItem *btnImages;
@property (weak, nonatomic) IBOutlet DMItem *btnPoi;
@property (weak, nonatomic) IBOutlet DMItem *btnShops;
@property (weak, nonatomic) IBOutlet DMItem *btnPhone;
@property (weak, nonatomic) IBOutlet UILabel *txtDistance;

@end

@implementation BusinessDetailController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"商家详情"];
    [_btnPhone setTarget:self withAction:@selector(onPhone:)];
    [_btnPoi setTarget:self withAction:@selector(onPoi:)];
    
    __weak BusinessDetailController* __self = self;
    
    [DMMapUtil getPosition:^(DMLocationInfo* info) {
        [__self onLocationSuccess:info];
    }];
    /*
    _locationModel = [[DMLocationModel alloc]init];
    _locationModel.delegate = self;
    [_locationModel startLocation];
     */
}
-(void)onLocationSuccess:(DMLocationInfo*)info{
    _locationInfo = info;
    
    if(_detail){
        //计算
        [self formatDistance];
    }
}

-(void)formatDistance{
   double distance =  [DMMapUtil getDistance:_detail.lat lng:_detail.lng targetLat:_locationInfo.lat targetLng:_locationInfo.lng];
    
    _txtDistance.text = [NSString stringWithFormat:@"%d米",(int)distance];
    
}

-(void)onLocationFailWithCode:(NSInteger)code description:(NSString*)description{
    _locationInfo = nil;
}
-(void)dealloc{
   // _locationModel = nil;
    _detail = nil;
    _locationInfo = nil;
}


API_JOB_SUCCESS(business, detail, BusinessDetailVo*){
    
    _detail = result;
    _txtDetail.text = _detail.detail;
    if(_locationInfo){
        //计算
        [self formatDistance];
    }
}


-(void)onPhone:(id)sender{
    if(!_detail)return;
    [CommonUtil makePhoneCall:_detail.phone parent:self.view];
}
-(void)onPoi:(id)sender{
    if(!_detail)return;
    if(!_locationInfo)return;
    
   
    
    NSString* url = [NSString stringWithFormat:@"http://m.amap.com/navi/?start=%f,%f&dest=%f,%f&destName=%@&naviBy=car&key=7ffc0743945d569cc8501e757af68d25", _locationInfo.lng,_locationInfo.lat,
                     _detail.lng,_detail.lat, _detail.addr  ];
    url = [self clean:url];
    DMWebViewController* web = [[DMWebViewController alloc]initWithTitle:@"路线规划" url:url];
    
    [self.navigationController pushViewController:web animated:YES];
}

-(NSString*)clean:(NSString*)uglyString{
    return [uglyString stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    
}

INIT_BUNDLE_CONTROLLER(BusinessDetailController, ecardlibbundle.bundle)

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
