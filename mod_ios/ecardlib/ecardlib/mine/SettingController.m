//
//  SettingController.m
//  eCard
//
//  Created by randy ren on 14-1-27.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "SettingController.h"
#import "ItemView.h"
#import "FeedbackController.h"
#import "Constants.h"
#import "SpecialTermController.h"
#import "WelcomeController.h"

#import <ecardlib/ecardlib.h>
#import <dmlib/dmlib.h>

@interface SettingController ()
{
 
    
}
    
    @property (weak, nonatomic) IBOutlet UILabel *versonCode;
    @property (weak, nonatomic) IBOutlet ItemView *btnContact;
    
    
@property (weak, nonatomic) IBOutlet UILabel *txtSize;
@property (weak, nonatomic) IBOutlet ItemView *btnClearCache;
@property (weak, nonatomic) IBOutlet ItemView *btnFeedback;

@property (weak, nonatomic) IBOutlet ItemView *btnHelp;
@property (weak, nonatomic) IBOutlet UILabel *phoneNumber;

    
@property (weak, nonatomic) IBOutlet ItemView *btnSpecial;
@property (weak, nonatomic) IBOutlet ItemView *btnAbout;

@end

@implementation SettingController

-(void)dealloc{
    [SVProgressHUD dismiss];
}
INIT_BUNDLE_CONTROLLER(SettingController, ecardlibbundle.bundle)
- (void)viewDidLoad
{
    [super viewDidLoad];
    self.view.backgroundColor = RGB_WHITE(f2);
    [self setTitle:@"设置"];
    [_btnClearCache setTarget:self withAction:@selector(onClear:)];
    [_btnFeedback setTarget:self withAction:@selector(onFeedback:)];
    [_btnContact setTarget:self withAction:@selector(onContact:)];
    [_btnHelp setTarget:self withAction:@selector(onHelp:)];
    [_btnSpecial setTarget:self withAction:@selector(onSpecial:)];
    [_btnAbout setTarget:self withAction:@selector(onAbout:)];
    _versonCode.text = [DMConfigReader getString:@"version"];
   self.txtSize.text = @"";
/*
    float tmpSize = [[ECardTaskManager sharedInstance]getCacheSize];
    NSString *clearCacheName = tmpSize >= 1 ? [NSString stringWithFormat:@"%.2fM",tmpSize] : [NSString stringWithFormat:@"%.2fK",tmpSize * 1024];
    self.txtSize.text = clearCacheName;*/
}



-(void)onAbout:(id)sender{
    
   [CommonUtil makePhoneCall:self.phoneNumber.text parent:self.view];
    
    
}


-(void)onClear:(id)sender{
    [SVProgressHUD showWithStatus:@"请稍等..."];
    dispatch_async(GLOBAL_QUEUE, ^{
        [[DMJobManager sharedInstance]clearCache];
        dispatch_async(dispatch_get_main_queue(), ^{
            [SVProgressHUD showSuccessWithStatus:@"清理缓存成功"];
        });
    });
    
}


-(void)onFeedback:(id)sender{
    Push_ViewController(FeedbackController);
}

-(void)onSpecial:(id)sender{
    Push_ViewController(SpecialTermController);
}

-(void)onContact:(id)sender{
    [CommonUtil makePhoneCall:self.phoneNumber.text parent:self.view];
}

-(void)onHelp:(id)sender{
    
    DMWebViewController* controller = [[DMWebViewController alloc]initWithTitle:@"帮助" url:
                                       [DMServers formatUrl:0 url:@"/index.php/api2/helper/index"]];
    [self.navigationController pushViewController:controller animated:YES];
 // DMWebActivity.openUrl(this,DMServers.getImageUrl(0, "/index.php/api2/helper/index") , "帮助");
}


@end
