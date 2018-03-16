//
//  SAddrManager.m
//  ecard
//
//  Created by randy ren on 15-1-29.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "SAddrManager.h"
#import "ECardTaskManager.h"
#import <DMLib/dmlib.h>

#define ADDR_LIST @"s_addr_list3"
@interface SAddrManager()
{
    ArrayJsonTask* _listTask;
    ObjectJsonTask* _deleteTask;
    ObjectJsonTask* _updateTask;
    ObjectJsonTask* _addTask;
}

@end

@implementation SAddrManager

IMPLEMENT_SHARED_INSTANCE_DIRECT(SAddrManager);

-(void)dealloc{
    _updateTask = nil;
    _deleteTask = nil;
    _listTask = nil;
}


-(void)createListTask{
    
    if(!_listTask){
        _listTask = [[ECardTaskManager sharedInstance]createArrayJsonTask:ADDR_LIST cachePolicy:DMCachePolicy_NoCache
                                                                isPrivate:YES ];
        [_listTask setClass:[SAddrListVo class]];
    }
    
}

-(void)getList:(NSObject<IArrayRequestResult>*)listener controller:(UIViewController*)controller{
    [self createListTask];
    _listTask.successListener = nil;
    _listTask.errorListener = nil;
    [_listTask setListener:listener];
    [_listTask execute];
}
-(void)destroy{
    instance = NULL;
}
-(void)deleteData:(SAddrListVo*)data listener:(NSObject<IRequestResult>*)listener{
    if(!_deleteTask){
        _deleteTask = [[ECardTaskManager sharedInstance]createObjectJsonTask:@"s_addr_del3" cachePolicy:DMCachePolicy_NoCache];
    }
    [_deleteTask put:@"id" value:[NSString stringWithFormat:@"%d",data.ID]];
    __weak ObjectJsonTask* __deleteTask = _deleteTask;
    __weak ArrayJsonTask* __listTask = _listTask;
    __weak NSObject<IRequestResult>* __listener = listener;
    __weak NSObject* __data = data;
    _deleteTask.successListener = ^(id result){
        [__listTask clearCache];
        [__listener task:__deleteTask result:__data];
    };
    _deleteTask.errorListener = ^(NSString* err,BOOL isNet){
        [__listener task:__deleteTask error:err isNetworkError:isNet];
    };

    [_deleteTask execute];
}
-(void)addData:(NSDictionary*)data listener:(NSObject<IRequestResult>*)listener{
    if(!_addTask){
        _addTask =[[ECardTaskManager sharedInstance]createObjectJsonTask:@"s_addr_add4" cachePolicy:DMCachePolicy_NoCache];
    }
    [_addTask putAll:data];
     __weak NSObject<IRequestResult>* __listener = listener;
    __weak ObjectJsonTask* __addTask = _addTask;
     __weak ArrayJsonTask* __listTask = _listTask;
    __weak NSDictionary* __data = data;
    _addTask.successListener = ^(id result){
        [__listTask clearCache];
        //id
        
        SAddrListVo* newData = [[SAddrListVo alloc]init];
        [ReflectUtil jsonToObject:__data src:newData];
        newData.ID = [result intValue];
        [__listener task:__addTask result:newData];
    };
    _addTask.errorListener = ^(NSString* err,BOOL isNet){
        [__listener task:__addTask error:err isNetworkError:isNet];
    };
    [_addTask execute];
}

-(void)updateData:(NSDictionary*)data oldData:(SAddrListVo*)oldData id:(id)ID listener:(NSObject<IRequestResult>*)listener{
    if(!_updateTask){
        _updateTask = [[ECardTaskManager sharedInstance]createObjectJsonTask:@"s_addr_edit3" cachePolicy:DMCachePolicy_NoCache];
    }
    [_updateTask put:@"id" value:ID];
    [_updateTask putAll:data];
    __weak ObjectJsonTask* __updateTask = _updateTask;
     __weak NSObject<IRequestResult>* __listener = listener;
     __weak ArrayJsonTask* __listTask = _listTask;
    __weak NSDictionary* __data = data;
    __weak SAddrListVo* __oldData = oldData;
    _updateTask.successListener = ^(id result){
        [__listTask clearCache];
        [ReflectUtil jsonToObject:__data src:__oldData];
        [__listener task:__updateTask result:__oldData];
    };
    _updateTask.errorListener = ^(NSString* err,BOOL isNet){
        [__listener task:__updateTask error:err isNetworkError:isNet];
    };
    [_updateTask execute];
    
}
-(JsonTask*)getDefaultAddr:(void(^)(SAddrListVo* defaultAddr))listener{
    [self createListTask];
    [_listTask setListener:NULL];
    _listTask.successListener = ^(NSArray* result,NSInteger position,BOOL isLastPage){
        for (SAddrListVo* data in result) {
            if(data.def > 0){
                listener(data);
                return;
            }
        }
        listener(NULL);
    };
    _listTask.errorListener=^(NSString* errMsg,BOOL isNetwork){
        [SVProgressHUD showErrorWithStatus:errMsg];
    };
    [_listTask execute];
    return _listTask;
}


-(void)destroyTask:(JsonTask*)task{
    if(task == _listTask){
        _listTask = nil;
    }
}




@end
