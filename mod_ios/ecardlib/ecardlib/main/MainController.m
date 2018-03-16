
#import "MainController.h"
#import "ViewUtil.h"
#import "EditorUtil.h"
#import <DMLib/dmlib.h>
#import "ImageUtil.h"
#import "MainPane.h"
#import "MainView.h"
#import "NewsController.h"
#import "MineController.h"
#import "LicaiController.h"
#import "QuestionController.h"
#import "NetController.h"
#import "BusinessMainController.h"
#import "PinganController.h"
#import "YuanxinController.h"
#import "ECardUserInfo.h"
#import "ECardCaller.h"
#import "BaseImageHandler.h"


#define is_first_run @"is_first_run1"


@interface MainController ()
{
    WelcomeView* _welcome;
  BaseImageHandler* _imageHandler;
    DMLoginHandler* handler;
     BaseImageHandler* _headImageHandler;
    
    __weak UIImageView* _wallImageView;
}
@property (weak, nonatomic) IBOutlet DMAdvView *advView;

@end

@implementation MainController

INIT_BUNDLE_CONTROLLER(MainController, ecardlibbundle.bundle)

-(void)dealloc{
    _imageHandler = nil;
    _headImageHandler = nil;
    handler = nil;
    _welcome = nil;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"e通卡"];
    self.navigationItem.leftBarButtonItem = nil;
    MainView* mainView = (MainView*)self.view;
    [mainView updateMargin];
    MainPane* pane = mainView.mainPane;
    
    Control_AddTarget(pane.btnQuestion, onQuestion);
    Control_AddTarget(pane.btnBusiness, onBusiness);
    Control_AddTarget(pane.btnNet, onNet);
    Control_AddTarget(pane.btnBack, onGoodCard);
    AddTapGestureRecognizer(mainView.headBg, onTapBg:);
    
    UIButton* button = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, 20, 20)];
    handler = [[DMLoginHandler alloc]initWithButton:button parent:self];
   // handler.controllerClass = [MessageController class];
    [button setImage:[UIImage imageNamed:@"ic_title_msg.png"] forState:UIControlStateNormal];
    UIBarButtonItem* item = [[UIBarButtonItem alloc]initWithCustomView:button];
    self.navigationItem.rightBarButtonItem = item;
    
  //[_advView awakeFromNib];
  
    
    mainView.headView.userInteractionEnabled = YES;
    
    AddTapGestureRecognizer(mainView.headView, onPersonal  );
    
   button= [self.view viewWithTag:1000];
    Control_AddTarget(button, onYuanxin);
    
    _headImageHandler = [BaseImageHandler create:@"bg_head_view_girl" localImage:@"head" key:@"headImage" type:1 cornor:YES];
    [_headImageHandler handle:  mainView.headView];
    
    
    
    NSUserDefaults* def = [NSUserDefaults standardUserDefaults];
    id value = [def valueForKey:is_first_run];
    if(!value){
        _welcome = [[WelcomeView alloc]initWithFrame:[UIScreen mainScreen].bounds];
        UIWindow *appWindow = [[UIApplication sharedApplication] keyWindow];
        [appWindow addSubview:_welcome];
        _welcome.delegate = self;
    }
    
    _wallImageView = mainView.headBg
    ;

    mainView.bgButton.userInteractionEnabled = YES;
    UITapGestureRecognizer* ges = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(onTapHead:)];
    [mainView.bgButton addGestureRecognizer:ges];
    
    
    _wallImageView = mainView.headBg;
    
    
    [self onLoginStateChange:nil];
}


ON_LOGIN_SUCCESS{
    [self onLoginStateChange:nil];
}

ON_LOGOUT{
    [self onLoginStateChange:nil];
}
-(void)onTapHead:(UITapGestureRecognizer*)sender{
    
    UIActionSheet *sheet = [[UIActionSheet alloc]initWithTitle:@"选择图片" delegate:self cancelButtonTitle:@"关闭" destructiveButtonTitle:nil otherButtonTitles:@"相册",@"拍照", nil];//关闭按钮在最后
    
    sheet.actionSheetStyle = UIActionSheetStyleAutomatic;//样式
    [sheet showInView:self.view];//显示样式
    
    
}



-(void)editWallpaper:(UIImage*)image{
    [EditorUtil configEditor:_wallImageView.frame.size.height / _wallImageView.frame.size.width minWidth:0 minHeight:0];
    [EditorUtil editImage:image parent:self delegate:self title:@"编辑墙纸" autoEdit:YES];
}



// Called when a button is clicked. The view will be automatically dismissed after this call returns
- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex{
    
    __weak MainController* __self = self;
    switch (buttonIndex) {
        case 0:
        {
            [ExternalUtil selectFromAlbum:self completion:^(UIImage * image) {
                [__self editWallpaper:image];
            }];
            
        }
            break;
        case 1:
        {
            [ExternalUtil captureImage:self completion:^(UIImage * image) {
                [__self editWallpaper:image];
            }];
        }
            break;
    }
}




-(void)onLoginStateChange:(id)sender{
    
    MainView* mainView = (MainView*)self.view;
    
    _imageHandler = [BaseImageHandler create:@"bg_main_adv" localImage:@"wallpaper" key:@"bg" type:0 cornor:NO];
    [_imageHandler handle:_wallImageView];
    
    _headImageHandler = [BaseImageHandler create:@"bg_head_view_girl" localImage:@"head" key:@"headImage" type:1 cornor:YES];
    [_headImageHandler handle:mainView.headView];
}



