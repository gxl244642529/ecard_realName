//
//  GoodPersonalController.m
//  ecard
//
//  Created by randy ren on 14-5-18.
//  Copyright (c) 2014年 citywithincity. All rights reserved.
//

#import "GoodPersonalController.h"
#import "LostCardDetailInfo.h"
#import "GoodCardPickController.h"
#import "GoodInfoController.h"
#import "GoodQueryController.h"
#import "ArrayJsonTask.h"
#import "UIPopupView.h"


#define ADD_PASS @"您登记的信息已经审核通过,可选择失卡发布"
#define CONFIRM_NOT_PASS @"您登记的信息没有审核通过，请您先修改信息以后在重新发布,要去修改吗"
#define CONFIRM_BIND_CARD @"您还没有绑定任何e通卡，需要绑定e通卡后才能发布，去绑定吗?"
#define MODIFY_NOT_PASS @"您修改的信息审核未通过，原因是:%@"
#define ADD_NOT_PASS @"您登记的信息审核未通过，原因是:%@"
#define ADD_WAIT_NO_CARD @"您登记的信息正在审核中，请耐心等待..."
#define ADD_WAIT_HAS_CARD @"您登记的信息正在审核中,您已经发布失卡:%@请耐心等待..."
#define FIND_CARD @"您的卡%@失卡信息已经发布，如雷锋捡到，相信会与您联系"

#define INFO9 @"您还没有登记信息,登记信息可帮助您在丢失卡片以后找回,去登记吗?"
 #define MODIFY_PASS @"您修改的信息审核已经通过，您可以发布失卡"

  #define MODIFY_WAIT_HAS_CARD @"您修改的信息正在审核中,您已经发布失卡:%@请耐心等待..."

 #define MODIFY_WAIT @"您修改的信息正在审核中，请耐心等待..."


#define TAG_INPUT_INFO 1
#define TAG_BIND_CARD 3

#define REQUEST_INPUT_CARD_INFO 102


@interface GoodPersonalController ()
{
    //数据
    LostCardDetailInfo* _info;
    ObjectJsonTask* _rTask;//撤销
    ObjectJsonTask* _pTask;//发布
    ObjectJsonTask* _infoTask;//获取我的信息任务
}
@property (weak, nonatomic) IBOutlet ItemView *btnQuery;
@property (weak, nonatomic) IBOutlet ItemView *btnPick;
@property (weak, nonatomic) IBOutlet ItemView *btnInfo;

@property (weak, nonatomic) IBOutlet UILabel *txtInfo;

@property (weak, nonatomic) IBOutlet UIButton *btnPublish;
@property (weak, nonatomic) IBOutlet UIButton *btnReject;

@end

@implementation GoodPersonalController

INIT_CONTROLLER(GoodPersonalController)

-(void)dealloc{
    _info = NULL;
    _rTask = NULL;
    _pTask = NULL;
}


-(void)viewDidAppear:(BOOL)animated{
    [super viewDidAppear:animated];
    
    if(![[ECardTaskManager sharedInstance]isLogin]){
        [Alert confirm:@"登录以后才可执行相关操作，请登录" yesButton:@"去登录" cancelButton:@"取消" delegate:self withTitle:@"登录提示"];
    }

}

- (void)viewDidLoad
{
    [super viewDidLoad];
    self.view.backgroundColor = RGB(f2, f2, f2);
    [self setTitle:@"拾卡不昧"];
    [_btnInfo setTarget:self withAction:@selector(onInfo:)];
    [_btnQuery setTarget:self withAction:@selector(onQuery:)];
    [_btnPick setTarget:self withAction:@selector(onPick:)];
    
    _btnInfo.layer.masksToBounds = YES;
    _btnInfo.layer.cornerRadius = 3;
    
    _btnQuery.layer.masksToBounds = YES;
    _btnQuery.layer.cornerRadius = 3;
    
    _btnPick.layer.masksToBounds = YES;
    _btnPick.layer.cornerRadius = 3;
    [ViewUtil setButtonBg:_btnPublish];
    [ViewUtil setButtonBg:_btnReject];
    
    if([[ECardTaskManager sharedInstance]isLogin]){
        [self loadData];
    }

}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    if(alertView.tag==TAG_INPUT_INFO){
        if(buttonIndex==1){
            //登记信息
            [self gotoInputInfo];
        }else{
            [self finish];
        }
    }else{
        if(buttonIndex==0){
            [self finish];
        }else{
            [[ECardTaskManager sharedInstance]checkLogin:self loginSuccess:@selector(onLoginLoadData:)];
        }
    }
}

-(void)onLoginLoadData:(id)sender{
    [self loadData];
}



-(void)gotoInputInfo{
    GoodInfoController* controller = [[GoodInfoController alloc]init];
    controller.data = _info;
    [self openControllerForResult:controller requestCode:REQUEST_INPUT_CARD_INFO data:_info modal:FALSE];
}

/**
 *  加载我的信息
 */
-(void)loadData{
    if(!_infoTask){
        _infoTask = [[ECardTaskManager sharedInstance]createObjectJsonTask:@"my_lost_card_info" cachePolicy:DMCachePolicy_NoCache];
        [_infoTask setWaitMessage:@"正在加载信息..."];
        [_infoTask setListener:self];
    }
    [_infoTask execute];
   
    _btnPublish.hidden = YES;
    _btnReject.hidden = YES;
}

