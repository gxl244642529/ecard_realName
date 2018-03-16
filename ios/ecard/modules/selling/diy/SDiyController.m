//
//  SDiyController.m
//  ecard
//
//  Created by randy ren on 15/3/29.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//
#import <ecardlib/ecardlib.h>
#import "SDiyController.h"
#import "SellingModel.h"
#import "SizeUtil.h"
#import "SaleUtil.h"
#import "FileArrayJsonTask.h"
#import "CardModel.h"
#import <DMLib/DMLib.h>

#import "AddToCartView.h"
#import "CardModel.h"
#import "SDiySelImageController.h"
#import "NetworkImage.h"
#import "ShareModel.h"


@interface SDiyController ()
{
    __weak FileArrayJsonTask* _model;
    DiyVo* _currentData;
    ObjectJsonTask* _cartTask;
    __weak IBOutlet UILabel *_fprice;
    __weak IBOutlet UILabel *_ftitle;
    ShareModel* _shareModel;
    __weak IBOutlet ItemView *_btnShare;
    
    ObjectJsonTask* _stockTask;
    BOOL _loadingStock;
    
}
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *bottomHeight;
@property (weak, nonatomic) IBOutlet UILabel *txtLabel;
@property (weak, nonatomic) IBOutlet UIImageView *image1;
@property (weak, nonatomic) IBOutlet NetworkImage *image2;
@property (weak, nonatomic) IBOutlet ItemView *btnCart;
@property (weak, nonatomic) IBOutlet UIButton *btnBuy;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *image1Width;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *image1Height;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *image2Width;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *image2Height;
@end

@implementation SDiyController
-(void)dealloc{
    _stockTask = nil;
    _shareModel = nil;
    _cartTask = nil;
    _currentData = nil;
    [SVProgressHUD dismiss];
}
-(id)init{
    if(self=[super initWithNibName:@"SDiyController" bundle:nil]){
        
    }
    return self;
}

-(void)setModel:(FileArrayJsonTask*)model{
    _model = model;
    
}

-(void)updateViewConstraints{
    [super updateViewConstraints];
    
    CGRect rect = [UIScreen mainScreen].bounds;
    CGFloat height = rect.size.height;
    height -= 65;
    height -= _bottomHeight.constant;
    height -= (_txtLabel.frame.size.height + 5)*2;
    height/=2;
    
    CGSize size= [SizeUtil getCenterInsideSize:rect.size.width-20 containerHeight:height-20 targetWidth:CARD_WIDTH targetHeight:CARD_HEIGHT];
    
   _image2Width.constant = _image1Width.constant = size.width;
   _image2Height.constant = _image1Height.constant = size.height;
}

-(void)onShare:(id)sender{
    [_shareModel share:@"e通卡也可以DIY卡啦，你也来试试？" view:self.view];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"开始DIY"];
    [_btnShare setTarget:self withAction:@selector(onShare:)];
    _currentData = [_model getCurrent];
    _image1.layer.cornerRadius = 8;
    _image1.layer.masksToBounds = YES;
    _shareModel = [[ShareModel alloc]init];
    _image2.layer.cornerRadius = 8;
    _image2.layer.masksToBounds = YES;
    
    
    _image1.userInteractionEnabled = YES;
    UITapGestureRecognizer* gest = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(onImage1:)];
    [_image1 addGestureRecognizer:gest];
    gest = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(onImage2:)];
    _image2.userInteractionEnabled = YES;
    [_image2 addGestureRecognizer:gest];
    [self showImage];
    [_btnCart setTarget:self withAction:@selector(onCart:)];
    
    DiyVo* diy = _currentData;
    if(diy.fImage){
        [_btnBuy setEnabled:NO];
        //请求库存
        _stockTask = [[JsonTaskManager sharedInstance]createObjectJsonTask:@"s_card_detail2" cachePolicy:DMCachePolicy_NoCache];
        _loadingStock = YES;
        [_stockTask put:@"id" value:ITOA((int)diy.ID)];
        [_stockTask setListener:self];
        [_stockTask execute];
        [_image2 load:diy.fImage];
    }else{
        [_image2 setImage:[UIImage imageNamed:@"s_ic_add_diy"]];
        
    }
    
    [self updateF];
}


-(void)task:(id)task result:(id)result{
    _currentData.store = (int)[result[@"STOCK"] integerValue];
    _loadingStock = NO;
    [_btnBuy setEnabled:YES];
    
    
}

-(void)task:(id)task error:(NSString *)errorMessage isNetworkError:(BOOL)isNetworkError{
    [SVProgressHUD showErrorWithStatus:errorMessage];
}

-(void)updateF{
    if(_currentData.ID){
        _ftitle.text = _currentData.title;
        _fprice.text = _currentData.priceString;
    }
}


-(void)onAddToCart:(AddToCartView*)sender{

    DiyVo* diy = _currentData;
    NSString* path = diy.image;
    if(!path || ![[NSFileManager defaultManager]fileExistsAtPath:path]){
        [Alert confirm:@"您还没有定制卡片" yesButton:@"去定制" cancelButton:@"再说" delegate:self withTitle:@"DIY确认" tag:1];
        return;
    }
    [[CartModel sharedInstance]addToCart:diy count:[sender getCount] recharge:[sender getRecharge] file:path successListener:^{
    }];
}
- (IBAction)onBuy:(id)sender {
    
    if(![self check])return;
    [[ECardTaskManager sharedInstance]checkLogin:self loginSuccess:@selector(onLoginBuy:)];
}

