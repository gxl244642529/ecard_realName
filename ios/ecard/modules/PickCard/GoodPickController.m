//
//  GoodPickController.m
//  ecard
//
//  Created by randy ren on 14-5-18.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "GoodPickController.h"
#import "ReturnCardView.h"
#import "LostCardInfo.h"
#import "GoodPickHeadView.h"

@interface GoodPickController ()
{
    ReturnCardView* _returnCardView;
    LostCardInfo* _lostCardInfo;
    NSString* _cardNumber;
    JsonTask* _lostCardReva;
    UIScrollView* _scrollView;
    GoodPickHeadView* _headView;
}



@end

@implementation GoodPickController

-(void)dealloc{
    RemoveObserver(self);
    _headView = NULL;
    _cardNumber = NULL;
    _returnCardView = NULL;
    _lostCardInfo = NULL;
    _lostCardReva=NULL;
    _scrollView = NULL;
    
}
/*

- (void)onQuery:(id)sender {
    NSString *cardNumber = [_headView.txtCardNumber.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
    
    if(cardNumber.length==0)
    {
        [SystemUtil alert:@"请输入e通卡卡号"];
        return;
    }
    _cardNumber = cardNumber;
    
    [SystemUtil wait:self.view message:@"正在搜索卡信息..."];
    _currentTask= [JsonUtil queyLostCard:_currentTask cardNumber:cardNumber delegate:self];
    
}
-(void)onRequestSuccess:(NSDictionary*)result{
    [SystemUtil cancelWait:self.view];
    [_headView.txtCardNumber resignFirstResponder];
    switch ([[result valueForKey:@"ret"]intValue]) {
        case 0:{
            if(_returnCardView){
                [_returnCardView removeFromSuperview];
                _returnCardView = NULL;
            }
            if(!_lostCardInfo){
                _lostCardInfo = [SystemUtil createView:@"LostCardInfo"];
                [self.view addSubview:_lostCardInfo];
                _lostCardInfo.frame = CGRectOffset(_lostCardInfo.bounds, 0, 53);
                [SystemUtil flyView:_lostCardInfo enter:YES delegate:self];
            }
            NSDictionary* data = [result valueForKey:@"data"];
            _lostCardInfo.txtInfo.text = [data valueForKey:@"INFO"];
            NSString* strSex = NULL;
            int sex = [Data_ValueForKey(SEX)intValue];
            switch (sex) {
                
                case 0:
                    strSex = @"保密";
                    break;
                case 1:
                    strSex = @"男";
                    break;
                case 2:
                    strSex = @"女";
                    break;
                default:
                    break;
            }
            _lostCardInfo.txtSex.text=strSex;
            _lostCardInfo.phone = [data valueForKey:@"PHONE"];
        }
            break;
        case 1:
            [SystemUtil alert:@"此卡不存在，请确认是否输入正确"];
            break;
        case 2:
            //拾卡
        {
            if(_lostCardInfo){
                [_lostCardInfo removeFromSuperview];
                _lostCardInfo = NULL;
            }
            if(!_returnCardView){
                _returnCardView = [SystemUtil createView:@"ReturnCardView"];
                CGRect rect = ConentViewFrame;
                _scrollView = [[UIScrollView alloc]initWithFrame:CGRectMake(0, 53, rect.size.width, rect.size.height-53)];
                _returnCardView.frame =CGRectMake(0, 0, rect.size.width, rect.size.height-53);
                [_scrollView addSubview:_returnCardView];
                [self.view addSubview:_scrollView];
                _returnCardView.txtCardNumber.text =_cardNumber;
                [SystemUtil flyView:_scrollView enter:YES delegate:self];
                
            }
            _returnCardView.txtCardNumber.text = _cardNumber;
        }
            
            break;
        case 3:
        {
            [SystemUtil confirm:@"此卡是本人的,并且已经登记找回，是否撤销找回?" yesButton:@"是" cancelButton:@"取消" delegate:self withTitle:@"本人的卡"];
        }
            break;
        case 4:
        {
            [SystemUtil alert:@"此卡是本人的"];
        }
            break;
        default:
            break;
    }
    
    
}

-(void)onShowKeyboard:(id)sender{
    if(_scrollView){
        CGRect rect = ConentViewFrame;
        _scrollView.frame = CGRectMake(0, 53, rect.size.width, 100);
    }
}

-(void)onHideKeyboard:(id)sender{
    CGRect rect = ConentViewFrame;
    _scrollView.frame = CGRectMake(0, 53, rect.size.width, rect.size.height-53);
}


- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    
    if(buttonIndex==1){
        [SystemUtil wait:self.view message:@"正在撤销找回..."];
        RequestResult* request = [[RequestResult alloc]init];
        request.successListener = ^(id result){
            [SystemUtil cancelWait:self.view];
            [SystemUtil alert:@"撤销找回成功"];
        };
        request.errorListener = ^(NSString* error,BOOL isNetworkError){
            [SystemUtil cancelWait:self.view];
            [SystemUtil alert:error];
        };
        
        _lostCardReva=[JsonUtil lostCardRevocation:_lostCardReva delegate:request];
        
    }
    
}
-(void)onRequestError:(NSString*)errorMessage isNetworkError:(BOOL)isNetworkError{
    
}
- (void)viewDidLoad
{
    [super viewDidLoad];
    [self setTitle:@"拾卡招领"];
    Keyboard_AddObserver_show(onShowKeyboard);
    Keyboard_AddObserver_hide(onHideKeyboard);
    _headView = [SystemUtil createView:@"GoodPickHeadView"];
    [self.view addSubview:_headView];
    [_headView.btnQuery addTarget:self action:@selector(onQuery:) forControlEvents:UIControlEventTouchUpInside];

}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}
*/
@end
