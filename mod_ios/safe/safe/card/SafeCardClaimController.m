//
//  SafeCardClaimController.m
//  ecard
//
//  Created by randy ren on 16/1/24.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "SafeCardClaimController.h"
#import "RealInfo.h"

@interface SafeCardClaimController ()
@property (weak, nonatomic) IBOutlet DMFormTextField *txtIdCard;
@property (weak, nonatomic) IBOutlet DMItem *btnShowCard;

@end

@implementation SafeCardClaimController

@synthesize data=_data;
INIT_BUNDLE_CONTROLLER(SafeCardClaimController, safebundle.bundle)
- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"填写报失信息"];
    [_btnShowCard setTarget:self withAction:@selector(onShowCard:)];
}


-(void)onShowCard:(id)sender{
    
    
    DMWebViewController* c = [[DMWebViewController alloc]initWithTitle:@"卡样展示" url:    _data.sampleUrl];

    [self.navigationController pushViewController:c animated:YES];
    
}

/**
 
 */
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}


API_JOB_SUCCESS(user, realInfo, RealInfo*){
    _txtIdCard.text = @"";
}

-(BOOL)submit:(id<DMSubmit>)submit validate:(NSMutableDictionary*)data{
    data[@"orderId"] = _data.orderId;
    return YES;
}

//i_m_safe/lost

API_JOB_SUCCESS(i_m_safe, lost, id){
     __weak typeof(self) __self =self;
    [Alert alert:@"报失成功" title:@"温馨提示" dialogListener:^(NSInteger buttonIndex) {
       
        [__self finish];
    }];
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
