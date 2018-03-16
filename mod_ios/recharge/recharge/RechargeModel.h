//
//  RechargeModel.h
//  ecard
//
//  Created by randy ren on 16/2/23.
//  Copyright © 2016年 citywithincity. All rights reserved.
//
#import <DMLib/dmlib.h>
@interface RechargeModel : DMModel

-(void)submit:(NSString*)cardId fee:(NSInteger)fee button:(UIView*)button;

//退款
-(void)refund:(NSString*)id button:(UIView*)button;

-(void)invoce:(NSString*)id button:(UIView*)button;

@end