//
//  UpdateHeadController.m
//  ecard
//
//  Created by randy ren on 15/4/4.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "UpdateHeadController.h"
#import "ECardUserInfo.h"


@interface UpdateHeadController ()
@property (weak, nonatomic) IBOutlet UIImageView *headImage;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *imageHeight;

@end

@implementation UpdateHeadController


-(id)init{
    if(self = [super initWithNibName:@"UpdateHeadController" bundle:CREATE_BUNDLE(ecardlibbundle.bundle)]){
        
    }
    return self;
}




-(void)updateViewConstraints{
    [super updateViewConstraints];
    _imageHeight.constant = 280 * self.view.frame.size.width/320;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"头像"];

    ECardUserInfo* userInfo = [DMAccount current];
    
    NSString* str = userInfo.headImage;
    
    if([str hasPrefix:@"/upload"]){
        str = [DMServers formatUrl:0 url:str];
    }
    
    [[DMJobManager sharedInstance]loadImage:str image:_headImage];
    
   /* [[JsonTaskManager sharedInstance]setImageSrcSync:_headImage src:[JsonTaskManager sharedInstance].userInfo.headImage];*/
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
