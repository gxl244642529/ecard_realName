//
//  RechargeSpotController.m
//  ecard
//
//  Created by 任雪亮 on 16/3/2.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "RechargeSpotController.h"
#import "RechargePointVo.h"
#import <amap/amap.h>

@interface RechargeSpotController ()
@property (weak, nonatomic) IBOutlet DMStateList *tableView;
@property (nonatomic,retain) DMLocationInfo* info;
@end

@implementation RechargeSpotController
-(void)dealloc{
    _info = nil;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"卟噔点"];
    
    [_tableView setOnItemClickListener:self];
    
    __weak RechargeSpotController* __self = self;
    
    [DMMapUtil getPosition:^(DMLocationInfo * info) {
        __self.info = info;
      //  [_tableView put:@"lat" value:WRAP_DOUBLE(info.lat)];
       // [_tableView put:@"lng" value:WRAP_DOUBLE(info.lng)];
        //[_tableView execute];
    }];
}

-(NSString*)clean:(NSString*)uglyString{
    return   [uglyString stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    
}
-(void)onItemClick:(UIView*)parent data:(RechargePointVo*)data index:(NSInteger)index{
    if(!_info)
    {
        return;
    }
    NSString* url = [NSString stringWithFormat:@"http://m.amap.com/navi/?start=%f,%f&dest=%f,%f&destName=%@&naviBy=car&key=7ffc0743945d569cc8501e757af68d25", _info.lng,_info.lat,
                     data.lng,data.lat, data.address  ];
    url = [self clean:url];
    DMWebViewController* web = [[DMWebViewController alloc]initWithTitle:@"路线规划" url:url];
    
    [self.navigationController pushViewController:web animated:YES];

    
    
}


INIT_BUNDLE_CONTROLLER(RechargeSpotController,rechargebundle.bundle)

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