-(void)onControllerResult:(NSInteger)resultCode requestCode:(NSInteger)requestCode data:(id)data{
    
    if(resultCode==RESULT_OK){
        if(requestCode==REQUEST_INPUT_CARD_INFO){
            [self loadData];
        }
        /*else if(requestCode == REQUEST_CODE_SELECT_ECARD){
         
        }*/
    }else{
        if(requestCode == REQUEST_LOGIN){
            [self finish];
        }
    }
}
ON_EVENT(onQuery){
    Push_ViewController(GoodQueryController);
}
ON_EVENT(onPick){
    Push_ViewController(GoodCardPickController);
}
ON_EVENT(onInfo){
    [self gotoInputInfo];
}

-(void)task:(NSObject<IJsonTask> *)task result:(id)result{
    [Alert cancelWait:self.view];
    if(task == _infoTask){
        NSDictionary* data = [result valueForKey:@"data"];
        if(data){
            
            LostCardDetailInfo* info = [[LostCardDetailInfo alloc]init];
            
            NSString* str = Data_ValueForKey(LAST_MODIFIED);
            if(str){
                info.time = str;
            }
            
            str = Data_ValueForKey(PHONE);
            if(str){
                info.phone = Data_ValueForKey(PHONE);
                info.sex = ValueForKey_IntValue(data, SEX);
                info.cardID = Data_ValueForKey_CheckNull(CARD_ID,NULL);
                info.info = Data_ValueForKey_CheckNull(INFO,NULL);
            }
            
            NSNumber* status = Data_ValueForKey(STATUS);
            if(status){
                info.status = ValueForKey_IntValue(data, STATUS);
                info.vry_time = ValueForKey_IntValue(data, VRY_TIME);
                info.vry_id = ValueForKey_IntValue(data, VRY_ID);
                info.vry_result = Data_ValueForKey_CheckNull(VRY_RESULT,NULL);
                info.vry_error = Data_ValueForKey_CheckNull(VRY_ERROR,NULL);
            }
            
            _info = info;
            
            if(_info.status==1){
                if(_info.vry_result){
                    if(_info.vry_error){
                        //审核错误
                        _txtInfo.text = [NSString stringWithFormat:ADD_NOT_PASS,_info.vry_error];
                        
                    }else{
                        //审核成功,不存在
                        _txtInfo.text = ADD_PASS;
                    }
                }else{
                    //还没有审核
                    if(_info.cardID){
                        //可以撤销失卡
                        _btnReject.hidden = NO;
                        _txtInfo.text = [NSString stringWithFormat:ADD_WAIT_HAS_CARD,_info.cardID];
                    }else{
                        //可以发布失卡
                        _btnPublish.hidden = NO;
                        _txtInfo.text = ADD_WAIT_NO_CARD;
                    }
                }
            }else{
                if(_info.vry_result){
                    if([_info.vry_result compare:@"pass"]==0){
                        //通过了
                        //审核结果,有，表示审核通过
                        if(_info.cardID){
                            //发布卡成功
                            
                            _txtInfo.text = [NSString stringWithFormat:FIND_CARD,_info.cardID];
                            _btnReject.hidden = NO;
                        }else{
                            //可以发布
                            _btnPublish.hidden = NO;
                            _txtInfo.text = MODIFY_PASS;
                            
                        }
                    }else{
                        //审核没有通过
                        _txtInfo.text = [NSString stringWithFormat:MODIFY_NOT_PASS,_info.vry_error];
                    }
                    
                    
                }else{
                    //修改审核中
                    if(_info.cardID){
                        //发布卡成功
                        _txtInfo.text = [NSString stringWithFormat:MODIFY_WAIT_HAS_CARD,_info.cardID];
                        
                        _btnReject.hidden = NO;
                    }else{
                        //可以发布
                        _btnPublish.hidden = NO;
                        
                        _txtInfo.text = MODIFY_WAIT;
                        
                    }
                }
            }
            
        }else{
            _info = LostCardDetailInfo.new;
            //登记失卡
            [Alert confirm:INFO9 yesButton:@"去登记" cancelButton:@"取消" delegate:self withTitle:@"登记信息" tag:TAG_INPUT_INFO];
            
        }

    }else if(task == _pTask){
        [self loadData];
        [Alert alert:@"发布失卡成功"];
    }else if(task == _rTask){
        [self loadData];
        [Alert alert:@"撤销发布成功"];
    }
}
-(void)task:(NSObject<IJsonTask> *)task error:(NSString *)errorMessage isNetworkError:(BOOL)isNetworkError{
    
    [Alert cancelWait:self.view];
    [Alert alert:errorMessage];
    
}

-(void)didSelectECard:(ECard*)ecard{
  //发布失卡
  if(!_pTask){
    _pTask = [[JsonTaskManager sharedInstance]createObjectJsonTask:@"lost_publish" cachePolicy:DMCachePolicy_NoCache];
    [_pTask setListener:self];
    [_pTask setWaitMessage:@"正在发布失卡..."];
  }
  [_pTask put:@"cardid" value:ecard.cardId];
  [_pTask execute];

}
- (IBAction)onLostCard:(id)sender {
  [BusinessCaller  selectECard:self delegate:self ];
}

- (IBAction)onFoundCard:(id)sender {
    if(!_rTask){
        _rTask = [[ECardTaskManager sharedInstance]createObjectJsonTask:@"lost_revocation" cachePolicy:DMCachePolicy_NoCache];
        [_rTask setListener:self];
    }
    [Alert wait:self.view message:@"正在撤销发布..."];
    [_rTask execute];
    
}


/*

-(void)onRequestError:(NSString*)errorMessage isNetworkError:(BOOL)isNetworkError{
    _info = LostCardDetailInfo.new;
    [SystemUtil cancelWait:self.view];
    [SystemUtil alert:@"网络错误，请稍后重试"];
    [self finish];
}
*/

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
