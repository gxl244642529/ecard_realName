//
//  MineController.m
//  ecard
//
//  Created by randy ren on 15/3/26.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "MineController.h"
#import "ItemView.h"
#import "CommonMacro.h"
#import "SettingController.h"

@interface MineController ()
{
   
    
    __weak IBOutlet ItemView *_btnMyMessage;
    BOOL _error;
}
@property (weak, nonatomic) IBOutlet ItemView *mySafe;
@property (weak, nonatomic) IBOutlet ItemView *btnMyECard;
@property (weak, nonatomic) IBOutlet ItemView *btnMyCollect;
@property (weak, nonatomic) IBOutlet ItemView *btnGame;
@property (weak, nonatomic) IBOutlet ItemView *btnSetting;
@property (weak, nonatomic) IBOutlet UIView *notLoginPane;
@property (weak, nonatomic) IBOutlet TouchableView *loginView;
@property (weak, nonatomic) IBOutlet UILabel *txtAccount;
@property (weak, nonatomic) IBOutlet UIImageView *headView;
@property (weak, nonatomic) IBOutlet ItemView *btnOrder;

@end

@implementation MineController

INIT_BUNDLE_CONTROLLER(MineController, ecardlibbundle.bundle)


ON_EVENT(onViewPersonal){
    
    Class clazz = NSClassFromString(@"PersonalController");
    [self.navigationController pushViewController:[[clazz alloc]init] animated:YES];
    
    //Push_ViewController();
    
}


- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = RGB_WHITE(f2);
    [self setTitle:@"我的"];
    
    _headView.userInteractionEnabled = YES;
    AddTapGestureRecognizer(_headView,onLogin:);
    [_loginView setLoginTarget:self withAction:@selector(onViewPersonal:)];
    [_btnMyECard setLoginTarget:self withAction:@selector(onMyECard:)];
    [_btnSetting setTarget:self withAction:@selector(onSetting:)];
    [_btnOrder setLoginTarget:self withAction:@selector(onAllOrder:)];
    [_btnMyCollect setLoginTarget:self withAction:@selector(onCollect:)];
    [_btnGame setTarget:self withAction:@selector(onAction:)];
    
    [_mySafe setTarget:self withAction:@selector(onMySafe:)];
    
  
    
    [_btnMyMessage setTarget:self withAction:@selector(onMyMessage:)];
}

-(void)onMyMessage:(id)sender{
   // Push_ViewController(MyMessageController);
}

ON_EVENT(onMySafe){
    
    Class clazz = NSClassFromString(@"MySafeController");
    [self.navigationController pushViewController:[[clazz alloc]init] animated:YES];
}

ON_EVENT(onAction){
    
   // Push_ViewController(EventController);
    
}

ON_EVENT(onCollect){
    Class clazz = NSClassFromString(@"SCollectionController");
    [self.navigationController pushViewController:[[clazz alloc]init] animated:YES];
   
}





-(void)onSetUserInfo:(DMAccount*)userInfo{
    if(userInfo){
        _notLoginPane.hidden = YES;
        _loginView.hidden = NO;
        _txtAccount.text = userInfo.userAccount;
    }else{
       
        _notLoginPane.hidden = NO;
        _loginView.hidden = YES;
    }
    

}
-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [self onSetUserInfo:[DMAccount current]];
    
}

ON_EVENT(onMyECard){
    
    Class clazz = NSClassFromString(@"ECardController");
    [self.navigationController pushViewController:[[clazz alloc]init] animated:YES];
}



-(void)onAllOrder:(id)sender{
    Class clazz = NSClassFromString(@"SOrderListController");
    id controller = [[clazz alloc]init];
    [controller setValue:@-1 forKey:@"state"];
    [self.navigationController pushViewController:controller animated:YES];
 /*   SOrderListController* controller = [[SOrderListController alloc]init];
    controller.state =-1;
    [self.navigationController pushViewController:controller animated:YES];
 */   
}


-(void)onSetting:(id)sender{
    
    [self.navigationController pushViewController:[[SettingController alloc]init] animated:YES];
    
}

- (IBAction)onLogin:(id)sender {
     [DMAccount callLoginController:nil];
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
