//
//  DMSubmitConditionButton.h
//  DMLib
//
//  Created by randy ren on 16/2/21.
//  Copyright © 2016年 damai. All rights reserved.
//
#import "DMSubmitButton.h"


/**
 在提交之前,需要先调用另外一个api检查数据
 */
IB_DESIGNABLE
@interface DMSubmitConditionButton : DMSubmitButton<DMJobDelegate>



@property (nonatomic,copy) IBInspectable NSString* checkApi;
@property (nonatomic,copy) IBInspectable NSString* checkFields;
@property (nonatomic,assign) IBInspectable NSInteger checkCrypt;
@end
