//
//  PickCardDetailController.m
//  ecard
//
//  Created by randy ren on 14-5-24.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "PickCardDetailController.h"


@interface PickCardDetailController ()
@property (weak, nonatomic) IBOutlet UILabel *txtCardNumber;
@property (weak, nonatomic) IBOutlet UILabel *txtPickTime;
@property (weak, nonatomic) IBOutlet UIButton *btnOk;

@end

@implementation PickCardDetailController


- (IBAction)onCallPicker:(id)sender {
    NSString* phone = [self.data valueForKey:@"PHONE"];
    [CommonUtil makePhoneCall:phone parent:self.view];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self setTitle:@"拾卡详情"];
    self.txtCardNumber.text = [self.data valueForKey:@"CARD_ID"];
    self.txtPickTime.text = [self.data valueForKey:@"TIME"];
 
    [ViewUtil setButtonBg:_btnOk];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