-(void)onLoginBuy:(id)sender{
    DiyVo* data = [_model getCurrent];
    [AddToCartView show:self count:1 recharge:0 target:self selector:@selector(onBuyDirect:) title:@"购买" data:[_model getCurrent] store:data.store ];
}

-(void)onLoginAddToCart:(id)sender{
    if(_loadingStock){
        [SVProgressHUD showErrorWithStatus:@"请等待数据加载完毕"];
        return;
    }
    
    DiyVo* data = [_model getCurrent];
    [AddToCartView show:self count:1 recharge:0 target:self selector:@selector(onAddToCart:) title:@"加入购物车" data:[_model getCurrent] store:data.store];
}


-(void)onBuyDirect:(AddToCartView*)sender{
    _currentData.count = [sender getCount];
    _currentData.recharge = [sender getRecharge];
    [[SellingModel sharedInstance]onBuyDirect:self data:_currentData];
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    if(buttonIndex==1){
        if(alertView.tag==1){
            [self onImage1:nil];
        }else{
            [self onImage2:nil];
        }
    }
}

-(BOOL)check{
    DiyVo* diy = _currentData;
    NSString* path = diy.image;
    if(!path || ![[NSFileManager defaultManager]fileExistsAtPath:path]){
        //[SVProgressHUD showErrorWithStatus:@"请先定制卡片正面"];
        [Alert confirm:@"您还没有定制卡片正面" yesButton:@"去定制" cancelButton:@"再说" delegate:self withTitle:@"DIY确认" tag:1];
        return false;
    }

    if(!diy.fImage){
     
        [Alert confirm:@"您还没有定制卡片反面" yesButton:@"去定制" cancelButton:@"再说" delegate:self withTitle:@"DIY确认" tag:2];
        return false;
    }
    
    return true;
    
    
}

-(void)onCart:(id)sender{
    if(![self check])return;
    
    [[ECardTaskManager sharedInstance]checkLogin:self loginSuccess:@selector(onLoginAddToCart:)];
}

-(void)backToPrevious:(id)sender{
    DiyVo* diy = _currentData;
    if(diy.image){
        [self onSave:sender];
    }
    
    [super backToPrevious:sender];
    
}



-(void)onSave:(id)sender{
    [_model saveCurrent];
}

// Called when a button is clicked. The view will be automatically dismissed after this call returns
- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex{
    __weak SDiyController* __self =self;
    if(buttonIndex == 0)
    {
        [ExternalUtil selectFromAlbum:self completion:^(UIImage * image) {
            [__self editImage:image autoEdit:YES];
        }];
    } else if(buttonIndex == 1)
    {
        [ExternalUtil captureImage:self completion:^(UIImage * image) {
            [__self editImage:image autoEdit:YES];
        }];
    }
    
}

-(void)editImage:(UIImage*)image autoEdit:(BOOL)autoEdit{
    [EditorUtil configEditor:(CGFloat)CARD_HEIGHT / CARD_WIDTH minWidth:CARD_WIDTH minHeight:CARD_HEIGHT];
    [EditorUtil editImage:image parent:self delegate:self title:@"编辑定制图片" autoEdit:autoEdit];
}

-(void)imageEditorWillFinishEditingWidthImage:(UIImage *)image{
    DiyVo* diy = _currentData;
    [diy saveImage:image];
    [diy saveThumb:image];
}

- (void)imageEditorDidFinishEdittingWithImage:(UIImage*)image{
    _image1.contentMode = UIViewContentModeScaleToFill;
    _image1.image = image;
}

-(void)onLoginGotoDiy:(id)sender{
    SDiySelImageController* controller = [[SDiySelImageController alloc]init];
    controller.selComplete = ^(NSString* image,NSString* ID){
        
        
    };
    [self.navigationController pushViewController:controller animated:YES];
}

-(void)onImage1:(id)sender{
    DiyVo* diy = _currentData;
    if([diy hasImage]){
        
        [self editImage:[diy getImage] autoEdit:NO];
        
    }else{
       
        UIActionSheet *sheet = [[UIActionSheet alloc]initWithTitle:@"选择图片" delegate:self cancelButtonTitle:@"关闭" destructiveButtonTitle:nil otherButtonTitles:@"相册",@"拍照", nil];//关闭按钮在最后
        
        sheet.delegate = self;
        sheet.actionSheetStyle = UIActionSheetStyleBlackTranslucent;//样式
        [sheet showInView:self.view];//显示样式

    }
 }

-(UIImage*)getImage{
    DiyVo* diy = _currentData;
    return [diy getImage];

}

-(void)showImage{
    
    UIImage* image = [self getImage];
    if(image){
        _image1.contentMode = UIViewContentModeScaleToFill;
        _image1.image = image;
    }
}

-(void)onImage2:(id)sender{
    SDiyFController* controller = [[SDiyFController alloc]init];
    controller.data = _currentData;
    controller.delegate = self;
    [self.navigationController pushViewController:controller animated:YES];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}




#pragma SDiyFController
-(void)editFComplete:(SDiyPagesVo*)data{
    DiyVo* diy = _currentData;
    diy.ID = data.ID;
    diy.fImage = data.img;
    diy.price = data.price;
    diy.title = data.title;
    diy.store = (int) data.store;
    [self updateF];
    [_image2 load:diy.fImage];
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
