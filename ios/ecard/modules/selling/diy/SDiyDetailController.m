//
//  SDiyDetailController.m
//  ecard
//
//  Created by randy ren on 15/5/13.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "SDiyDetailController.h"
#import "SellingModel.h"
#import "NetworkImage.h"

#import <ecardlib/ecardlib.h>

@interface SDiyDetailController ()
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *viewHeight;
@property (weak, nonatomic) IBOutlet NetworkImage *image;
@property (weak, nonatomic) IBOutlet UILabel *diyTitle;

@end

@implementation SDiyDetailController

-(void)updateViewConstraints{
    [super updateViewConstraints];
     _viewHeight.constant =  ([UIScreen mainScreen].bounds.size.width - 20 ) * CARD_HEIGHT  / CARD_WIDTH;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"DIY详情"];
    _image.layer.masksToBounds = YES;
    _image.layer.cornerRadius = 8;
    [_image load:[Constants formatUrl:self.data[@"IMG_Z"]]];
    _diyTitle.text = self.data[@"TITLE"];
    
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
