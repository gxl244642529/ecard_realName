

//
//  AddressEditController.m
//  ecard
//
//  Created by randy ren on 16/1/16.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "AddressEditController.h"

@interface AddressEditController ()

@end

@implementation AddressEditController
@synthesize data=_data;



INIT_BUNDLE_CONTROLLER(AddressEditController, ecardlibbundle.bundle)

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"编辑收货地址"];
    UIView* view = self.view;
    self.view.backgroundColor = RGB_WHITE(f2);
}

-(void)submit:(id<DMSubmit>)submit completeData:(NSMutableDictionary*)data{
    if(_data){
        data[@"id"]=[NSNumber numberWithInteger:_data.id];
        data[@"tyId"] = [NSNumber numberWithInteger:_data.tyId];
    }
    
}
- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

API_JOB_SUCCESS(address, save, id){
    
    [self.navigationController popViewControllerAnimated:YES];
    
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
