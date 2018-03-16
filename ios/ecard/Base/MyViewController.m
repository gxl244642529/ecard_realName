//
//  MyViewController.m
//  ecard
//
//  Created by randy ren on 14-9-24.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "MyViewController.h"
#import "NavBarItem.h"


#import <ecardlib/ecardlib.h>

@interface MyViewController ()

@end

@implementation MyViewController



- (void)viewDidLoad
{
    [super viewDidLoad];
    self.view.autoresizesSubviews = NO;
    [self.view setBackgroundColor:[ColorUtil defaultPageColor]];
}


-(void)finish{
  [self backToPrevious:nil];
}

-(void)backToPrevious:(id)sender{
  [self.controllerDelegate controllerWillFinish:self];
  [self.navigationController popViewControllerAnimated:YES];
}

-(id)initWithData:(id)data{
    if(self = [super init]){
        self.data = data;
    }
    return self;
}

-(UIButton*)createTitleButton:(NSString*)title width:(NSInteger)width{
    UIButton* addButton = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, width,22)];
    [addButton setTitle:title forState:UIControlStateNormal];
    [addButton.titleLabel setFont:[UIFont systemFontOfSize:13]];
    [addButton setTitleColor:[UIColor darkGrayColor] forState:UIControlStateNormal];
    self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc]initWithCustomView:addButton];
    return addButton;
}

-(UIButton*)createTitleButton:(NSString*)title{
    UIButton* addButton = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, 30,22)];
    [addButton setTitle:title forState:UIControlStateNormal];
    [addButton.titleLabel setFont:[UIFont systemFontOfSize:13]];
    [addButton setTitleColor:[UIColor darkGrayColor] forState:UIControlStateNormal];
    self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc]initWithCustomView:addButton];
    return addButton;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