-(void)onWelcomeComplete{
    NSUserDefaults* def = [NSUserDefaults standardUserDefaults];
    [def setValue:@TRUE forKey:is_first_run];
    [UIView animateWithDuration:0.3 animations:^{
        _welcome.alpha = 0.1;
    } completion:^(BOOL finished) {
        [_welcome removeFromSuperview];
        _welcome = nil;
    }];
    
}

ON_NOTIFICATION(UpdateUserHead, UIImage*){
      MainView* mainView = (MainView*)self.view;
    [mainView.headView setImage:data];
}

API_JOB_SUCCESS(user, head, NSString*){
    ECardUserInfo* info = [DMAccount current];
    info.headImage = result;
    [info save];
}
API_JOB_SUCCESS(user_api, set_bg, NSString*){
    ECardUserInfo* info = [DMAccount current];
    info.bg = result;
    [info save];
}
-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
  

   
}

-(void)onYuanxin:(id)sender{
    if(! [ DMAccount isLogin]){
        [DMAccount callLoginController:nil];
    }else{
        DMAccount* account = [DMAccount current];
        NSString* url = [NSString stringWithFormat:@"http://www.cczcc.net/index.php/yuanxin/index/%@", account.userID ];
        YuanxinController* c= [[YuanxinController alloc]initWithTitle:@"圆信永丰" url:url];
        [self.navigationController pushViewController:c animated:YES];
    }

}
- (IBAction)onSub4:(id)sender {
    
    
  //  [ECardCaller callStudent];
    
    
}


-(void)imageEditorDidFinishEdittingWithImage:(UIImage*)image{
 
    MainView* mainView = (MainView*)self.view;
    
    [mainView.headBg setImage:image];

    

}
-(void)imageEditorWillFinishEditingWidthImage:(UIImage*)image{
     [self saveWallpaper:image];
}

/**
 保存
 */
-(void)saveWallpaper:(UIImage*)image{
    [_imageHandler onSaveImage:image];
}
-(void)onTapBg:(id)sender{
     __weak MainController* __self = self;
    [ExternalUtil showActionSheet:self completion:^(UIImage * image) {
        [__self onShowImage:image];
    }];
}

-(void)onShowImage:(UIImage*)image{
    MainView* mainView = (MainView*)self.view;
    //编辑
    [EditorUtil configEditor:0.8 minWidth:mainView.headBg.frame.size.width minHeight:mainView.headBg.frame.size.height];
    [EditorUtil editImage:image parent:self delegate:self title:@"编辑背景" autoEdit:YES];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}




#pragma event

- (IBAction)onLicai:(id)sender {
    Push_ViewController(LicaiController);
    
 }

- (IBAction)onBus:(id)sender {
    //掌上公交
     DMWebViewController* controller = [[DMWebViewController alloc]initWithTitle:@"公交线路查询" url:@"http://m.doudou360.com/bus/Index.aspx?area=xiamen&partner=etongka.com"];
    [self.navigationController pushViewController:controller animated:YES];
}

- (IBAction)onMovie:(id)sender {
    Class clazz = NSClassFromString(@"SafeController");
    [self.navigationController pushViewController:[[clazz alloc]init] animated:YES];
}

- (IBAction)onSafe:(id)sender {
    if(! [ DMAccount isLogin]){
        [DMAccount callLoginController:nil];
    }else{
        DMAccount* account = [DMAccount current];
        NSString* url = [NSString stringWithFormat:@"http://www.cczcc.net/index.php/pingan/index/%@", account.userID ];
        PinganController* c= [[PinganController alloc]initWithTitle:@"平安之家" url:url];
        [self.navigationController pushViewController:c animated:YES];
    }

  
}

- (IBAction)onRecharge:(id)sender {
  Class clazz = NSClassFromString(@"RechargeController");
  [self.navigationController pushViewController:[[clazz alloc]init] animated:YES];

}

- (IBAction)onShop:(id)sender {
     Class clazz = NSClassFromString(@"SellingMainController");
    
    [self.navigationController pushViewController:[[clazz alloc]init] animated:YES];
}
- (IBAction)onECard:(id)sender {
  Class clazz = NSClassFromString(@"ECardController");
  
  [self.navigationController pushViewController:[[clazz alloc]init] animated:YES];
}
- (IBAction)onStudent:(id)sender {
  //  [ECardCaller callStudent];
}


-(void)onGoodCard:(id)sender{
    
    
    
    Class clazz = NSClassFromString(@"GoodPersonalController");
    
    [self.navigationController pushViewController:[[clazz alloc]init] animated:YES];
}

- (IBAction)onNews:(id)sender {
    Push_ViewController(NewsController);
}

- (IBAction)onPersonal {
    Push_ViewController(MineController);
}
- (IBAction)onQuestion:(id)sender {
    Push_ViewController(QuestionController);
}
- (IBAction)onBusiness:(id)sender {
     Push_ViewController(BusinessMainController);
}
- (IBAction)onNet:(id)sender {
     Push_ViewController(NetController);
}



@end
 

