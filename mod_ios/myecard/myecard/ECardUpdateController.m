//
//  ECardUpdateController.m
//  ecard
//
//  Created by randy ren on 16/2/3.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "ECardUpdateController.h"

@interface ECardUpdateController ()

@end

@implementation ECardUpdateController


@synthesize data = _data;

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"修改备注"];
}


INIT_BUNDLE_CONTROLLER(ECardUpdateController,myecardbundle.bundle)

-(void)submit:(id<DMSubmit>)submit completeData:(NSMutableDictionary*)data{
    
    [data setValue:_data.cardId forKey:@"cardId"];
    
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    [self.navigationController popViewControllerAnimated:YES];
}
API_JOB_SUCCESS(ecard, update, id){
    _data.cardName = result;
    [Alert alert:@"修改成功" delegate:self];
    
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
