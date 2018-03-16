//
//  GoodCardPickController.m
//  ecard
//
//  Created by randy ren on 14-7-16.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "GoodCardPickController.h"
#import "ReturnCardView.h"
#import "LostCardInfo.h"
#import "GoodPickHeadView.h"
#import "UIBottomView.h"
#import "AnimationUtil.h"
#import "FormScrollView.h"

@interface GoodCardPickController ()
{
    NSString* _cardNumber;
    ObjectJsonTask* _lostCardReva;
    ObjectJsonTask* _pickCard;
    ObjectJsonTask* _queryCardTask;
    FormScrollView* _scrollView;
    GoodPickHeadView* _headView;
    BOOL inputTime;
}
@end

@implementation GoodCardPickController
-(void)dealloc{
    _lostCardReva=NULL;
    _pickCard = NULL;
    _queryCardTask = NULL;
    
    _headView = NULL;
    _cardNumber = NULL;
   
    _scrollView = NULL;
}
- (void)viewDidLoad
{
    [super viewDidLoad];
    [self setTitle:@"拾卡招领"];
    inputTime = FALSE;
    
    _scrollView = [[FormScrollView alloc]initWithFrame:self.view.bounds contentNibName:@"GoodPickHeadView"];
    [self.view addSubview:_scrollView];
    _headView = [_scrollView contentView];

    [_headView.btnQuery addTarget:self action:@selector(onQuery:) forControlEvents:UIControlEventTouchUpInside];
    _headView.lostCardInfo.hidden = YES;
    _headView.returnCardView.hidden = YES;
    Control_AddTarget(_headView.btnSaveLostCard, onSaveLostCard);
    [_headView.btnPickTime setTarget:self withAction:@selector(onPickTime:)];
    [_headView.btnHelp setTarget:self withAction:@selector(onHelp:)];
    
    
    [ViewUtil setButtonBg:_headView.btnSaveLostCard];
    
    
}
-(void)onViewLoaded:(UIViewController*)controller{
    controller.title = @"帮助";
    BgTextView* _textView = [[BgTextView alloc]initWidth:self.view.frame.size.width-10 text:@"1）您的信息将在1个工作日内进行审核并最终发布；\n2）审核后发布于【拾卡招领】，丢卡人看到后可直接拨打电话认领。"];
    _textView.frame = CGRectMake(5,5+65, self.view.frame.size.width-10, _textView.bounds.size.height);
    [controller.view addSubview:_textView];
}
-(void)onDealloc:(UIViewController*)controller{
}
//帮助
ON_EVENT(onHelp){
    [self.navigationController pushViewController:[[SimpleViewController alloc]initWithListener:self] animated:YES];
}
- (void)onQuery:(id)sender {
    NSString *cardNumber = [_headView.txtCardNumber.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
    if(cardNumber.length==0)
    {
        [Alert alert:@"请输入e通卡卡号"];
        return;
    }
    _cardNumber = cardNumber;
    
    [Alert wait:self.view message:@"正在搜索卡信息..."];
    if(!_queryCardTask){
        _queryCardTask = [[ECardTaskManager sharedInstance]createObjectJsonTask:@"query_lost_card" cachePolicy:DMCachePolicy_NoCache];
        [_queryCardTask setListener:self];
      [_queryCardTask put:@"last" value:@0];
        [_queryCardTask put:@"pushID" value:[[ECardTaskManager sharedInstance]getPushID]];
    }
    [_queryCardTask put:@"cardid" value:_cardNumber];
    [_queryCardTask execute];
    
}
-(void)task:(NSObject<IJsonTask>*)task result:(id)result{
    [Alert cancelWait:self.view];
    if(task == _queryCardTask){
        [_headView.txtCardNumber resignFirstResponder];
        switch ([[result valueForKey:@"ret"]intValue]) {
            case 0:{
                _headView.lostCardInfo.hidden = NO;
                _headView.returnCardView.hidden = YES;
                [AnimationUtil flyView:_headView.lostCardInfo enter:YES delegate:self];
                NSDictionary* data = [result valueForKey:@"data"];
                _headView.lostCardInfo.txtInfo.text = [data valueForKey:@"INFO"];
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
                _headView.lostCardInfo.txtSex.text=strSex;
                _headView.lostCardInfo.phone = [data valueForKey:@"PHONE"];
            }
                break;
            case 1:
                [Alert alert:@"此卡不存在，请确认是否输入正确"];
                break;
            case 2:
                //拾卡
            {
                
                _headView.lostCardInfo.hidden = YES;
                _headView.returnCardView.hidden = NO;
                _headView.returnCardView.txtCardNumber.text = _cardNumber;
            }
                
                break;
            case 3:
            {
                [Alert confirm:@"此卡是本人的,并且已经登记找回，是否撤销找回?" yesButton:@"是" cancelButton:@"取消" delegate:self withTitle:@"本人的卡"];
            }
                break;
            case 4:
            {
                [Alert alert:@"此卡是本人的"];
            }
                break;
            default:
                break;
        }
        
    }else if(task == _pickCard){
        //发布拾卡
        [Alert alert:@"发布拾卡完毕"];
        
    }else if(task == _lostCardReva){
        [Alert alert:@"撤销找回成功"];
    }
}
-(void)task:(NSObject<IJsonTask> *)task error:(NSString *)errorMessage isNetworkError:(BOOL)isNetworkError{
    [Alert cancelWait:self.view];
    [Alert alert:errorMessage];
}

//选择时间
ON_EVENT(onPickTime){
    UIDatePicker *datePicker = [ [ UIDatePicker alloc] initWithFrame:CGRectMake(0.0,0.0,0.0,0.0)];
    datePicker.datePickerMode = UIDatePickerModeDateAndTime;
    [self.view addSubview:datePicker];
    datePicker.backgroundColor = [UIColor whiteColor];
    
    UIBottomView* bottomView = [[UIBottomView alloc]initWithTitle:@"选择日期时间"];
    [bottomView setContentView:datePicker];
    bottomView.delegate = self;
    [bottomView show];
    
    
    [_headView.txtCardNumber resignFirstResponder];
    [_headView.txtLostPhone resignFirstResponder];
}
-(void)popupView:(UIPopup*)popup contentView:(UIDatePicker*)control result:(UIPopupResult)result{
    if(result==UIPopupResultOk){
        inputTime = TRUE;
        NSDate* date = control.date;
        NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
        [dateFormatter setDateFormat: @"yyyy-MM-dd HH:mm:ss"];
        NSString *destDateString = [dateFormatter stringFromDate:date];
        _headView.txtPickTime.text = destDateString;
    }
}

//保存信息
ON_EVENT(onSaveLostCard){
    NSString* phone = [_headView.txtLostPhone.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceCharacterSet]];
    if(phone.length==0){
        [Alert alert:@"请输入联系方式"];
        return;
    }
    if(!inputTime){
        [Alert alert:@"请输入拾卡时间"];
        return;
    }
    
    if(!_pickCard){
        _pickCard = [[JsonTaskManager sharedInstance]createObjectJsonTask:@"pick_save" cachePolicy:DMCachePolicy_NoCache];
        [_pickCard setWaitMessage:@"正在发布拾卡..."];
        _pickCard.successListener = ^(id result){
            [Alert alert:@"发布拾卡成功"];
            [SVProgressHUD dismiss];
        };
        _pickCard.errorListener = ^(NSString* error,BOOL isNet){
            [SVProgressHUD showErrorWithStatus:error];
        };
    }
    [_pickCard put:@"pushID" value:[[JsonTaskManager sharedInstance] getPushID]];
    [_pickCard put:@"cardid" value:_cardNumber];
    [_pickCard put:@"phone" value:phone];
    [_pickCard put:@"address" value:@""];
    [_pickCard put:@"time" value:_headView.txtPickTime.text];
    [_pickCard execute];
}



- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    
    if(buttonIndex==1){
        [Alert wait:self.view message:@"正在撤销找回..."];
        _lostCardReva = [[JsonTaskManager sharedInstance]createObjectJsonTask:@"lost_revocation" cachePolicy:DMCachePolicy_NoCache];
        [_lostCardReva execute];
    }
    
}




@end
