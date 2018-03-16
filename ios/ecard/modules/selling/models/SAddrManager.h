//
//  SAddrManager.h
//  ecard
//
//  Created by randy ren on 15-1-29.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "SellingModel.h"
#import "ECardTaskManager.h"

#define REQUEST_ADD 1
#define REQUEST_EDIT 2


@protocol AddrDelegate <NSObject>
-(void)onGetDefaultAddr:(SAddrListVo*)data;
@end

@interface SAddrManager : NSObject

DECLARE_SHARED_INSTANCE_DIRECT(SAddrManager);

-(void)destroyTask:(JsonTask*)task;
//获取默认地址
-(JsonTask*)getDefaultAddr:(void(^)(SAddrListVo* defaultAddr))listener;


-(void)getList:(NSObject<IArrayRequestResult>*)listener controller:(UIViewController*)controller;

-(void)deleteData:(SAddrListVo*)data listener:(NSObject<IRequestResult>*)listener;

-(void)addData:(NSDictionary*)data listener:(NSObject<IRequestResult>*)listener;

-(void)updateData:(NSDictionary*)data oldData:(SAddrListVo*)oldData id:(id)ID listener:(NSObject<IRequestResult>*)listener;




@end
