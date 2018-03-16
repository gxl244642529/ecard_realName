//
//  DMFormValidateUtil.m
//  dmlib
//
//  Created by 任雪亮 on 17/1/20.
//  Copyright © 2017年 任雪亮. All rights reserved.
//

#import "DMFormValidateUtil.h"
#import "ValidateUtil.h"

/**
 规则
 */
@protocol IRule <NSObject>

-(BOOL)validate:(id)value error:(NSString**)error;

@end


@interface PostCodeRule : NSObject<IRule>

@end


@implementation PostCodeRule




-(BOOL)validate:(id)value error:(NSString *__autoreleasing *)error{
    
    if([ValidateUtil isPostCode:value]){
        
        
        return TRUE;
    }
    
    *error = @"请输入6位邮编号码";
    
    return FALSE;
}

@end


////////////////////////电话号码
@interface PhoneRule : NSObject<IRule>

@end

@implementation PhoneRule

-(BOOL)validate:(id)value error:(NSString *__autoreleasing *)error{
    
    if([ValidateUtil isPhoneNumber:value]){
        return TRUE;
    }
    
    *error = @"请输入正确的电话号码";
    
    return FALSE;
}

@end



NSMutableDictionary* g_validateMap;

@interface DMFormValidateUtil()
{
    
}

@end



@implementation DMFormValidateUtil

+(void)initialize{
    
    
    g_validateMap = [[NSMutableDictionary alloc]init];
    
    g_validateMap[@"phone"] = [PhoneRule new];
    
    g_validateMap[@"postCode"] = [PostCodeRule new];

}


+(BOOL)validate:(id)value rule:(NSString*)rule error:(NSString**)error{
    id<IRule> r = g_validateMap[rule];
    if(r){
        return [r validate:value error:error];
    }
    return TRUE;
}




@end
