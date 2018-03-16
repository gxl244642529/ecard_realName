//
//  OrderModel.h
//  ecard
//
//  Created by randy ren on 15/4/22.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "CommonMacro.h"
#import "JsonTaskManager.h"
#import "SellingModel.h"
@interface OrderModel : NSObject<IRequestResult,IArrayRequestResult>

DECLARE_SHARED_INSTANCE_DIRECT(OrderModel)

-(void)onDestroy:(ObjectJsonTask*)task;

//收货
-(ObjectJsonTask*)confirmRecv:(NSString*)ID completion:(void (^)(void))completion;

//关闭
-(ObjectJsonTask*)closeOrder:(NSString*)ID completion:(void (^)(void))completion;



-(void)getList:(NSObject<IArrayRequestResult>*)listener state:(int)state;
-(void)reloadList;

-(SOrderListVo*)getItem:(NSInteger)index;

-(ObjectJsonTask*)submit:(int)addressID title:(NSString*)title list:(NSArray*)list invoice:(BOOL)invoice  completion:(void (^)(id result))completion fail:(void (^)())fail;

@end
