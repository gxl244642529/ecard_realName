//
//  SpecialTermController.m
//  eCard
//
//  Created by randy ren on 14-3-6.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "SpecialTermController.h"
#import "ColorUtil.h"

@interface SpecialTermController ()

@end

@implementation SpecialTermController

INIT_BUNDLE_CONTROLLER(SpecialTermController, ecardlibbundle.bundle)

- (void)viewDidLoad
{
    [super viewDidLoad];
    [self setTitle:@"特别声明"];
    self.view.backgroundColor = [UIColor whiteColor];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
