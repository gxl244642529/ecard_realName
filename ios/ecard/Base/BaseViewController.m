//
//  BaseViewController.m
//  randycommonlibs
//
//  Created by randy ren on 14-9-21.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "BaseViewController.h"
#import "MyNavigationController.h"
#import "NavBarItem.h"
#import <ecardlib/ecardlib.h>
@interface BaseViewController ()
{
    BOOL _modal;
     __weak BaseViewController* _requestController;
}
@end

@implementation BaseViewController
-(BOOL)isModal{
    return _modal;
}
- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

-(void)dealloc{
}


- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}



-(void)backToPrevious:(id)sender
{
    if(_requestController){
        [self setResult:RESULT_CANCEL data:NULL];
    }else{
        [self.navigationController popViewControllerAnimated:YES];
    }
    
}

-(void)openControllerForResult:(BaseViewController*)controller requestCode:(NSInteger)requestCode data:(NSObject*)data modal:(BOOL)modal
{
    [controller setRequestController:self requestCode:requestCode data:data modal:modal];
    if(modal)
    {
        [self openModalController:controller];
    }else{
        [self.navigationController pushViewController:controller animated:YES];
    }
}


-(void)openModalController:(UIViewController*)controller{
    /*UINavigationController* nav = [[UINavigationController alloc]initWithRootViewController:controller];
    [self.navigationController presentViewController:nav animated:YES completion:NULL];*/
    MyNavigationController* nav = [[MyNavigationController alloc]initWithRootViewController:controller];
    [self.navigationController presentViewController:nav animated:YES completion:NULL];

}
-(void)openController:(BaseViewController*)controller data:(NSObject *)data{
    controller.data = data;
    [self.navigationController pushViewController:controller animated:YES];
}

-(void)setRequestController:(BaseViewController*)controller requestCode:(NSInteger)requestCode data:(NSObject *)data modal:(BOOL)modal
{
    _modal = modal;
    _requestController = controller;
    _requstCode = requestCode;
    self.data = data;
}
-(void)setResult:(NSInteger)resultCode data:(NSObject *)data animate:(BOOL)animate{
    if(_modal)
    {
        [self.navigationController dismissViewControllerAnimated:animate completion:^{
            [self removeFromParentViewController];
        }];
    }else{
        [self.navigationController popViewControllerAnimated:animate];
    }
    if(_requestController){
        [_requestController onControllerResult:resultCode requestCode:_requstCode data:data];
    }

}
-(void)setResult:(NSInteger)resultCode data:(NSObject *)data
{
    [self setResult:resultCode data:data animate:YES];
}
-(void)onControllerResult:(NSInteger)resultCode requestCode:(NSInteger)requestCode data:(NSObject *)data
{
    
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
