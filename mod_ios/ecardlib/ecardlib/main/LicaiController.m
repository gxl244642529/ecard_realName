//
//  LicaiController.m
//  ecard
//
//  Created by randy ren on 16/2/4.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "LicaiController.h"

@implementation LicaiController

-(void)viewDidLoad{
    [super viewDidLoad];
    [self setTitle:@"惠民理财"];
    [self load:@"http://www.xmecard.com:8092/index.php/c_licai/index"];
    
}

@end
