//
//  GoodInfoController.m
//  ecard
//
//  Created by randy ren on 14-5-18.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "GoodInfoController.h"
#import "LostCardDetailInfo.h"
#import "GoodInfoView.h"
#import "BgTextView.h"


@interface GoodInfoController ()
{
    Form* _form;
    BgTextView* _view;
}
@end

@implementation GoodInfoController

-(void)dealloc{
    _form = NULL;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.view.backgroundColor = RGB(f2, f2, f2);
    [self setTitle:@"信息登记"];
    _form = [[Form alloc]initWithParent:self.view contentNibName:@"GoodInfoView" frame:[UIScreen mainScreen].bounds api:@"lost_save" listener:self];

}

-(void)initForm:(FormModel*)model contentView:(GoodInfoView*)contentView{
    LostCardDetailInfo* data = (LostCardDetailInfo*)self.data;
    [contentView.btnHelp setTarget:self withAction:@selector(onHelp:)];
    contentView.txtInfo.text = data.info;
    contentView.txtPhone.text = data.phone;
    [contentView.sex setSelectedIndex:data.sex];
    [ViewUtil setButtonBg:contentView.btnOk];
    [model add:contentView.txtPhone name:@"phone" rules:RULES_REQUIRED];
    [model addOkButton:contentView.btnOk];
}


ON_EVENT(onHelp){
    [self.navigationController pushViewController:[[SimpleViewController alloc]initWithListener:self] animated:YES];
}
-(void)onViewLoaded:(UIViewController*)controller{
    [controller setTitle:@"信息登记帮助"];
    _view = [[BgTextView alloc]initWidth:self.view.frame.size.width-10 text:@"1）您的信息将在1个工作日内进行审核,并保存到系统中；\n2）此信息将在点击发布失卡后才对外公布；\n3）公开的信息仅为拾卡人输入卡号信息才能获得；\n相信个个都是活雷锋，您也一样。"];
    _view.frame = CGRectMake(5, 5+65, self.view.frame.size.width-10, _view.bounds.size.height);
    [controller.view addSubview:_view];
}
-(void)onDealloc:(UIViewController*)controller{
    _view = NULL;
}


-(void)onSubmitComplete:(NSDictionary*)submitData result:(id)result{
    [self setResult:RESULT_OK data:result];
}

-(BOOL)beforeSubmit:(NSMutableDictionary *)data contentView:(GoodInfoView *)contentView{
    NSString *info = [contentView.txtInfo.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
    if(info.length>200){
        [Alert alert:@"信息字数太多了"];
        return FALSE;
    }
    if(info.length==0)
    {
        [Alert alert:@"亲，说点什么吧"];
        return FALSE;
    }
    LostCardDetailInfo* detail = (LostCardDetailInfo*)self.data;
    detail.info = info;
    detail.phone = data[@"phone"];
    detail.sex = (int)[contentView.sex getSelected];
    [data setValue:info forKey:@"message"];
    [data setValue:[NSNumber numberWithInt:detail.sex] forKey:@"sex"];
    
    
    return TRUE;
}



- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
