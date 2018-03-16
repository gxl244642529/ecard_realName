//
//  NetController.m
//  ecard
//
//  Created by randy ren on 16/2/22.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "NetController.h"
#import <dmlib/dmlib.h>

#import "NetPotController.h"

@interface NetController ()
@property (weak, nonatomic) IBOutlet DMItem *po1;
@property (weak, nonatomic) IBOutlet DMItem *po2;
@property (weak, nonatomic) IBOutlet DMItem *po3;
@property (weak, nonatomic) IBOutlet DMItem *po4;

@end

@implementation NetController

INIT_BUNDLE_CONTROLLER(NetController, ecardlibbundle.bundle)
- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"网点查询"];
    
    [_po1 setTarget:self withAction:@selector(onPoi:)];
    [_po2 setTarget:self withAction:@selector(onPoi:)];
    [_po3 setTarget:self withAction:@selector(onPoi:)];
    [_po4 setTarget:self withAction:@selector(onPoi:)];
    
}

-(void)onPoi:(UIView*)sender{
    
    NetPotController* c = [[NetPotController alloc]init];
    c.data = [NSNumber numberWithInt:sender.tag];
    
    [self.navigationController pushViewController:c animated:YES];
    
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
