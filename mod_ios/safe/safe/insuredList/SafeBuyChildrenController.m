//
//  SafeBuyChildrenController.m
//  ecard
//
//  Created by 任雪亮 on 16/3/22.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "SafeBuyChildrenController.h"
#import "SafeInsuredListView.h"
#import "RealInfo.h"
#import "SafePayHandler.h"
#import "SafeUtil.h"
#import "SafeSelContactController.h"
#import "SafeModel.h"



@interface SafeBuyChildrenController ()
{
    SafePayHandler* _handler;
}

@property (weak, nonatomic) IBOutlet DMFormTextField *myIdCard;

@property (weak, nonatomic) IBOutlet CommonConditionButton *btnSubmit;
@property (weak, nonatomic) IBOutlet DMFormView *formView;

@property (weak, nonatomic) IBOutlet UITextField *name;
@property (weak, nonatomic) IBOutlet UITextField *idCard;


@end

@implementation SafeBuyChildrenController
@synthesize data = _data;
INIT_BUNDLE_CONTROLLER(SafeBuyChildrenController, safebundle.bundle)
- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"购买"];
    _handler = [[SafePayHandler alloc]initWithParent:self];
    _btnSubmit.enabled = NO;
    _handler.payCancelAction = PayCancelAction_BacktoPreviousController;
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (IBAction)onContact:(id)sender {
    SafeSelContactController* c = [[SafeSelContactController alloc]init];
    c.data = _data;
    c.delegate = self;
   
    
    [self.navigationController pushViewController:c animated:YES];
}
/**
 如果是单选，则替换掉，如果是多选，也是替换掉
 */
-(void)onSelectContacts:(NSArray*)arr{
    if(arr.count > 0){
        
        SafeContact* contack = arr[0];
        _name.text = contack.name;
        _idCard.text = contack.idCard;
        
    }
}

API_JOB_SUCCESS(user, realInfo, RealInfo*){
    [_btnSubmit setEnabled:YES];
    
    if(result.isValid){
       // _myIdCard.userInteractionEnabled = NO;
    }
}

API_JOB_SUCCESS(i_safe, submitInsured, id){
    
    //这里进入收银台
    NSMutableDictionary* data = [[NSMutableDictionary alloc]initWithDictionary:result];
    //这里修改几个字段
    data[@"guardUrl"] = [SafeUtil getUrl:data[@"guardUrl"]];
    //infos
    NSMutableArray* infos = [[NSMutableArray alloc]init];
    switch(_data.type){
        case 1:
            break;
        case TYPE_FAMILLY:
            [infos addObject:@{@"label":@"保障地址:",@"value":data[@"addr"]}];
            break;
        case TYPE_CAR:
        {
            [infos addObject:@{@"label":@"车牌号:",@"value":data[@"carNo"]}];
            [infos addObject:@{@"label":@"车架号:",@"value":data[@"carFrame"]}];
        }
            break;
    }
    
    
    data[@"infos"]=infos;
    data[@"fee"] = [NSString stringWithFormat:@"%.2f",[data[@"fee"]floatValue] / 100];
    
    
    [_handler initParam:[result[@"fee"]integerValue] orderId:result[@"id"] data:data];
    
}


-(BOOL)submit:(id<DMSubmit>)submit validate:(NSMutableDictionary*)data{
    NSString* phone = data[@"phone"];
    if(phone.length != 11){
        [Alert alert:@"请输入正确的电话号码"];
        return NO;
    }
    if([_name.text isEqualToString:@""]){
        [Alert alert:@"请输入被保险人姓名"];
        return NO;
    }
    
    if([_idCard.text isEqualToString:@""]){
        [Alert alert:@"请输入被保险人身份证号"];
        return NO;
    }
    
    if(![ValidateUtil isIdCard:_idCard.text])
    {
        [Alert alert:@"请输入正确的被保险人身份证号"];
        return NO;
    }
    
    SafeContact* c = [[SafeContact alloc]init];
    c.name = _name.text;
    c.idCard = _idCard.text;
    
    data[@"insured"]=@[c.toJson];
    
    data[@"inId"] = self.data.inId;
    data[@"typeId"] = self.data.typeId;
    data[@"count"] = WRAP_INTEGER( self.data.count );
    return YES;
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
