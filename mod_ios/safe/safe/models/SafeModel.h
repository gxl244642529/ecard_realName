//
//  SafeModel.h
//  ecard
//
//  Created by randy ren on 16/1/27.
//  Copyright © 2016年 citywithincity. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <DMLib/dmlib.h>
#import "MySafeClaimInfo.h"

#import "MySafeVo.h"
#define TYPE_CAR 5
#define TYPE_FAMILLY 2
@interface SafeModel : DMModel


-(void)submitInsured:(NSMutableDictionary*)data typeId:(NSString*)typeId inId:(NSString*)inId count:(NSInteger)count button:(UIButton*)button;

-(void)submit:(NSMutableDictionary*)data button:(UIButton*)button;

-(void)canBuyCardSafe:(NSString*)cardId idCard:(NSString*)idCard ticket:(NSString*)ticket button:(UIButton*)button;

-(void)getPayInfo:(MySafeVo*)data;


-(void)getOrderInfo:(NSString*)orderId;



@end
