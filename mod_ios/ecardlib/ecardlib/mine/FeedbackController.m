//
//  FeedbackController.m
//  ecard
//
//  Created by randy ren on 15/3/31.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "FeedbackController.h"
#import "ViewUtil.h"
@interface FeedbackController ()
@property (weak, nonatomic) IBOutlet UIButton *btnSubmit;

@end

@implementation FeedbackController
INIT_BUNDLE_CONTROLLER(FeedbackController, ecardlibbundle.bundle)
-(void)dealloc{
    [SVProgressHUD dismiss];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"意见"];
    self.view.backgroundColor = RGB_WHITE(f2);
}


API_JOB_SUCCESS(feedback, add, id){
    
    [Alert alert:nil title:@"提交成功" dialogListener:^(NSInteger buttonIndex) {
        [self finish];
    }];
    
}

- (IBAction)onSubmit:(id)sender {
   
}
-(void)onSubmitReal:(id)sender{
    
 
    
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
