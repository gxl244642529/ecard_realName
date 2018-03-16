//
//  CryptAES.m
//  ecard
//
//  Created by randy ren on 15/8/27.
//  Copyright (c) 2015年 citywithincity. All rights reserved.
//

#import "CryptAES.h"
#import "Aes.h"

@implementation CryptAES


+(NSString *) encrypt:(NSString *)text key:(NSString *)key
{
    Aes* aes = [[Aes alloc]initWithKey:key];
    return [aes encrpyt:text];
    
}

//解密
+(NSString*) decrypt:(NSString*)cipherText key:(NSString*)key {
     Aes* aes = [[Aes alloc]initWithKey:key];
    return [aes decrpyt:cipherText];

 }


@end
