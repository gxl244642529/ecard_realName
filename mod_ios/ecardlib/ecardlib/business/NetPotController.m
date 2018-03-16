//
//  NetPotController.m
//  ecardlib
//
//  Created by 任雪亮 on 17/1/18.
//  Copyright © 2017年 任雪亮. All rights reserved.
//

#import "NetPotController.h"

#import <amap/amap.h>




@interface NetPotController ()
{
    
}
@property (weak, nonatomic) IBOutlet DMStateList *tableView;

@property (nonatomic,retain) DMLocationInfo* info;


@end

@implementation NetPotController


-(void)dealloc{
    _info = nil;
}

INIT_BUNDLE_CONTROLLER(NetPotController, ecardlibbundle.bundle)
- (void)viewDidLoad {
    [super viewDidLoad];
    int tag = [self.data intValue];
    NSString* str = nil;
    switch (tag) {
        case 0:
            str = @"消费点";
            break;
        case 1:
            str = @"充值点";
             break;
        case 2:
            str= @"售卡点";
             break;
        default:
            str = @"服务网点";
            break;
    }
    [self setTitle:str];
    [_tableView put:@"tag" value:str];
    [_tableView setOnItemClickListener:self];
    
    __weak NetPotController* __self = self;
    
    [DMMapUtil getPosition:^(DMLocationInfo * info) {
        __self.info = info;
        [_tableView put:@"lat" value:WRAP_DOUBLE(info.lat)];
        [_tableView put:@"lng" value:WRAP_DOUBLE(info.lng)];
        [_tableView put:@"distance" value:@3000];
        [_tableView execute];
    }];
    
}



-(NSString*)clean:(NSString*)uglyString{
    return [uglyString stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    
}
-(void)onItemClick:(UIView*)parent data:(NSDictionary*)data index:(NSInteger)index{
    
    
    
    NSString* url = [NSString stringWithFormat:@"http://m.amap.com/navi/?start=%f,%f&dest=%@,%@&destName=%@&naviBy=car&key=7ffc0743945d569cc8501e757af68d25", _info.lng,_info.lat,
                     data[@"location"][0],data[@"location"][1], data[@"address"]  ];
    url = [self clean:url];
    DMWebViewController* web = [[DMWebViewController alloc]initWithTitle:@"路线规划" url:url];
    
    [self.navigationController pushViewController:web animated:YES];

    
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
