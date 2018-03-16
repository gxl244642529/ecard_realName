//
//  PersonalController.m
//  ecard
//
//  Created by randy ren on 15/3/26.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//
#import <DMLib/DMLib.h>
#import "PersonalController.h"
#import "BaseImageHandler.h"
#import "CommonMacro.h"
#import "UpdateHeadController.h"
@interface PersonalController ()
{
    __weak IBOutlet UIActivityIndicatorView *indicator;
    //PassportModel* model;
    BaseImageHandler* _headImageHandler;
}
@property (weak, nonatomic) IBOutlet UIButton *btnLogout;
@property (weak, nonatomic) IBOutlet ItemView *btnHead;
@property (weak, nonatomic) IBOutlet ItemView *btnAddress;
@property (weak, nonatomic) IBOutlet ItemView *btnUpdatePassword;
@property (weak, nonatomic) IBOutlet UIImageView *imageHead;
@property (weak, nonatomic) IBOutlet ItemView *btnBind;
@property (weak, nonatomic) IBOutlet UILabel *txtPhone;

@property (weak, nonatomic) IBOutlet UILabel *txtChange;

@end

@implementation PersonalController

INIT_BUNDLE_CONTROLLER(PersonalController, ecardlibbundle.bundle)


-(void)dealloc{
    _headImageHandler = nil;
    
}

- (void)viewDidLoad {
    [super viewDidLoad];
   
    [self setTitle:@"个人信息"];
    
    [_btnHead setTarget:self withAction:@selector(onHead:)];
    [_btnAddress setTarget:self withAction:@selector(onAddress:)];
    [_btnUpdatePassword setTarget:self withAction:@selector(onUpdatePassword:)];
   
   
    _imageHead.layer.masksToBounds = YES;
    _imageHead.layer.cornerRadius = _imageHead.frame.size.width/2;
    AddTapGestureRecognizer(_imageHead, onViewHead:);
     [indicator setHidden:YES];
    
  _headImageHandler = [BaseImageHandler create:@"bg_head_view_girl" localImage:@"head" key:@"headImage" type:1 cornor:YES];
  [_headImageHandler handle:_imageHead];


    /*
    model = [[PassportModel alloc]initWithObserver:self];
   [model setObserver:model.get_bind_phone selector:@selector(onGetBindPhone:)];
   // [indicator startAnimating];
    //[model getBindPhone];
    
     
   UserInfo* userInfo =  [[JsonTaskManager sharedInstance]userInfo];
    
    if(userInfo.userPhone && userInfo.userPhone.length > 0){
        _txtPhone.text = [ NSString stringWithFormat:@"%@****%@",[userInfo.userPhone substringToIndex:3],[userInfo.userPhone substringFromIndex:7]
                          ];
        _txtChange.text = @"换绑";
    }else{
        _txtPhone.text = @"无";
        _txtChange.text = @"绑定";
    }
     [_btnBind setTarget:self withAction:@selector(onChangeBindPhone:)];
     */
}


-(IBAction)onChangeBindPhone:(id)sender{
  //  [model getBindPhone];
}


-(void)onGetBindPhone:(NSString*)phone{
    [SVProgressHUD dismiss];
    if(phone && [phone isKindOfClass:[NSString class]] && phone.length > 0 ){
        _txtPhone.text = [ NSString stringWithFormat:@"%@****%@",[phone substringToIndex:3],[phone substringFromIndex:7]];
    }
   /* if(![[JsonTaskManager sharedInstance].userInfo.userPhone  isEqualToString:phone]){
        [JsonTaskManager sharedInstance].userInfo.userPhone = phone;
        [[DMAccount current]save];
    }
    
    [self openControllerForResult:[[BindPhoneController alloc]initWithPhone:phone] requestCode:0 data:nil modal:NO];
    */
    }


-(void)onViewHead:(id)sender{
    
    ECardUserInfo* userInfo = [DMAccount current];
    
   NSString* headImage = userInfo.headImage;
    if(headImage){
        Push_ViewController(UpdateHeadController);
    }else{
        [self onHead:sender];
    }
}

-(void)onHead:(id)sender{
  
    UIActionSheet *sheet = [[UIActionSheet alloc]initWithTitle:@"选择图片" delegate:self cancelButtonTitle:@"关闭" destructiveButtonTitle:nil otherButtonTitles:@"相册",@"照相机", nil];//关闭按钮在最后

    sheet.actionSheetStyle = UIActionSheetStyleBlackTranslucent;//样式
    [sheet showInView:self.view];//显示样式

}

-(void)editImage:(UIImage*)image{
    [EditorUtil configEditor:1 minWidth:0 minHeight:0];
    [EditorUtil editImage:image parent:self delegate:self title:@"编辑头像" autoEdit:YES];
}


-(void)imageEditorWillFinishEditingWidthImage:(UIImage *)image{
   
 
    
    [_headImageHandler onSaveImage:image];
 
}

- (void)imageEditorDidFinishEdittingWithImage:(UIImage*)image{
    //上传文件
    [_imageHead setImage:image];
  //  _hasHead = YES;
    [[DMNotifier notifier]sendNotification:@"UpdateUserHead" data:image];
}


-(void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex
{
    __weak PersonalController* __self =self;
    if(buttonIndex == 0)
    {
        [ExternalUtil selectFromAlbum:self completion:^(UIImage * image) {
            [__self editImage:image];
        }];
    } else if(buttonIndex == 1)
    {
        [ExternalUtil captureImage:self completion:^(UIImage * image) {
            [__self editImage:image];
        }];
    }
    
}





OVERRIDE
-(void)onControllerResult:(NSInteger)resultCode requestCode:(NSInteger)requestCode data:(NSString*)phone{
   /* if(resultCode==RESULT_OK){
        _txtPhone.text = [ NSString stringWithFormat:@"%@****%@",[phone substringToIndex:3],[phone substringFromIndex:7]
                          ];
        _txtChange.text = @"换绑";
        [_btnBind setTarget:self withAction:@selector(onChangeBindPhone:)];
    }*/
}



-(void)onAddress:(id)sender{
    Push_ViewController(AddressController);
}

-(void)onUpdatePassword:(id)sender{
  Class clazz = NSClassFromString(@"UpdatePasswordController");
  [self.navigationController pushViewController:[[clazz alloc]init] animated:YES];

  //  Push_ViewController(UpdatePasswordController);
}


// Called when a button is clicked. The view will be automatically dismissed after this call returns
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    if(buttonIndex==1){
        [ECardUserInfo logout];
        [self finish];
    }
 }
- (IBAction)onLogout:(id)sender {
     [Alert confirm:@"注销以后将不能查看和绑定e通卡信息，您确定要注销吗?" yesButton:@"确定" cancelButton:@"取消" delegate:self withTitle:@"注销确认"];
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
