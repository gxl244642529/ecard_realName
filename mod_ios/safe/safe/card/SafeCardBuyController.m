//
//  SafeCardBuyController.m
//  ecard
//
//  Created by randy ren on 16/1/30.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "SafeCardBuyController.h"
#import "SafeModel.h"
#import "SafePayAction.h"
#import "SafeUtil.h"
#import "SafeCashierController.h"
#import "RealInfo.h"
#import "SafePayResultController.h"
#import "MySafeController.h"
#import "SafeModel.h"
#import <ecardlib/ecardlib.h>

@interface SafeCardBuyController ()
{
    __weak IBOutlet NSLayoutConstraint *_idCardHeight;
    __weak IBOutlet UIView *_idCardView;
    __weak IBOutlet CommonConditionButton *_btnSubmit;
    __weak IBOutlet DMFormTextField *_txtName;
    __weak IBOutlet DMFormTextField *_txtIdCard;
    SafeModel* _model;
}
@end

@implementation SafeCardBuyController


@synthesize data = _data;
INIT_BUNDLE_CONTROLLER(SafeCardBuyController, safebundle.bundle)
- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self setTitle:@"填写保单信息"];
    self.payResultController = [SafePayResultController class];
    
    
    DMAccount* account = [DMAccount current];
    if([account.userID isEqualToString:@"443"]){
        _idCardView.hidden = YES;
        _idCardHeight.constant = 0;
        UIView* idCardText = [self.view viewWithTag:11];
        idCardText.hidden = YES;
    }
}



-(BOOL)submit:(id<DMSubmit>)submit validate:(NSMutableDictionary*)data{
    
    data[@"inId"] = self.data.inId;
    data[@"typeId"] = self.data.typeId;
    
    if(!_idCardView.hidden){
        if(![ValidateUtil isIdCard:data[@"idCard"]]){
            [Alert alert:@"请输入正确的身份证号"];
            return FALSE;
        }
    }
    
   
    
    return YES;
}

-(BOOL)submit:(id<DMSubmit>)submit onSubmit:(NSMutableDictionary *)data{
    
    if(_data.ticket){
        
        data[@"ticket"] = _data.ticket;
        
        if(!_model){
            _model = [[SafeModel alloc]init];
        }
        
        [_model submit:data button:_btnSubmit];
        
        
        return  YES;
    }
    return NO;
}


API_JOB_SUCCESS(user, realInfo, RealInfo*){
    
    if(result.isValid){
        _txtName.enabled = NO;
        _txtIdCard.enabled = NO;
    }
    
}
// Called when a button is clicked. The view will be automatically dismissed after this call returns
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    UINavigationController* nav = self.navigationController;
    [self.navigationController popViewControllerAnimated:NO];
    [nav pushViewController:[[MySafeController alloc]init] animated:YES];
    //[self finish];
    
}
API_JOB_SUCCESS(i_safe, submitV2, NSDictionary*){
    if(_data.ticket){
        [Alert alert:@"保单生成成功，请等待保单生效" delegate:self];
    }else{
        //这里进入收银台
        [self createPayModel];
        
        if(!_payModel.action){
            _payModel.action = [[SafePayAction alloc]init];
        }
        //这里进入收银台
        NSMutableDictionary* data = [[NSMutableDictionary alloc]initWithDictionary:result];
        //这里修改几个字段
        data[@"guardUrl"] = [SafeUtil getUrl:data[@"guardUrl"]];
        //infos
        NSMutableArray* infos = [[NSMutableArray alloc]init];
        [infos addObject:@{@"label":@"ｅ通卡号:",@"value":data[@"cardId"]}];
        data[@"infos"]=infos;
        data[@"fee"] = [NSString stringWithFormat:@"%.2f",[data[@"fee"]floatValue] / 100];
        [DMPayModel runningInstance].orderId = data[@"id"];
        
        
        Push_ViewController_Data(SafeCashierController, data);

    }
    
}

-(void)dealloc{
    _model = nil;
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
