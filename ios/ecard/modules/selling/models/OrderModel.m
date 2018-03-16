//
//  OrderModel.m
//  ecard
//
//  Created by randy ren on 15/4/22.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "OrderModel.h"
#import "CardModel.h"

@interface OrderModel()
{

    //关闭订单
    ObjectJsonTask* _closeTask;
    
    //确认收货
    ObjectJsonTask* _confirmTask;
    
    //提交订单
    ObjectJsonTask* _submitTask;
    
    //列表
    ArrayJsonTask* _listTask;
    
    __weak NSObject<IArrayRequestResult>* _listener;
    int _state;
    
    NSMutableArray* _data;
}

@end

@implementation OrderModel

-(void)dealloc{
    _submitTask = nil;
    _data = nil;
    _listTask = nil;
    _closeTask = nil;
    _confirmTask = nil;
}
-(void)onDestroy:(ObjectJsonTask*)task{
    if(_confirmTask == task){
        _confirmTask = nil;
    }
    
    if(_closeTask == task){
        _closeTask = nil;
    }
    
    if(_submitTask == task){
        _submitTask = nil;
        [SVProgressHUD dismiss];
    }
}
-(SOrderListVo*)getItem:(NSInteger)index{
    return _listTask.array[index];
}

IMPLEMENT_SHARED_INSTANCE_DIRECT(OrderModel)


-(ArrayJsonTask*)createListTask{
  ArrayJsonTask* task =[[JsonTaskManager sharedInstance]createArrayJsonTask:@"s_order_list3" cachePolicy:DMCachePolicy_NoCache isPrivate:YES];
  
  [task setClass:[SOrderListVo class]];
  [task setPosition:Start_Position];
  [task setListener:self];
  return task;
}

-(void)setChanged{
    _data = nil;
    [_listTask clearCache];
    
}

-(void)getList:(NSObject<IArrayRequestResult>*)listener state:(int)state{
    _state = state;
    _listener = listener;
   /* if(_data){
        if(_state>=0){
            [self parse:_data state:_state];
        }else{
             [listener task:_listTask result:_data position:Start_Position isLastPage:YES];
        }
       
        
    }else*/{
        _listTask=[self createListTask];
        [_listTask execute];
    }
}

-(ObjectJsonTask*)closeOrder:(NSString*)ID completion:(void (^)(void))completion{
    if(!_closeTask){
        _closeTask = [[JsonTaskManager sharedInstance]createObjectJsonTask:@"s_order_cancel3" cachePolicy:DMCachePolicy_NoCache];
        [_closeTask setListener:self];
    }
    _closeTask.successListener = ^(id result){
        completion();
    };
    [_closeTask setWaitMessage:@"正在取消订单..."];
    [_closeTask put:@"id" value:ID];
    [_closeTask execute];

    return _closeTask;
}


-(ObjectJsonTask*)confirmRecv:(NSString*)ID completion:(void (^)(void))completion{
    //确认收货
    if(!_confirmTask){
        _confirmTask = [[JsonTaskManager sharedInstance]createObjectJsonTask:@"s_order_confirm3" cachePolicy:DMCachePolicy_NoCache];
        [_confirmTask setWaitMessage:@"正在确认订单..."];
        [_confirmTask setListener:self];
    }
    _confirmTask.successListener = ^(id result){
       
        completion();
    };
    [_confirmTask put:@"id" value:ID];
    [_confirmTask execute];
    
    return _confirmTask;
}


-(ObjectJsonTask*)submit:(int)addressID title:(NSString*)title list:(NSArray*)list invoice:(BOOL)invoice  completion:(void (^)(id result))completion fail:(void (^)())fail{
     BOOL containsDiy = NO;
    for (CartVo* data in list) {
        if([data isKindOfClass:[DiyVo class]]){
            containsDiy = YES;
        }
    }
    [SVProgressHUD showWithStatus:containsDiy ? @"本订单需要上传DIY图片数据,可能需要1分钟或更长时间,请耐心等待..." : @"提交订单..."];
    if(!_submitTask){
        _submitTask = [[JsonTaskManager sharedInstance]createObjectJsonTask:@"s_order_submit3" cachePolicy:DMCachePolicy_NoCache];
        [_submitTask setListener:self];
    }

    __weak NSArray* __list = list;
    _submitTask.successListener = ^(id result){
        //刷新购物车内容
        for (CartVo* data in __list) {
            if(data.cartID){
                //删除
                [[CartModel sharedInstance]removeObject:data];
            }
            [[CartModel sharedInstance]notifyListHasChanged];
        }
        
        completion(result);
    };
    
    _submitTask.errorListener = ^(NSString* error,BOOL isNetwork){
        
        fail();
    };
    dispatch_async(GLOBAL_QUEUE, ^{
        [_submitTask put:@"address_id" value:[NSString stringWithFormat:@"%d",addressID]];
        CartVo* cartVo = list[0];
        [_submitTask put:@"title" value:cartVo.title];
        NSMutableArray* arr = [[NSMutableArray alloc]init];
        for (CartVo* data in list) {
            [arr addObject:[CartModel toJson:data]];
        }
        [_submitTask put:@"invoice" value:[NSNumber numberWithInt: (invoice ? 0 :1) ]];
        [_submitTask put:@"list" value:arr];

        dispatch_async(dispatch_get_main_queue(),^{
            [_submitTask execute];
        });
    });
    
    
    
    return _submitTask;

}
-(void)parse:(NSArray*)arr state:(int)state{
    NSMutableArray* result = [[NSMutableArray alloc]init];
    for (SOrderListVo* data in arr) {
        if(data.status == state){
            [result addObject:data];
        }
    }
    [_listener task:_listTask result:result position:Start_Position isLastPage:YES];
    
}
-(void)reloadList{
    _data = nil;
    [_listTask reload];
}
-(void)task:(id)task result:(NSArray*)result position:(NSInteger)position isLastPage:(BOOL)isLastPage{
    
    if(!_data || position == Start_Position){
        _data = [[NSMutableArray alloc]initWithArray:result];
    }else{
        [_data addObjectsFromArray:result];
    }
    
    if(_state<0){
        [_listener task:_listTask result:result position:position isLastPage:isLastPage];
    }else{
        [self parse:result state:_state];
    }
}
-(void)task:(id)task result:(id)result{
    if(task==_confirmTask){
        [SVProgressHUD showSuccessWithStatus:@"确认收货成功"];
        [_listTask reload];
    }else if(task == _closeTask){
        [SVProgressHUD showSuccessWithStatus:@"取消订单成功"];
        [_listTask reload];
    }else if(task == _submitTask){
        [SVProgressHUD dismiss];
    }
    
    [self setChanged];
}

-(void)task:(id)task error:(NSString *)errorMessage isNetworkError:(BOOL)isNetworkError{
    [SVProgressHUD showErrorWithStatus:errorMessage];
}

@end
