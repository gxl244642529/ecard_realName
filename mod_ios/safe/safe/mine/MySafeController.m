//
//  MySafeController.m
//  ecard
//
//  Created by randy ren on 16/1/23.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import "MySafeController.h"
#import "MyCardSafeDetailController.h"
#import "MySafeDetailController.h"
#import "MySafeOrderController.h"
#import "SafePayHandler.h"
#import "SafeModel.h"
#import "SafeUtil.h"

@interface MySafeController()
{
    SafePayHandler* _handler;
    SafeModel* _model;
    MySafeVo* _submitData;
    __weak IBOutlet DMStateTabView *_tabView;
}

@end

@implementation MySafeController

INIT_BUNDLE_CONTROLLER(MySafeController, safebundle.bundle)

-(void)dealloc{
    _handler = nil;
    _model = nil;
}
-(void)updateListView{
    
    [_tabView reloadWithStatus];
    
}



//获取下标
-(NSInteger)getDataIndex:(MySafeVo*)data{
    
    
    if(data.status == 3 || data.status == 4 || data.status == 5){
        
        return 1;
        
    }else{
        
        return 0;
    }
    
}

-(void)viewDidLoad{
    [super viewDidLoad];
    [self setTitle:@"我的保单"];
    self.view.backgroundColor = RGB_WHITE(f2);
    [_tabView setOnitemClickListener:self];
    _tabView.dataSource = self;
}

-(void)gotoPay:(MySafeVo*)data{
    //去付款
    
    if(!_model){
        _model = [[SafeModel alloc]init];
    }
    _submitData = data;
    [_model getPayInfo:data];
}




API_JOB_SUCCESS(i_m_safe, lost, id){
    [_tabView reloadWithStatus];
}

API_JOB_SUCCESS(i_safe, payInfo, id){
    if(!_handler){
        _handler = [[SafePayHandler alloc]initWithParent:self];
        _handler.payCancelAction = PayCancelAction_BacktoCurrentController;
        _handler.paySuccessAction = PaySuccessAction_BacktoCurrentController;
    }
    
    //这里进入收银台
    NSMutableDictionary* data = [[NSMutableDictionary alloc]initWithDictionary:result];
    //这里修改几个字段
    data[@"guardUrl"] = [SafeUtil getUrl:data[@"guardUrl"]];
    //infos
    NSMutableArray* infos = [[NSMutableArray alloc]init];
    NSInteger type = [[result valueForKey:@"typeId"]integerValue];
    switch(type){
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

-(void)onItemClick:(UIView *)parent data:(MySafeVo *)data index:(NSInteger)index{
    if(data.typeId == 0){
        //卡保险
        Push_ViewController_Data(MyCardSafeDetailController,data);
    }else{
        if(data.status==7){
            [Alert confirm:@"本订单没有付款，去付款吗" confirmListener:^(NSInteger buttonIndex){
                if(buttonIndex==1){
                    __weak typeof(self) __self = self;
                    [__self gotoPay:data];
                }
            }];
            
        }else{
            //其他保险
            Push_ViewController_Data(MySafeOrderController,data);
        }
    }
    
}

@end
