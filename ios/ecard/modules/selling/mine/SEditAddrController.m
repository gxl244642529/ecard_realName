//
//  SEditAddrController.m
//  ecard
//
//  Created by randy ren on 15-1-8.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "SEditAddrController.h"
#import "SAddAddrView.h"
#import "SellingModel.h"


#import "SAddrManager.h"
@interface SEditAddrController ()
{
    FormModel* _model;
    SAddrListVo* _addrData;
    SAddAddrView* _content;
    
    //加载地区信息
    ArrayJsonTask* _task;
    
}
@end

@implementation SEditAddrController
-(void)dealloc{
    _task = NULL;
    _model = NULL;
    _addrData = NULL;
    _content = NULL;
    [SVProgressHUD dismiss];
}
-(SAddrListVo*)addrData{
    return _addrData;
}
- (void)viewDidLoad
{
    [super viewDidLoad];
     self.view.backgroundColor = RGB(f2, f2, f2);
    FormScrollView* scrollView = [[FormScrollView alloc]initWithFrame:[UIScreen mainScreen].bounds contentNibName:@"SAddAddrView"];
    [self.view addSubview:scrollView];
    SAddAddrView* content=[scrollView contentView];
    _content = content;
    [content.btnSelectArea setTarget:self withAction:@selector(onSelectArea:)];
    _model = [[FormModel alloc]initWithListener:self];
    [content.btnSetDef setTarget:self withAction:@selector(onSetDef:)];
    [_model add:content.chkDef name:@"def"];
    [_model add:content.txtAddr name:@"jie" rules:RULES_REQUIRED];
    [_model add:content.txtName  name:@"name" rules:RULES_REQUIRED];
    [_model add:content.txtPhone name:@"phone" rules:RULES_REQUIRED];
    [_model add:content.txtPostCode name:@"pc" rules:RULES_REQUIRED returnKeyType:UIReturnKeyDone];
     if(self.mode == AddrEditMode_Edit){
        //编辑
        _addrData = [ReflectUtil copyObject:self.data];
        content.detailAddr.text = [_addrData getArea];
        //设置值
        [content.txtPhone setText:_addrData.phone];
        [content.txtAddr  setText:_addrData.jie];
        [content.txtName  setText:_addrData.name];
        [content.txtPostCode  setText:_addrData.pc];
        
        [self setTitle:@"编辑收货人信息"];
         if(_addrData.def){
            content.btnSetDef.hidden = YES;
            content.chkDef.selected = YES;
         }else{
            content.chkDef.selected = NO;
         }
    }else{
        [self setTitle:@"增加收货人信息"];
        _addrData = [[SAddrListVo alloc]init];
    }
    
    UIButton* button = [self createTitleButton:@"保存"];
    [_model addOkButton:button];

}

ON_EVENT(onSetDef){
    _content.chkDef.selected = !_content.chkDef.selected;
}


ON_EVENT(onSelectArea){
    [_model resignFirstResponder];
    //加载地区信息
    
    if(!_task){
        _task = [[ECardTaskManager sharedInstance]createArrayJsonTask:@"s_ssq" cachePolicy:DMCachePolicy_Permanent isPrivate:NO];
        
        __weak SEditAddrController* __self = self;
        
        [_task setClass:[AddrInfo class]];
        [SVProgressHUD showWithStatus:@"加载省市区..."];
        _task.successListener = ^(NSArray* result,NSInteger position,BOOL isLastPage){
            [SVProgressHUD dismiss];
            UIAreaPicker* picker = [[UIAreaPicker alloc]initWithSheng:result];
            [__self.view addSubview:picker];
            SAddrListVo* listVo = __self.addrData;
            picker.province_id = listVo.sheng_id ? listVo.sheng_id :350000;
            picker.city_id = listVo.shi_id? listVo.shi_id : 350200;
            picker.area_id = listVo.qu_id ? listVo.qu_id : 350203;
            
            
            picker.delegate = __self;
            [picker show];
        };
        _task.errorListener = ^(NSString* error,BOOL isNetworkError){
            [SVProgressHUD showErrorWithStatus:error];
        };
    }
    [_task execute];
    
    
}
-(void)popupView:(UIAreaPicker*)popup contentView:(UIView*)contentView result:(UIPopupResult)result{
    if(result == UIPopupResultOk){
        _addrData.sheng_id = popup.province_id;
        _addrData.shi_id =popup.city_id;
        _addrData.qu_id =popup.area_id;
        _addrData.sheng = popup.province;
         _addrData.shi = popup.city;
         _addrData.qu = popup.area;
        
        _content.detailAddr.text = [_addrData getArea];

    }
    
}
-(void)onFormError:(NSString*)error{
    
    [SVProgressHUD showErrorWithStatus:error];
    
}
-(void)onFormSubmit:(NSMutableDictionary*)data{
    
    if(!_addrData.sheng_id || !_addrData.shi_id){
        [SVProgressHUD showErrorWithStatus:@"请选择收货地区"];
        return;
    }
    
    NSString* pc = data[@"pc"];
    if(pc.length!=6){
        [SVProgressHUD showErrorWithStatus:@"邮编长度为6位"];
        return;
    }
    
    [data setValue:[NSString stringWithFormat:@"%ld",(long)_addrData.sheng_id] forKey:@"sheng_id"];
    [data setValue:[NSString stringWithFormat:@"%ld",(long)_addrData.shi_id] forKey:@"shi_id"];
    if(_addrData.qu_id>0){
        [data setValue:[NSString stringWithFormat:@"%ld",(long)_addrData.qu_id] forKey:@"qu_id"];
    }
    
    
    if(self.mode == AddrEditMode_Edit){
        [[SAddrManager sharedInstance]updateData:data oldData:self.data id:[NSNumber numberWithInt:_addrData.ID] listener:self];
    }else{
        [[SAddrManager sharedInstance]addData:data listener:self];
    }
    
    
    [SVProgressHUD showWithStatus:@"正在保存地址..."];
    
}

-(void)task:(NSObject<IJsonTask>*)task result:(id)result{
    [SVProgressHUD showSuccessWithStatus:@"保存地址成功"];
    SAddrListVo* listVo = result;
    if(_requstCode==REQUEST_ADD){
        listVo.sheng = _addrData.sheng;
        listVo.shi = _addrData.shi;
        listVo.qu = _addrData.qu;
    }
    [self setResult:RESULT_OK data:result];
}
-(void)task:(NSObject<IJsonTask> *)task error:(NSString *)errorMessage isNetworkError:(BOOL)isNetworkError{
    [SVProgressHUD showErrorWithStatus:errorMessage];
}
- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
