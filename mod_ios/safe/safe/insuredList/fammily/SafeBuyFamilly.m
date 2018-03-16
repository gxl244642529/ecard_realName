//
//  SafeBuyFamilly.m
//  ecard
//
//  Created by 任雪亮 on 16/3/8.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "SafeBuyFamilly.h"
#import "SafeInsuredListView.h"
#import "RealInfo.h"
#import "SafePayHandler.h"
#import "SafeUtil.h"
#import "SafeSelContactController.h"
#import "SafeModel.h"



@interface SafeBuyFamilly ()
{
    SafePayHandler* _handler;
    __weak id<DMFormElement> _element;
}
@property (weak, nonatomic) IBOutlet CommonConditionButton *btnSubmit;
@property (weak, nonatomic) IBOutlet DMFormView *formView;

@property (weak, nonatomic) IBOutlet SafeInsuredListView *insuredListView;
@property (weak, nonatomic) IBOutlet UIView *extra;
@property (weak, nonatomic) IBOutlet UILabel *tip;

@end

@implementation SafeBuyFamilly


@synthesize data = _data;


INIT_BUNDLE_CONTROLLER(SafeBuyFamilly, safebundle.bundle)

- (void)viewDidLoad {
    [super viewDidLoad];
    [self setTitle:@"购买"];
    self.view.backgroundColor = RGB_WHITE(f2);
    _handler = [[SafePayHandler alloc]initWithParent:self];
    _handler.payCancelAction = PayCancelAction_BacktoPreviousController;
    
    [_btnSubmit setEnabled:NO];
    
    UIView* extraView;
    switch(_data.type){
        case TYPE_FAMILLY:
        {
            extraView= [DMViewUtil createViewFormNibName:@"SafeBuyAddressView" bundle:CREATE_BUNDLE(safebundle.bundle)];
        }
            break;
        case TYPE_CAR:
        {
            extraView = [DMViewUtil createViewFormNibName:@"SafeBuyCarView" bundle:CREATE_BUNDLE(safebundle.bundle)];
        }
            break;
    }
    if(extraView){
        extraView.frame = CGRectMake(0, 0, SCREEN_WIDTH, extraView.frame.size.height);
        [_extra addSubview:extraView];
        [_extra findHeight].constant = extraView.frame.size.height;
        _element = (id<DMFormElement>)extraView;
    }
    
    if([_data.inId isEqualToString:@"2003"]){
        _insuredListView.isMutil = YES;
    }else{
        _insuredListView.isMutil = NO;
        _tip.hidden = YES;
        [_tip findHeight].constant = 0;
    }
    
}
- (IBAction)onContact:(id)sender {
    SafeSelContactController* c = [[SafeSelContactController alloc]init];
    c.data = _data;
    c.delegate = self;
    if([_insuredListView  getInsured]){
        [c setSelected:[_insuredListView getInsured]];
    }
    
    [self.navigationController pushViewController:c animated:YES];
    
    
}

/**
 如果是单选，则替换掉，如果是多选，也是替换掉
 */
-(void)onSelectContacts:(NSArray*)arr{
    if(arr.count > 0){
        
        [_insuredListView onSelect:arr];
        
    }
}

API_JOB_SUCCESS(user, realInfo, RealInfo*){
    [_btnSubmit setEnabled:YES];
    
    if(result.isValid){
        
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
    if(_element){
        NSString* error = [_element validate:data];
        if(error){
            [Alert alert:error];
            return NO;
        }
    }
    
    
    data[@"inId"] = self.data.inId;
    data[@"typeId"] = self.data.typeId;
    data[@"count"] = WRAP_INTEGER( self.data.count );
    return YES;
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
