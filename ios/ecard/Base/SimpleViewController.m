//
//  SimpleViewController.m
//  randycommonlibs
//
//  Created by randy ren on 14-9-22.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "SimpleViewController.h"

@interface SimpleViewController ()
{
    __weak NSObject<IViewControllerListener>* _listener;
}
@end

@implementation SimpleViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

-(void)dealloc{
    [_listener onDealloc:self];
}


-(id)initWithListener:(NSObject<IViewControllerListener>*)listener{
    if(self = [super init]){
        _listener = listener;
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    [_listener onViewLoaded:self];
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
